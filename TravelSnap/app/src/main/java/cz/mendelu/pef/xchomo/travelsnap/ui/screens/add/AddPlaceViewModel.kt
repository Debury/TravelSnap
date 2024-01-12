package cz.mendelu.pef.xchomo.travelsnap.ui.screens.add

import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.api.IPlacesRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.api.PlacesRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.architecture.BaseViewModel
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.database.IPlacesRepository
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.location.ILocationManager
import cz.mendelu.pef.xchomo.travelsnap.location.LocationManager
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
open class AddPlaceViewModel @Inject constructor(
    val repository: IPlacesRemoteRepository,
    val databaseRepository : IPlacesRepository,
    val locationManager: ILocationManager
): BaseViewModel() {

    open val placesUIState: MutableState<UIState<PlacesResponse, AddPlaceErrors>>
            = mutableStateOf(UIState())
    open var performSearch: MutableState<Boolean> = mutableStateOf(true)



    open fun searchPlaceFromText(input: String, fields: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                repository.searchPlaceFromText(input)
            }
            handleCommunicationResult(result)
        }

    }

    open fun performNearbySearch(latitude: Double, longitude: Double) {

        launch {
            val result = withContext(Dispatchers.IO) {
                repository.searchNearbyPlaces("${latitude},${longitude}", 1500)
            }
            Log.d("AddPlaceViewModel", "Result: $result")
            handleCommunicationResult(result)
        }
    }
    private fun handleCommunicationResult(result: CommunicationResult<PlacesResponse>) {
        when (result) {
            is CommunicationResult.CommunicationError ->
                placesUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.no_internet_connection)
                )

            is CommunicationResult.Error ->
                placesUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.failed_to_load_the_list)
                )

            is CommunicationResult.Exception ->
                placesUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )

            is CommunicationResult.Success ->
                placesUIState.value = UIState(
                    loading = false,
                    data = result.data,
                    errors = null
                )


            else -> {
                placesUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )}
        }
    }

    open fun insertPlace(place: Place) {
        place.selectedDate = LocalDate.now().toString()
        viewModelScope.launch {
            try {
                databaseRepository.insert(place)
            } catch (e: Exception) {
                Log.d("AddPlaceViewModel", "Error: $e")
            }
        }
    }


}