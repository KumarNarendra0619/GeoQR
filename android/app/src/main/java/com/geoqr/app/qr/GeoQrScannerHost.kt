package com.geoqr.app.qr

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner

/** Explicit lifecycle owner bridge for CameraX; avoids casting Compose Context. */
@Composable
fun GeoQrScannerHost(
    onDetected: (String) -> Unit,
    onClose: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    GeoQrScannerScreen(
        onDetected = onDetected,
        onClose = onClose,
        lifecycleOwner = lifecycleOwner
    )
}
