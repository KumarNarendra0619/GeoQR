package com.geoqr.app.location

/** Runtime location boundary. Implement with FusedLocationProviderClient after permission is granted. */
interface LocationProvider {
    suspend fun currentLocation(): GeoLocation?
}
