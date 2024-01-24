package cz.mendelu.pef.xchomo.travelsnap.model


import java.io.Serializable

data class PolygonAndPolyline(var location: List<Location>) : Serializable {

    var id: Long? = null

    var type: Int? = null

    fun getLatLng(): List<LatLng>{
        val list: MutableList<LatLng> = mutableListOf()
        location.forEach {
            list.add(LatLng(it.lat, it.lng))
        }
        return list.toList()
    }

}
