package cz.mendelu.pef.xchomo.travelsnap.ui.screens.addedit

import android.os.Build
import androidx.annotation.RequiresApi
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class AddEditScreenViewModel@Inject constructor(

    val repository: IPlacesRemoteRepository,
    val databaseRepository : IPlacesRepository,
    val locationManager: ILocationManager
): BaseViewModel() {

    var placeUIState: MutableState<UIState<Place, AddPlaceErrors>>
            = mutableStateOf(UIState(loading = true))







    open fun getPlaceDetails(placeId: String) {
        Log.d("AddEditScreenViewModel", "placeId: $placeId")
        launch {
            val result = withContext(Dispatchers.IO) {
                repository.getPlaceDetails("$placeId")
            }
            Log.d("AddEditScreenViewModel", "Result: $result")
            handleCommunicationResult(result)
        }
    }

    private fun handleCommunicationResult(result: CommunicationResult<PlacesResponse>) {
        when (result) {
            is CommunicationResult.CommunicationError ->
                placeUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.no_internet_connection)
                )

            is CommunicationResult.Error ->
                placeUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.failed_to_load_the_list)
                )

            is CommunicationResult.Exception ->
                placeUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )

            is CommunicationResult.Success ->
                placeUIState.value = UIState(
                    loading = false,
                    data = result.data.result,
                    errors = null
                )


            else -> {
                placeUIState.value = UIState(
                    loading = false,
                    data = null,
                    errors = AddPlaceErrors(R.string.unknown_error)
                )}
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    open fun insertPlace(place: Place) {
        viewModelScope.launch {
            try {
                databaseRepository.insert(place)
            } catch (e: Exception) {
                Log.d("AddPlaceViewModel", "Error: $e")
            }
        }
    }

    open fun updatePlace(place: Place) {
        Log.d("AddPlaceViewModel", "Place: $place")
        viewModelScope.launch {
            try {
                databaseRepository.update(place)
                Log.d("AddPlaceViewModel", "Place updated successfully")
            } catch (e: Exception) {
                Log.d("AddPlaceViewModel", "Error updating place: $e")
            }
        }
    }

    open fun getPlaceById(id: Long?) {
        viewModelScope.launch {
            try {
                val result = id?.let { databaseRepository.getPlaceById(it) }
                if(result != null) {
                    placeUIState.value = UIState(
                        loading = true,
                        data = result,
                        errors = null
                    )
                }
            } catch (e: Exception) {
                Log.d("AddPlaceViewModel", "Error getting place: $e")
            }
        }
    }

    open fun deletePlace(place: Long?) {
        viewModelScope.launch {
            try {
                if (place != null) {
                    databaseRepository.delete(place)
                }
                Log.d("AddPlaceViewModel", "Place deleted successfully")
            } catch (e: Exception) {
                Log.d("AddPlaceViewModel", "Error deleting place: $e")
            }
        }
    }
}