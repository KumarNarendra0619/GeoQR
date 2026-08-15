package com.geoqr.app.core

import com.geoqr.app.location.GeoLocation
import com.geoqr.app.location.LocationConsent
import com.geoqr.app.model.GeoQrPayload

sealed interface GeoQrSessionState {
    data object Idle : GeoQrSessionState
    data class Scanned(val payload: GeoQrPayload) : GeoQrSessionState
    data class Resolved(val destination: String) : GeoQrSessionState
    data class AwaitingLocationConsent(val destination: String) : GeoQrSessionState
    data class LocationGranted(val destination: String, val location: GeoLocation) : GeoQrSessionState
    data class LocationDenied(val destination: String) : GeoQrSessionState
    data class Error(val message: String) : GeoQrSessionState
}

class GeoQrSessionController {
    var state: GeoQrSessionState = GeoQrSessionState.Idle
        private set

    fun acceptScan(rawValue: String): Boolean {
        val payload = GeoQrParser.parse(rawValue) ?: run {
            state = GeoQrSessionState.Error("Invalid GeoQR payload")
            return false
        }
        state = GeoQrSessionState.Scanned(payload)
        return true
    }

    fun resolved(destination: String) {
        state = GeoQrSessionState.AwaitingLocationConsent(destination)
    }

    fun applyConsent(consent: LocationConsent, location: GeoLocation? = null) {
        val destination = when (val current = state) {
            is GeoQrSessionState.AwaitingLocationConsent -> current.destination
            else -> return
        }
        state = when (consent) {
            LocationConsent.Granted -> if (location != null && location.isValid()) {
                GeoQrSessionState.LocationGranted(destination, location)
            } else {
                GeoQrSessionState.Error("A valid location is required after consent")
            }
            LocationConsent.Denied -> GeoQrSessionState.LocationDenied(destination)
            LocationConsent.NotAsked -> GeoQrSessionState.AwaitingLocationConsent(destination)
        }
    }
}

private fun GeoLocation.isValid(): Boolean =
    latitude in -90.0..90.0 && longitude in -180.0..180.0
