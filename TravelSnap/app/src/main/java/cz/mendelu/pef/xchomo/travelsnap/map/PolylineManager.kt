package cz.mendelu.pef.xchomo.travelsnap.map

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class PolylineManager(private val googleMap: GoogleMap) {
    private val polylines: MutableList<Polyline> = mutableListOf()

    fun addPolyline(points: List<LatLng>, color: Int, width: Float): Polyline {
        val polylineOptions = PolylineOptions()
            .addAll(points)
            .color(color)
            .width(width)

        val polyline = googleMap.addPolyline(polylineOptions)
        polylines.add(polyline)
        return polyline
    }

    fun clearPolylines() {
        for (polyline in polylines) {
            polyline.remove()
        }
        polylines.clear()
    }
}