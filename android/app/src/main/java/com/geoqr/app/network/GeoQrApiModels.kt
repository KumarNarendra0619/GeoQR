package com.geoqr.app.network

import com.geoqr.app.model.CreateQrRequest
import com.geoqr.app.model.GeoPoint
import com.geoqr.app.model.ScanRequest

/** Transport DTOs kept separate from UI state. */
data class CreateQrDto(
    val type: String,
    val destination: String,
    val generationLocation: GeoPoint? = null,
    val expiresAt: String? = null
)

data class CreateQrResponseDto(
    val qrId: String,
    val token: String,
    val status: String
)

data class ResolveQrResponseDto(
    val qrId: String,
    val destination: String,
    val type: String,
    val active: Boolean
)

data class ScanDto(
    val sessionId: String,
    val locationConsent: Boolean,
    val location: GeoPoint? = null
)

fun CreateQrRequest.toDto() = CreateQrDto(type, destination, generationLocation, expiresAt)
fun ScanRequest.toDto() = ScanDto(sessionId, locationConsent, location)
