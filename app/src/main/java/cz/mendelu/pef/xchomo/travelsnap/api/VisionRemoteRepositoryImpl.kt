package cz.mendelu.pef.xchomo.travelsnap.api

import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import cz.mendelu.pef.xchomo.travelsnap.model.VisionAPIResponse
import cz.mendelu.pef.xchomo.travelsnap.model.VisionApiRequestBody
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject

class VisionRemoteRepositoryImpl @Inject constructor(private val visionAPI: VisionAPI): IVisionRemoteRepository {
    override suspend fun detectLandMarks(request: VisionApiRequestBody): CommunicationResult<VisionAPIResponse> {
        return processResponse(withContext(Dispatchers.IO) {
            visionAPI.detectLandmarks(request)
        })
    }
}
