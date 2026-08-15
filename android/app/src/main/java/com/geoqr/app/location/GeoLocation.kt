package com.geoqr.app.location

data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double? = null,
    val timestampMillis: Long = System.currentTimeMillis()
)

object GeoLocationValidator {
    fun isValid(location: GeoLocation): Boolean =
        location.latitude in -90.0..90.0 && location.longitude in -180.0..180.0
}
