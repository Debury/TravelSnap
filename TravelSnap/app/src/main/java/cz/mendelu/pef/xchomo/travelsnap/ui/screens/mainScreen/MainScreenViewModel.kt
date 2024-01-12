package cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen

import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.api.IPlacesRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.IVisionRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.PlacesRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.api.VisionRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.architecture.BaseViewModel
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.auth.IAuthRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.database.IPlacesRepository
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.datastore.BitmapDataStore
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.model.VisionAPIResponse
import cz.mendelu.pef.xchomo.travelsnap.model.VisionApiFeature
import cz.mendelu.pef.xchomo.travelsnap.model.VisionApiImage
import cz.mendelu.pef.xchomo.travelsnap.model.VisionApiRequest
import cz.mendelu.pef.xchomo.travelsnap.model.VisionApiRequestBody
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
open class MainScreenViewModel @Inject constructor(
    val placesDatabaseRepository: IPlacesRepository,
    private val visionApiRepositoryImpl: IVisionRemoteRepository,
    private val placesRemoteRepositoryImpl: IPlacesRemoteRepository,
    val authRemoteRepositoryImpl: IAuthRemoteRepository
) : BaseViewModel() {



    val placesUIState: MutableState<UIState<List<Place>, Int>>
            = mutableStateOf(UIState())

    val visionUIState: MutableState<UIState<VisionAPIResponse, AddPlaceErrors>>
            = mutableStateOf(UIState(loading = false))



    val placeIdUIState: MutableState<UIState<String, AddPlaceErrors>>
            = mutableStateOf(UIState(loading = false))
    init {
        launch {
            loadPlacesForSelectedDate(LocalDate.now())
        }
    }
    open suspend fun loadPlacesForSelectedDate(selectedDate: LocalDate) {
        viewModelScope.launch {
            placesUIState.value = UIState(loading = true)
            try {
                val places = placesDatabaseRepository.getPlacesForDate(selectedDate.toString())
                placesUIState.value = UIState(loading = false, data = places)
            } catch (e: Exception) {
                val errorResId = mapExceptionToErrorResource(e)
                placesUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = errorResId
                )
            }
        }
    }

    fun detailPlace(place: Place){
        Log.d("MainScreenViewModel", "place: $place")
    }
    private fun mapExceptionToErrorResource(exception: Exception): Int {
        val databaseError = when (exception) {
            is SQLiteException -> DatabaseError.DatabaseAccessError
            is IllegalStateException -> DatabaseError.UnexpectedError(exception.message)
            else -> DatabaseError.UnexpectedError(exception.message)
        }
        return databaseError.getErrorStringResId()
    }

    fun checkImageWithVisionApi(image: Bitmap){
        visionUIState.value.loading = true
        launch {
            val result = withContext(Dispatchers.IO) {
                val maxResults = 1
                val base64Image = bitmapToBase64(image)
                val requestBody = createVisionApiRequestBody(base64Image, maxResults)
                visionApiRepositoryImpl.detectLandMarks(requestBody)
            }
            handleCommunicationResult(result)
            if (result is CommunicationResult.Success){
                if(result.data.responses!!.isEmpty()){
                    visionUIState.value.loading = false
                    visionUIState.value = UIState(
                        loading = false,
                        data = null,
                        errors = AddPlaceErrors(R.string.unknown_error)
                    )
                }else {
                    getId(
                        visionUIState.value.data?.responses?.get(0)?.landmarkAnnotations?.get(0)?.description
                            ?: ""
                    )
                }
            }
        }
    }

    suspend fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun handleCommunicationResult(result: CommunicationResult<VisionAPIResponse>) {
        when (result) {
            is CommunicationResult.CommunicationError ->
                visionUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.no_internet_connection)
                )

            is CommunicationResult.Error ->
                visionUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.failed_to_load_the_list)
                )

            is CommunicationResult.Exception ->
                visionUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )

            is CommunicationResult.Success ->
                visionUIState.value = UIState(
                    loading = false,
                    data = result.data,
                    errors = null
                )


            else -> {
                visionUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )
            }
        }
    }

    fun createVisionApiRequestBody(base64Image: String, maxResults: Int): VisionApiRequestBody {
        val feature = VisionApiFeature(maxResults)
        val image = VisionApiImage(base64Image)
        val request = VisionApiRequest(image, listOf(feature))
        return VisionApiRequestBody(listOf(request))
    }





    fun getId(placeName:String){
        launch {
            val result = withContext(Dispatchers.IO) {
                placesRemoteRepositoryImpl.searchPlaceFromText(placeName)
            }
            Log.d("MainScreenViewModel", "Result: $result")
            if(result is CommunicationResult.Success){
                if(result.data.candidates?.isNotEmpty() == true){
                    visionUIState.value.loading = false
                    handlePlacesResponse(result)
                }else{
                    visionUIState.value.loading = false
                    placeIdUIState.value = UIState(
                        loading = false,
                        data = null,
                        errors = AddPlaceErrors(R.string.unknown_error)
                    )
                }
            }



        }
    }

    private fun handlePlacesResponse(result: CommunicationResult<PlacesResponse>){
        when (result) {
            is CommunicationResult.CommunicationError ->
                placeIdUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.no_internet_connection)
                )
            is CommunicationResult.Error ->
                placeIdUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.failed_to_load_the_list)
                )
            is CommunicationResult.Exception ->
                placeIdUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )
            is CommunicationResult.Success ->
                placeIdUIState.value = UIState(
                    loading = false,
                    data = result.data.candidates?.get(0)?.id,
                    errors = null
                )
            else -> {
                placeIdUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )
            }

        }
    }

    fun logout(){
        launch {
            authRemoteRepositoryImpl.logout()
        }
    }
}