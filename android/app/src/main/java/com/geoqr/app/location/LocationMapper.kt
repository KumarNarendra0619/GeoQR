package com.geoqr.app.location

import android.location.Location
import com.geoqr.app.model.GeoPoint

object LocationMapper {
    fun toGeoPoint(location: Location): GeoPoint = GeoPoint(
        latitude = location.latitude,
        longitude = location.longitude,
        accuracyMeters = location.accuracy.toDouble()
    )
}
