package cz.mendelu.pef.xchomo.travelsnap.map

import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.compose.Polyline
import cz.mendelu.pef.xchomo.travelsnap.model.Place
import cz.mendelu.pef.xchomo.travelsnap.ui.theme.AppColors
import cz.mendelu.pef.xchomo.travelsnap.util.MarkerUtil

class CustomMapRenderer(
    private val context: Context,
    googleMap: GoogleMap,
    manager: ClusterManager<Place>? = null
): DefaultClusterRenderer<Place>(context, googleMap, manager)  {

    override fun onBeforeClusterItemRendered(item: Place, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getIcon(item)))
    }
    override fun shouldRenderAsCluster(cluster: Cluster<Place>): Boolean {
        return cluster.size > 2
    }

    fun addMarkersToMap(places: List<Place>, googleMap: GoogleMap, context: Context) {
        places.forEach { place ->
            val markerOptions = MarkerOptions()
                .position(LatLng(place.geometry?.location?.lat!!, place.geometry?.location?.lng!!))
                .icon(BitmapDescriptorFactory.fromBitmap(MarkerUtil.createBitmapMarker(context, place.selectedIconId!!)))
                .title(place.name)



            googleMap.addMarker(markerOptions)
        }
    }


    fun adjustCameraToPlaces(places: List<Place>, googleMap: GoogleMap) {
        val boundsBuilder = LatLngBounds.Builder()
        places.forEach { place ->
            boundsBuilder.include(LatLng(place.geometry?.location?.lat!!, place.geometry?.location?.lng!!))
        }

        val bounds = boundsBuilder.build()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    fun getIcon(place: Place): Bitmap {
        return MarkerUtil.createBitmapMarker(context, place.selectedIconId!!)
    }
}