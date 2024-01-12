package cz.mendelu.pef.xchomo.travelsnap.api

import cz.mendelu.pef.xchomo.travelsnap.di.VisionApiRetrofit
import cz.mendelu.pef.xchomo.travelsnap.model.VisionAPIResponse;
import cz.mendelu.pef.xchomo.travelsnap.model.VisionApiRequestBody
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


interface VisionAPI {

    @VisionApiRetrofit
    @Headers("Content-Type: application/json")
    @POST("v1/images:annotate")
    suspend fun detectLandmarks(
        @Body request: VisionApiRequestBody
    ): Response<VisionAPIResponse>;

}