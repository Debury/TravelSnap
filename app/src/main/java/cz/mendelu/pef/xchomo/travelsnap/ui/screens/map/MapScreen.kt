package cz.mendelu.pef.xchomo.travelsnap.ui.screens.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.GridBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.mendelu.pef.xchomo.travelsnap.R
import cz.mendelu.pef.xchomo.travelsnap.map.CustomMapRenderer
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.BaseScreen
import cz.mendelu.pef.xchomo.travelsnap.ui.elements.CustomBottomAppBar
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.destinations.AddEditScreenPlaceDestination
import cz.mendelu.pef.xchomo.travelsnap.ui.screens.mainScreen.MainScreenContent
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.util.MarkerUtil
import cz.mendelu.pef.xchomo.travelsnap.util.MarkerUtil.Companion.createBitmapMarker
import cz.mendelu.pef.xchomo.travelsnap.util.MarkerUtil.Companion.createMarkerIconFromResource


@Destination
@Composable
fun MapScreen(
    placesArray: Array<Place>?,
    navigator: DestinationsNavigator,
    displayPolyline: Boolean = false

) {

    BaseScreen(
        topBarText = null,
        drawFullScreenContent = true,
        showLoading = false,
        placeholderScreenContent = null,
        bottomAppBar = { CustomBottomAppBar(navigator, "map") }) {
        MapScreenContent(navigator, it, placesArray, displayPolyline)
    }
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreenContent(
    navigator: DestinationsNavigator,
    paddingValues: PaddingValues,
    placesArray: Array<Place>?,
    displayPolyline: Boolean
) {

    val viewModel = hiltViewModel<MapScreenViewModel>()
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false
            )
        )
    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(49.192244, 16.611338), 10f)
    }

    val context = LocalContext.current
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    val googlePolylineCoordinates = if (placesArray != null) {
        viewModel.createPolygonAndPolyline(placesArray).map {
            LatLng(it.latitude!!, it.longitude!!)
        }
    } else {
        listOf()
    }


    var places: List<Place> = mutableListOf()
    if (placesArray != null) {
        places = placesArray.toList()
    }
    var manager by remember { mutableStateOf<ClusterManager<Place>?>(null) }


    if(placesArray != null) {
        manager?.addItems(places)
        manager?.cluster()
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxHeight(),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ) {
            if (placesArray != null) {
                MapEffect(places) {
                    if (displayPolyline) {
                        val customMapRenderer = CustomMapRenderer(context, it)
                        customMapRenderer.addMarkersToMap(places, it, context)

                        customMapRenderer.adjustCameraToPlaces(places, it)
                    } else {
                        manager = ClusterManager<Place>(context, it)

                        manager?.apply {
                            algorithm = GridBasedAlgorithm()
                            renderer = CustomMapRenderer(context, it, manager)
                        }

                        manager?.addItems(places)

                        it.setOnCameraIdleListener {
                            manager?.cluster()
                        }


                    }
                }
                if (displayPolyline) {
                    if (googlePolylineCoordinates.isNotEmpty()) {
                        Polyline(
                            points = googlePolylineCoordinates,
                            color = AppColors.PrimaryColor,
                            width = 5f,
                        )
                    }

                    for (i in 0 until googlePolylineCoordinates.size - 1) {
                        val start = googlePolylineCoordinates[i]
                        val end = googlePolylineCoordinates[i + 1]
                        val midpoint = viewModel.calculateMidpoint(start, end)
                        val rotationAngle = viewModel.calculateBearing(start, end)



                        Marker(
                            state = MarkerState(position = midpoint),
                            icon = BitmapDescriptorFactory.fromBitmap(
                                createBitmapMarker(
                                    context,
                                    R.drawable.reshot_icon_arrow_upward_v2xfr4eu7z
                                )
                            ),
                            rotation = rotationAngle.toFloat(),
                            flat = true
                        )
                    }
                }
            }
        }
    }
}
