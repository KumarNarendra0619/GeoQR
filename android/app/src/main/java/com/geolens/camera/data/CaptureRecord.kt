package com.geolens.camera.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captures")
data class CaptureRecord(
    @PrimaryKey val captureId: String,
    val uri: String,
    val mediaType: String = "PHOTO",
    val latitude: Double?,
    val longitude: Double?,
    val accuracyMeters: Float?,
    val altitudeMeters: Double?,
    val headingDegrees: Float?,
    val capturedAtEpochMs: Long,
    val placeName: String? = null,
    val locationStatus: String,
    val qrReference: String
)
