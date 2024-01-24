package cz.mendelu.pef.xchomo.travelsnap.ui.screens.addedit

sealed class ImageLoadState {
    object Loading : ImageLoadState()
    object Success : ImageLoadState()
    object Error : ImageLoadState()
}