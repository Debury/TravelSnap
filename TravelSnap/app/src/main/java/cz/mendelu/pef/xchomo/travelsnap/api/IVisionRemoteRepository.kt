package cz.mendelu.pef.xchomo.travelsnap.api

import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.architecture.IBaseRemoteRepository
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import cz.mendelu.pef.xchomo.travelsnap.model.VisionAPIResponse
import cz.mendelu.pef.xchomo.travelsnap.model.VisionApiRequestBody
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel
import okhttp3.RequestBody
import javax.inject.Inject


interface IVisionRemoteRepository : IBaseRemoteRepository {
    suspend fun detectLandMarks(
        request: VisionApiRequestBody
    ): CommunicationResult<VisionAPIResponse>
}