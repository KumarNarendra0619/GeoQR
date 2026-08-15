package com.geoqr.app.qr

/** Immutable result emitted by the camera decoder. */
data class GeoQrScanFrame(
    val rawValue: String,
    val timestampMillis: Long = System.currentTimeMillis()
)

object GeoQrScanValidator {
    fun accept(frame: GeoQrScanFrame): String? =
        if (frame.rawValue.startsWith("GQR://v1/")) frame.rawValue else null
}
