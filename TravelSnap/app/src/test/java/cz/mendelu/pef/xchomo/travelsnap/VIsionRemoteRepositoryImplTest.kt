package cz.mendelu.pef.xchomo.travelsnap

import cz.mendelu.pef.xchomo.travelsnap.api.VisionAPI
import cz.mendelu.pef.xchomo.travelsnap.api.VisionRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.architecture.Error
import cz.mendelu.pef.xchomo.travelsnap.model.*
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class VisionRemoteRepositoryImplTest {

    @Mock
    private  var mockVisionAPI = mockk<VisionAPI>()

    @Mock
    private var mockVisionApiImage: VisionApiImage = mockk()

    @Mock
    private var mockVisionApiFeature: VisionApiFeature = mockk()

    @Mock
    private var mockVisionApiRequestBody: VisionApiRequestBody = mockk()

    @Mock
    private var mockVisionApiRequest: VisionApiRequest = mockk()

    private lateinit var visionRemoteRepositoryImpl: VisionRemoteRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        visionRemoteRepositoryImpl = VisionRemoteRepositoryImpl(mockVisionAPI)
    }


    @Test
    fun detectLandMarks_ReturnsCommunicationResult() = runBlocking {

        mockVisionApiFeature.maxResults = 1
        mockVisionApiFeature.type = "LANDMARK_DETECTION"
        mockVisionApiImage.content = "BASE64_ENCODED_IMAGE"
        mockVisionApiRequest.features = listOf(mockVisionApiFeature)
        val request = VisionApiRequest(mockVisionApiImage, listOf(mockVisionApiFeature))
        val requestBody = VisionApiRequestBody(listOf(request))

        val landmarkAnnotation = LandmarkAnnotation("Eiffel Tower", listOf(LocationInfo(LatLng(48.8584, 2.2945))), 0.99f)
        val annotateImageResponse = AnnotateImageResponse(listOf(landmarkAnnotation))
        val visionApiResponse = VisionAPIResponse(listOf(annotateImageResponse))
        val apiResponse = Response.success(visionApiResponse)
        `when`(mockVisionAPI.detectLandmarks(requestBody)).thenReturn(apiResponse)


        assertEquals(CommunicationResult.Success(visionApiResponse).data, (visionRemoteRepositoryImpl.detectLandMarks(requestBody) as CommunicationResult.Success<VisionAPIResponse>).data)
    }

    @Test
    fun detectLandMarks_ReturnsCommunicationResultError() = runBlocking {
        mockVisionApiFeature.maxResults = 1
        mockVisionApiFeature.type = "LANDMARK_DETECTION"
        mockVisionApiImage.content = "BASE64_ENCODED_IMAGE"
        mockVisionApiRequest.features = listOf(mockVisionApiFeature)
        val request = VisionApiRequest(mockVisionApiImage, listOf(mockVisionApiFeature))
        val requestBody = VisionApiRequestBody(listOf(request))

        val landmarkAnnotation =
            LandmarkAnnotation("Eiffel Tower", listOf(LocationInfo(LatLng(48.8584, 2.2945))), 0.99f)
        val annotateImageResponse = AnnotateImageResponse(listOf(landmarkAnnotation))
        val visionApiResponse = VisionAPIResponse(listOf(annotateImageResponse))
        val apiResponse = Response.error<VisionAPIResponse>(404, "Not Found".toResponseBody())
        val expectedError = CommunicationResult.Error(Error(404, "Not Found"))

        `when`(mockVisionAPI.detectLandmarks(requestBody)).thenReturn(apiResponse)
        val result = visionRemoteRepositoryImpl.detectLandMarks(requestBody)
        assertEquals(expectedError.error.code, (result as CommunicationResult.Error).error.code)
    }
}