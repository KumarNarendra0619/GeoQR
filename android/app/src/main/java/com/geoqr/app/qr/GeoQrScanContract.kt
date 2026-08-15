package com.geoqr.app.qr

/** Camera/decoder boundary. A concrete ML Kit or ZXing implementation plugs in here. */
interface GeoQrScanner {
    suspend fun scan(): String?
}

data class ScanResult(
    val rawValue: String,
    val isGeoQr: Boolean
)
