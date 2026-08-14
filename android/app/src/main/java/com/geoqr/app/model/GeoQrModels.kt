package com.geoqr.app.model

data class GeoQrPayload(
    val version: Int = 1,
    val qrId: String,
    val token: String
)

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
