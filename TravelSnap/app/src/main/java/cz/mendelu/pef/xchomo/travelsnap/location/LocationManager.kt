package cz.mendelu.pef.xchomo.travelsnap.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import cz.mendelu.pef.xchomo.travelsnap.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

class LocationManager @Inject constructor(@ApplicationContext private val  context: Context) : DefaultLifecycleObserver, ILocationManager {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    var onLocationChanged: ((LatLng?) -> Unit)? by mutableStateOf(null)

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient?.requestLocationUpdates(
                LocationRequest.create(),
                locationCallback,
                null
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                onLocationChanged?.invoke(LatLng(it.latitude, it.longitude))
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        startLocationUpdates()
    }

    override fun onStop(owner: LifecycleOwner) {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        fusedLocationClient = null
    }
}