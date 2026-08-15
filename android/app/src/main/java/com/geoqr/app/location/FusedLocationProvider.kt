package com.geoqr.app.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FusedLocationProvider(context: Context) : LocationProvider {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun currentLocation(): GeoLocation? = suspendCancellableCoroutine { continuation ->
        client.lastLocation
            .addOnSuccessListener { location ->
                continuation.resume(location?.let {
                    GeoLocation(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        accuracyMeters = it.accuracy.toDouble()
                    )
                })
            }
            .addOnFailureListener { continuation.resume(null) }
    }
}
