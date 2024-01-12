package cz.mendelu.pef.xchomo.travelsnap.fake

import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import java.time.LocalDate

class MockMainScreenViewModel : MainScreenViewModel(
    placesDatabaseRepository = FakePlacesRepositoryImpl(),
    visionApiRepositoryImpl = FakeVisionRemoteRepositoryImpl(),
    placesRemoteRepositoryImpl = FakePlacesRemoteRepositoryImpl(),
    authRemoteRepositoryImpl = MockAuthRemoteRepository()
) {
    override suspend fun loadPlacesForSelectedDate(selectedDate: LocalDate) {
        val places = placesDatabaseRepository.getPlacesForDate("2024-01-01")
        placesUIState.value = UIState(loading = false, data = listOf( places[0]))
    }




}