package cz.mendelu.pef.xchomo.travelsnap.ui.screens.camera


import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nmd.screenshot.Screenshot
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.api.VisionRemoteRepositoryImpl
import cz.mendelu.pef.xchomo.travelsnap.architecture.BaseViewModel
import cz.mendelu.pef.xchomo.travelsnap.architecture.CommunicationResult
import cz.mendelu.pef.xchomo.travelsnap.datastore.BitmapDataStore
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PlacesResponse
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import cz.mendelu.pef.xchomo.travelsnap.model.VisionAPIResponse
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.add.AddPlaceErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import javax.inject.Inject



@HiltViewModel
class CameraScreenViewModel @Inject constructor(
    private val repository: VisionRemoteRepositoryImpl,
): ViewModel() {

    var landmarkListened: MutableState<Boolean> = mutableStateOf(false)
    var visionUIState: MutableState<UIState<VisionAPIResponse, AddPlaceErrors>>
            = mutableStateOf(UIState())
    var isLandmark: MutableState<Boolean> = mutableStateOf(false)

    var landmarkDetectionListener: LandmarkDetectionListener? = null
    var contentBitMap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)

    val landmarkLabels = setOf(
        "Arch", "Archaeological Site", "Architecture", "Arena", "Bridge",
        "Castle", "Cathedral", "Church", "City",
        "City Park", "Courthouse", "Dam", "Dome", "Fountain",
        "Garden", "Government Building", "Historical Site", "House", "Landmark",
        "Library", "Lighthouse", "Memorial", "Monastery", "Monument",
        "Mosque", "Museum", "Palace", "Park", "Pavilion",
        "Plaza", "Prison", "Religious Site", "Resort", "Ruins",
        "Sculpture", "Skyscraper", "Stadium", "Statue", "Temple",
        "Theatre", "Tower", "Town Square", "Train Station", "University",
        "Zoo", "Building"
    )



    fun processLandmarkLabels(labels: List<String>) {
        val topLabels = labels.take(5)
        val landmarkCount = topLabels.count { landmarkLabels.contains(it) }

        val isLandmark = landmarkCount >= 2
        this.isLandmark.value = isLandmark
        if (isLandmark) {
            landmarkDetectionListener?.onLandmarkDetected()
        }
    }



    fun printEverything(){
        Log.d("CameraScreenViewModel", "printEverything: ${contentBitMap.value?.byteCount}")
    }



    

}

interface LandmarkDetectionListener {
    fun onLandmarkDetected()
}