package cz.mendelu.pef.xchomo.travelsnap.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.mendelu.pef.xchomo.travelsnap.model.Geometry
import cz.mendelu.pef.xchomo.travelsnap.model.Location
import cz.mendelu.pef.xchomo.travelsnap.model.Photo

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromGeometry(geometry: Geometry?): String? {
        return gson.toJson(geometry)
    }

    @TypeConverter
    fun toGeometry(geometryString: String?): Geometry? {
        return gson.fromJson(geometryString, Geometry::class.java)
    }

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return gson.toJson(location)
    }

    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return gson.fromJson(locationString, Location::class.java)
    }

    @TypeConverter
    fun fromPhotoList(photos: List<Photo>?): String? {
        return gson.toJson(photos)
    }

    @TypeConverter
    fun toPhotoList(photosString: String?): List<Photo>? {
        val listType = object : TypeToken<List<Photo>>() {}.type
        return gson.fromJson(photosString, listType)
    }
}