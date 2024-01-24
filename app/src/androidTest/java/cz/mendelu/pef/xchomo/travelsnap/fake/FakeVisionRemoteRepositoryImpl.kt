package cz.mendelu.pef.xchomo.travelsnap.fake

import cz.mendelu.pef.xchomo.travelsnap.api.IVisionRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.model.*
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import javax.inject.Inject

class FakeVisionRemoteRepositoryImpl @Inject constructor(): IVisionRemoteRepository {
    override suspend fun detectLandMarks(request: VisionApiRequestBody): CommunicationResult<VisionAPIResponse> {
        val mockResponse = createMockVisionAPIResponse()
        return CommunicationResult.Success(mockResponse)
    }

    private fun createMockVisionAPIResponse(): VisionAPIResponse {
        val mockLandmarkAnnotation = LandmarkAnnotation(
            description = "Mock Landmark",
            locations = listOf(
                LocationInfo(
                    latLng = LatLng(
                        latitude = 10.0,
                        longitude = 10.0
                    )
                )
            ),
            score = 0.99f
        )
        val mockAnnotateImageResponse = AnnotateImageResponse(
            landmarkAnnotations = listOf(mockLandmarkAnnotation)
        )
        return VisionAPIResponse(
            responses = listOf(mockAnnotateImageResponse)
        )
    }
}