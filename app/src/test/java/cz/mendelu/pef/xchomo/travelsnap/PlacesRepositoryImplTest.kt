package cz.mendelu.pef.xchomo.travelsnap

import cz.mendelu.pef.xchomo.travelsnap.database.PlacesDao
import cz.mendelu.pef.xchomo.travelsnap.database.PlacesRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.mock.PlaceMockFactory
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

import cz.mendelu.pef.xchomo.travelsnap.model.Geometry
import cz.mendelu.pef.xchomo.travelsnap.model.Location
import cz.mendelu.pef.xchomo.travelsnap.model.Photo


@ExperimentalCoroutinesApi
class PlacesRepositoryImplTest {

    @Mock
    private lateinit var mockDao: PlacesDao

    private lateinit var placesRepositoryImpl: PlacesRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        placesRepositoryImpl = PlacesRepositoryImpl(mockDao)
    }

    @Test
    fun getAll_ReturnsFlowOfPlaces() = runBlocking {
        val places = listOf(
            PlaceMockFactory.createSamplePlace(),
            PlaceMockFactory.createCustomPlace(
                id = "4",
                name = "Custom Place 2",
                address = "Custom Address 2",
                rating = 4.2,
                description = "Custom Description 2",
                geometry = Geometry(
                    location = Location(
                        lat = 0.0,
                        lng = 0.0
                    )
                ),
                photos = listOf(
                    Photo(
                        photoReference = "Custom Photo Reference 2",
                    )
                ),
                selectedDate = "2022-01-01",
                selectedIconId = 0,
                time = System.currentTimeMillis()
            ),
            PlaceMockFactory.createCustomPlace(
                id = "5",
                name = "Custom Place 3",
                address = "Custom Address 3",
                rating = 4.2,
                description = "Custom Description 3",
                geometry = Geometry(
                    location = Location(
                        lat = 0.0,
                        lng = 0.0
                    )
                ),
                photos = listOf(
                    Photo(
                        photoReference = "Custom Photo Reference 3",
                    )
                ),
                selectedDate = "2022-01-01",
                selectedIconId = 0,
                time = System.currentTimeMillis()
            )
        )


        `when`(mockDao.getAll()).thenReturn(flowOf(places))
        val result = placesRepositoryImpl.getAll().first()
        assertEquals(places, result)
    }

    @Test
    fun insert_ReturnsInsertedPlaceId() = runBlocking {
        val customPlace = PlaceMockFactory.createCustomPlace(
            id = "3",
            name = "Custom Place 2",
            address = "Custom Address 2",
            rating = 4.2,
            description = "Custom Description 2",
            geometry = Geometry(
                location = Location(
                    lat = 0.0,
                    lng = 0.0
                )
            ),
            photos = listOf(
                Photo(
                photoReference = "Custom Photo Reference 2",
            )
            ),
            selectedDate = "2022-01-01",
            selectedIconId = 0,
            time = System.currentTimeMillis()
        )


        `when`(mockDao.insert(customPlace)).thenReturn(3L)

        val result = placesRepositoryImpl.insert(customPlace)

        assertEquals(3L, result)
    }

    @Test
    fun getPlaceById_ReturnsPlace() = runBlocking {

        val samplePlace = PlaceMockFactory.createSamplePlace()


        `when`(mockDao.getPlaceById(samplePlace.databaseId)).thenReturn(samplePlace)


        val result = placesRepositoryImpl.getPlaceById(samplePlace.databaseId)


        assertEquals(samplePlace, result)
    }


}
