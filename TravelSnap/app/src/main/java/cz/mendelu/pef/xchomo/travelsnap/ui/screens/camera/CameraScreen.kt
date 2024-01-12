package cz.mendelu.pef.xchomo.travelsnap.ui.screens.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BaseScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomBottomAppBar

import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import cz.mendelu.pef.xchomo.travelsnap.CameraXLivePreviewActivity
import cz.mendelu.pef.xchomo.travelsnap.datastore.BitmapDataStore
import cz.mendelu.pef.xchomo.travelsnap.datastore.DateDataStore
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddPlaceScreenDestination
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Destination
@Composable
fun CameraScreen(
    navigator: DestinationsNavigator
) {

    BaseScreen(topBarText = null, drawFullScreenContent = true, showLoading = false, placeholderScreenContent = null, bottomAppBar = { CustomBottomAppBar(navigator,"camera") }){
        CameraScreenContent(navigator)
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreenContent(
    navigator: DestinationsNavigator,
){

    val context = LocalContext.current

    val startCameraActivity = {
        val intent = Intent(context, CameraXLivePreviewActivity::class.java)
        context.startActivity(intent)
    }
    val viewModel: CameraScreenViewModel = hiltViewModel()



    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.QUERY_ALL_PACKAGES,

        )
    )

    val applicationContext = LocalContext.current.applicationContext
    val bitmap = BitmapDataStore.getSelectedBitmap(applicationContext)
        .collectAsState(initial = null)




    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            if (permissions.all { it.value }) {
                startCameraActivity()
            } else {
                Log.d("CameraScreenContent", "Permissions not granted")
            }
        }

    LaunchedEffect(multiplePermissionsState) {
        if (!multiplePermissionsState.allPermissionsGranted) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.QUERY_ALL_PACKAGES,


                )
            )
        }
    }

    Column {
        if (multiplePermissionsState.allPermissionsGranted) {
            startCameraActivity()
        }
    }

}


