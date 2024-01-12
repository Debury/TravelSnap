package cz.mendelu.pef.xchomo.travelsnap.model



import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class PlacesResponse(
    @Json(name = "html_attributions") val htmlAttributions: List<String>?,
    val results: List<Place>?,
    val status: String?,
    val candidates: List<Place>?,
    val result: Place?
): Serializable

@JsonClass(generateAdapter = true)
@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Long = 0,
    @Json(name = "place_id")
    val id: String,
    @Json(name = "formatted_address") val address: String?,
    var name: String?,
    val rating: Double?,
    val geometry: Geometry?,
    val photos: List<Photo>?,
    var selectedDate: String?,
    var description: String?,
    var time: Long?,
    var selectedIconId: Int?,
): Serializable, ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(geometry?.location?.lat!!, geometry.location.lng!!)
    }

    override fun getTitle(): String? {
        return name.toString() //ta 0 nebo 1 u bodu
    }

    override fun getSnippet(): String? {
        return name.toString()
    }

    override fun getZIndex(): Float? {
        return 0.0f
    }


}

data class Geometry(
    val location: Location
): Serializable

data class Location(
    val lat: Double?,
    val lng: Double?
): Serializable

data class Photo(
    @Json(name = "photo_reference") val photoReference: String
): Serializable

