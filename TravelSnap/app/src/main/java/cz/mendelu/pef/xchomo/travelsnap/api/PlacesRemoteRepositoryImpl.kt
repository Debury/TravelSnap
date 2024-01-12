package cz.mendelu.pef.xchomo.travelsnap.api

import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesRemoteRepositoryImpl@Inject constructor(private val placeAPI: PlacesAPI): IPlacesRemoteRepository {
    override suspend fun searchNearbyPlaces(
        location: String,
        radius: Int,
    ): CommunicationResult<PlacesResponse> {
        return processResponse(withContext(Dispatchers.IO) {
            placeAPI.searchNearbyPlaces(location, radius)
        })
    }

    override suspend fun getPlaceDetails(
        placeId: String): CommunicationResult<PlacesResponse> {
        return processResponse(withContext(Dispatchers.IO) {
            placeAPI.getPlaceDetails(placeId)
        })
    }

    override suspend fun searchPlaceFromText(
        textQuery: String,
    ): CommunicationResult<PlacesResponse> {
        return processResponse(withContext(Dispatchers.IO) {
            placeAPI.searchText(textQuery)
        })

    }


}