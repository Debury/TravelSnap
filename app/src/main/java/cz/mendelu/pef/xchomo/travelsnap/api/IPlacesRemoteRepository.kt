package cz.mendelu.pef.xchomo.travelsnap.api

import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.architecture.IBaseRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import retrofit2.Response
import retrofit2.http.Query


interface IPlacesRemoteRepository : IBaseRemoteRepository {

    suspend fun searchNearbyPlaces(
        location: String,
        radius: Int,
    ): CommunicationResult<PlacesResponse>

    suspend fun getPlaceDetails(
        placeId: String
    ): CommunicationResult<PlacesResponse>

    suspend fun searchPlaceFromText(
        textQuery: String,
    ): CommunicationResult<PlacesResponse>



}