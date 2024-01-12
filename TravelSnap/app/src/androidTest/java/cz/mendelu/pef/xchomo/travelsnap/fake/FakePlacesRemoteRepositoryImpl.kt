package cz.mendelu.pef.xchomo.travelsnap.fake

import cz.mendelu.pef.xchomo.travelsnap.api.IPlacesRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.architecture.Error
import cz.mendelu.pef.xchomo.travelsnap.model.*
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors

import javax.inject.Inject

class FakePlacesRemoteRepositoryImpl @Inject constructor() : IPlacesRemoteRepository {
    override suspend fun searchNearbyPlaces(
        location: String,
        radius: Int
    ): CommunicationResult<PlacesResponse> {
        val mockPlace = createMockPlace()
        val mockResponse = PlacesResponse(
            htmlAttributions = emptyList(),
            results = listOf(mockPlace),
            status = "OK",
            candidates = listOf(mockPlace),
            result = mockPlace
        )
        return CommunicationResult.Success(mockResponse)
    }

    override suspend fun getPlaceDetails(placeId: String): CommunicationResult<PlacesResponse> {
        val mockPlace = createMockPlace()
        val mockResponse = PlacesResponse(
            htmlAttributions = emptyList(),
            results = listOf(mockPlace),
            status = "OK",
            candidates = listOf(mockPlace),
            result = mockPlace
        )
        return CommunicationResult.Success(mockResponse)
    }

    override suspend fun searchPlaceFromText(textQuery: String): CommunicationResult<PlacesResponse> {
        if(textQuery == "Brno") {
            val mockPlace = createMockPlace()
            val mockResponse = PlacesResponse(
                htmlAttributions = emptyList(),
                results = listOf(mockPlace),
                status = "OK",
                candidates = listOf(mockPlace),
                result = mockPlace
            )
            return CommunicationResult.Success(mockResponse)
        }else{
            val mockPlace = createMockPlace()
            val mockResponse = PlacesResponse(
                htmlAttributions = emptyList(),
                results = null,
                status = "OK",
                candidates = listOf(mockPlace),
                result = mockPlace
            )
            return CommunicationResult.Success(mockResponse)
        }
    }

    private fun createMockPlace(): Place {
        return Place(
            id = "mock_id",
            address = "Mock Address",
            name = "Mock Place",
            rating = 4.5,
            geometry = Geometry(Location(10.0, 10.0)),
            photos = listOf(Photo("mock_photo_reference")),
            selectedDate = "2024-01-01",
            description = "Mock Description",
            time = 1234567890L,
            selectedIconId = 1
        )
    }
}