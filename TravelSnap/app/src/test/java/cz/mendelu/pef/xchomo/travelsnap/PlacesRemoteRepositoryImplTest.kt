package cz.mendelu.pef.xchomo.travelsnap

import cz.mendelu.pef.xchomo.travelsnap.api.PlacesAPI
import cz.mendelu.pef.xchomo.travelsnap.api.PlacesRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.architecture.Error
import cz.mendelu.pef.xchomo.travelsnap.mock.CommunicationResultMockFactory
import cz.mendelu.pef.xchomo.travelsnap.model.Geometry
import cz.mendelu.pef.xchomo.travelsnap.model.Location
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response


class PlacesRemoteRepositoryImplTest {

    @Mock
    private lateinit var mockPlacesAPI: PlacesAPI

    private lateinit var placesRemoteRepositoryImpl: PlacesRemoteRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        placesRemoteRepositoryImpl = PlacesRemoteRepositoryImpl(mockPlacesAPI)
    }

    @Test
    fun searchNearbyPlaces_ReturnsCommunicationResult() = runBlocking {
        // Create a sample API response using Response.success
        val apiResponse = Response.success(
            PlacesResponse(
                htmlAttributions = emptyList(), results = listOf(
                    Place(
                        id = "1",
                        address = "Address 1",
                        name = "Place 1",
                        rating = 4.5,
                        geometry = Geometry(Location(1.0, 2.0)),
                        photos = emptyList(),
                        selectedDate = "",
                        description = "",
                        time = 0,
                        selectedIconId = 0
                    )
                ), status = "OK", candidates = emptyList(), result = null
            )
        )

        `when`(mockPlacesAPI.searchNearbyPlaces("1.0,2.0", 1000)).thenReturn(apiResponse)
        val result = placesRemoteRepositoryImpl.searchNearbyPlaces(location = "1.0,2.0", radius = 1000)
        val expectedSuccessResult = CommunicationResult.Success(apiResponse.body()!!)
        assertEquals(expectedSuccessResult.data, (result as CommunicationResult.Success).data)


    }

    @Test
    fun getPlaceDetails_ReturnsCommunicationResult() = runBlocking {
        // Create a sample API response using Response.success
        val apiResponse = Response.success(
            PlacesResponse(
                htmlAttributions = emptyList(), results = listOf(
                    Place(
                        id = "2",
                        address = "Address 2",
                        name = "Place 2",
                        rating = 4.3,
                        geometry = Geometry(Location(3.0, 4.0)),
                        photos = emptyList(),
                        selectedDate = "",
                        description = "",
                        time = 0,
                        selectedIconId = 0
                    )
                ), status = "OK", candidates = emptyList(), result = null
            )
        )

        `when`(mockPlacesAPI.getPlaceDetails("2")).thenReturn(apiResponse)
        val result = placesRemoteRepositoryImpl.getPlaceDetails(placeId = "2")
        val expectedSuccessResult = CommunicationResult.Success(apiResponse.body()!!)
        assertEquals(expectedSuccessResult.data, (result as CommunicationResult.Success).data)
    }

    @Test
    fun searchPlaceFromText_ReturnsCommunicationResult() = runBlocking {
        // Create a sample API response using Response.success
        val apiResponse = Response.success(
            PlacesResponse(
                htmlAttributions = emptyList(), results = listOf(
                    Place(
                        id = "3",
                        address = "Address 3",
                        name = "Place 3",
                        rating = 4.0,
                        geometry = Geometry(Location(5.0, 6.0)),
                        photos = emptyList(),
                        selectedDate = "",
                        description = "",
                        time = 0,
                        selectedIconId = 0
                    )
                ), status = "OK", candidates = emptyList(), result = null
            )
        )


        `when`(mockPlacesAPI.searchText("Place 3")).thenReturn(apiResponse)
        val result = placesRemoteRepositoryImpl.searchPlaceFromText(textQuery = "Place 3")
        val expectedSuccessResult = CommunicationResult.Success(apiResponse.body()!!)
        assertEquals(expectedSuccessResult.data, (result as CommunicationResult.Success).data)
    }

    @Test
    fun searchNearbyPlaces_ReturnsCommunicationError() = runBlocking {
        val errorBody = "{\"message\": \"Not Found\"}"
        val apiResponse = Response.error<PlacesResponse>(404, errorBody.toResponseBody())


        `when`(mockPlacesAPI.searchNearbyPlaces("1.0,2.0", 1000)).thenReturn(apiResponse)
        val result = placesRemoteRepositoryImpl.searchNearbyPlaces(location = "1.0,2.0", radius = 1000)
        val expectedErrorResult = CommunicationResult.Error(cz.mendelu.pef.xchomo.travelsnap.architecture.Error(404, "Not Found"))
        assertEquals(expectedErrorResult.error.code, (result as CommunicationResult.Error).error.code)

    }

    @Test
    fun getPlaceDetails_ReturnsCommunicationError() = runBlocking {
        val errorBody = "{\"message\": \"Not Found\"}"
        val apiResponse = Response.error<PlacesResponse>(404, errorBody.toResponseBody())

        `when`(mockPlacesAPI.getPlaceDetails("2")).thenReturn(apiResponse)
        val result = placesRemoteRepositoryImpl.getPlaceDetails(placeId = "2")
        val expectedErrorResult = CommunicationResult.Error(
            cz.mendelu.pef.xchomo.travelsnap.architecture.Error(
                404,
                "Not Found"
            )
        )
        assertEquals(
            expectedErrorResult.error.code,
            (result as CommunicationResult.Error).error.code
        )

    }


}
