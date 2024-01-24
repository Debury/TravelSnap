package cz.mendelu.pef.xchomo.travelsnap.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VisionAPIResponse(
    val responses: List<AnnotateImageResponse>?
)

@JsonClass(generateAdapter = true)
data class AnnotateImageResponse(
    val landmarkAnnotations: List<LandmarkAnnotation>?
)

@JsonClass(generateAdapter = true)
data class LandmarkAnnotation(
    val description: String?,
    val locations: List<LocationInfo>?,
    val score: Float?
)

@JsonClass(generateAdapter = true)
data class LocationInfo(
    val latLng: LatLng?
)

@JsonClass(generateAdapter = true)
data class LatLng(
    val latitude: Double?,
    val longitude: Double?
)