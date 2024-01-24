package cz.mendelu.pef.xchomo.travelsnap.fake

import cz.mendelu.pef.xchomo.travelsnap.database.IPlacesRepository
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import kotlinx.coroutines.flow.Flow

import cz.mendelu.pef.xchomo.travelsnap.model.Geometry
import cz.mendelu.pef.xchomo.travelsnap.model.Location
import cz.mendelu.pef.xchomo.travelsnap.model.Photo

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakePlacesRepositoryImpl @Inject constructor() : IPlacesRepository {
    private val mockPlaces = mutableListOf<Place>()

    init {

        mockPlaces.add(Place(
            id = "mock_id",
            address = "123 Mock Street, Mock City",
            name = "Mock Place",
            rating = 4.5,
            geometry = Geometry(Location(10.0, 10.0)),
            photos = listOf(Photo("mock_photo_reference")),
            selectedDate = "2024-01-01",
            description = "Mock Description of the Place",
            time = 1234567890L,
            selectedIconId = 1

        ))

    }

    override fun getAll(): Flow<List<Place>> {
        return flow { emit(mockPlaces) }
    }

    override suspend fun insert(place: Place): Long {
        mockPlaces.add(place)
        return mockPlaces.indexOf(place).toLong() // Mock ID
    }

    override suspend fun getPlaceById(id: Long): Place {
       //Todo
        return mockPlaces[0]
    }



    override suspend fun update(place: Place) {
        val index = mockPlaces.indexOfFirst { it.id == place.id }
        if (index != -1) {
            mockPlaces[index] = place
        }
    }

    override suspend fun getPlacesForDate(date: String): List<Place> {
        return mockPlaces.filter { it.selectedDate == date }
    }

    override suspend fun delete(place: Long) {
        TODO("Not yet implemented")
    }

}