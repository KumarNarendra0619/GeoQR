package com.geoqr.app.model

/** Shared transport models for the Android MVP. */
data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double? = null
)

data class CreateQrRequest(
    val type: String,
    val destination: String,
    val generationLocation: GeoPoint? = null,
    val expiresAt: String? = null
)

data class ScanRequest(
    val sessionId: String,
    val locationConsent: Boolean,
    val location: GeoPoint? = null
)

data class GeoQrPayload(
    val qrId: String,
    val token: String
)
