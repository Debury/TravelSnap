package cz.mendelu.pef.xchomo.travelsnap.api


import cz.mendelu.pef.xchomo.travelsnap.di.PlacesApiRetrofit
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface PlacesAPI {

    @GET("nearbysearch/json")
    suspend fun searchNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("fields") fields: String = "formatted_address,name,geometry,photos,place_id,rating",
    ): Response<PlacesResponse>



    @GET("findplacefromtext/json")
    suspend fun searchText(
        @Query("input") textQuery: String,
        @Query("fields") fields: String = "formatted_address,name,geometry,photos,place_id,rating",
        @Query("inputtype") inputType: String = "textquery",
    ): Response<PlacesResponse>


    @GET("details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = "formatted_address,name,geometry,photos,place_id,rating",
    ): Response<PlacesResponse>



}