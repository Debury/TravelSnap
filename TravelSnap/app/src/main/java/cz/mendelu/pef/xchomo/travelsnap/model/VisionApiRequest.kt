package cz.mendelu.pef.xchomo.travelsnap.model

import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenViewModel

data class VisionApiRequest(val image: VisionApiImage, var features: List<VisionApiFeature>)