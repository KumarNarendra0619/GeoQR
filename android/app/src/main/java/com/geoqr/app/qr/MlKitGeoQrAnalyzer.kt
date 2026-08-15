package com.geoqr.app.qr

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class MlKitGeoQrAnalyzer(
    private val onGeoQr: (GeoQrScanFrame) -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage == null) {
            image.close()
            return
        }

        val input = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
        scanner.process(input)
            .addOnSuccessListener { barcodes ->
                barcodes.asSequence()
                    .mapNotNull { it.rawValue }
                    .firstOrNull { it.startsWith("GQR://v1/") }
                    ?.let { onGeoQr(GeoQrScanFrame(it)) }
            }
            .addOnCompleteListener { image.close() }
    }
}
