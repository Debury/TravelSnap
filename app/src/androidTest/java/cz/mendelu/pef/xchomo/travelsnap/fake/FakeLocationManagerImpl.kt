package cz.mendelu.pef.xchomo.travelsnap.fake

import com.google.android.gms.maps.model.LatLng
import cz.mendelu.pef.xchomo.travelsnap.location.ILocationManager
import javax.inject.Inject



class FakeLocationManagerImpl @Inject constructor() : ILocationManager {

    var mockLocation: LatLng? = null
    var onLocationChanged: ((LatLng?) -> Unit)? = null

    override fun startLocationUpdates() {

        mockLocation = LatLng(49.195061, 16.606836)
        onLocationChanged?.invoke(mockLocation)
    }

    fun setMockLocation(latitude: Double, longitude: Double) {
        mockLocation = LatLng(latitude, longitude)
        onLocationChanged?.invoke(mockLocation)
    }

}
