package cz.mendelu.pef.xchomo.travelsnap.fake

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.location.ILocationManager
import cz.mendelu.pef.xchomo.travelsnap.location.LocationManager
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MockAddScreenViewModel: AddPlaceViewModel(
    repository = FakePlacesRemoteRepositoryImpl(),
    databaseRepository = FakePlacesRepositoryImpl(),
    locationManager = FakeLocationManagerImpl()
) {



    override val placesUIState: MutableState<UIState<PlacesResponse, AddPlaceErrors>>
            = mutableStateOf(UIState())
    override var performSearch: MutableState<Boolean> = mutableStateOf(true)

    override fun searchPlaceFromText(input: String, fields: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                repository.searchPlaceFromText(input)
            }
            handleCommunicationResult(result)
        }

    }


    override fun performNearbySearch(latitude: Double, longitude: Double) {

        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                repository.searchNearbyPlaces("${latitude},${longitude}", 1500)
            }
            handleCommunicationResult(result)
        }
    }

    override fun insertPlace(place: Place) {
        place.selectedDate = LocalDate.now().toString()
    }

    fun handleCommunicationResult(result: CommunicationResult<PlacesResponse>) {
        when (result) {
            is CommunicationResult.Success -> {
                placesUIState.value = UIState(
                    loading = false,
                    data = result.data
                )
            }
            is CommunicationResult.Error -> {
                placesUIState.value = UIState(
                    loading = false,
                    errors = AddPlaceErrors(communicationError = 404)
                )
            }

            else -> {
                placesUIState.value = UIState(
                    loading = false,
                    errors = AddPlaceErrors(communicationError = 404)
                )
            }
        }
    }
}