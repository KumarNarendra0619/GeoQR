package com.geoqr.app.qr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Camera permission gate for the scanner layer.
 * A CameraX/ML Kit decoder can be injected behind GeoQrScanner.
 */
class GeoQrCameraScanner(private val context: Context) : GeoQrScanner {
    fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED

    override suspend fun scan(): String? {
        check(hasCameraPermission()) { "Camera permission is required" }
        // Decoder integration is intentionally injected at the UI/camera layer.
        return null
    }
}
