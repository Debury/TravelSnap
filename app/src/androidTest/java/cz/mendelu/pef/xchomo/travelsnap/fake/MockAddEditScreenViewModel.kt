package cz.mendelu.pef.xchomo.travelsnap.fake

import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import java.time.LocalDate


import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.addedit.AddEditScreenViewModel
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors
import kotlinx.coroutines.launch

class MockAddEditScreenViewModel : AddEditScreenViewModel(
    repository = FakePlacesRemoteRepositoryImpl(), // Replace with your fake repository
    databaseRepository = FakePlacesRepositoryImpl(), // Replace with your fake repository
    locationManager = FakeLocationManagerImpl() // Replace with your fake location manager
) {
    override fun getPlaceDetails(placeId: String) {

    }

    override fun insertPlace(place: Place) {

    }

    override fun updatePlace(place: Place) {

    }

    override fun getPlaceById(id: Long?) {
        launch {
            val mockPlace = databaseRepository.getPlaceById(1L)
            placeUIState.value = UIState(
                loading = true,
                data = mockPlace,
                errors = null
            )
        }
    }


    override fun deletePlace(place: Long?) {

    }
}
