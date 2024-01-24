package cz.mendelu.pef.xchomo.travelsnap.ui.screens.map

import android.view.View
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cz.mendelu.pef.xchomo.travelsnap.architecture.BaseViewModel
import cz.mendelu.pef.xchomo.travelsnap.model.LatLng
import cz.mendelu.pef.xchomo.travelsnap.model.Location
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.model.PolygonAndPolyline
import cz.mendelu.pef.xchomo.travelsnap.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MapScreenViewModel @Inject constructor(): BaseViewModel() {
    val polygonAndPolylinesUIState: MutableState<UIState<List<PolygonAndPolyline>,
            PolygonAndPolylinesErrors>> = mutableStateOf(
        UIState(loading = false)
    )

    fun createPolygonAndPolyline(places: Array<Place>): List<LatLng> {
        //sort places by time
        places.sortBy { it.time }
        val locationArray: List<Location> = places.map { Location(it.geometry?.location?.lat, it.geometry?.location?.lng) }
        val polygonAndPolyline = PolygonAndPolyline(locationArray)
        return polygonAndPolyline.getLatLng()
    }

    fun calculateMidpoint(start: com.google.android.gms.maps.model.LatLng, end: com.google.android.gms.maps.model.LatLng): com.google.android.gms.maps.model.LatLng {
        val midLat = (start.latitude + end.latitude) / 2
        val midLng = (start.longitude + end.longitude) / 2
        return com.google.android.gms.maps.model.LatLng(midLat, midLng)
    }

    fun calculateBearing(start: com.google.android.gms.maps.model.LatLng, end: com.google.android.gms.maps.model.LatLng): Double {
        val lat1 = Math.toRadians(start.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon2 = Math.toRadians(end.longitude)

        val dLon = lon2 - lon1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)
        val bearing = Math.toDegrees(Math.atan2(y, x))

        return (bearing + 360) % 360
    }
}