package com.geoqr.app.qr

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import android.content.Context
import android.view.Surface
import android.view.SurfaceHolder
import java.util.concurrent.Executors

/** Binds CameraX preview + analysis. UI owns the PreviewView surface. */
class CameraScannerController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val onGeoQr: (GeoQrScanFrame) -> Unit
) {
    private val executor = Executors.newSingleThreadExecutor()

    fun bind(previewSurfaceProvider: Preview.SurfaceProvider) {
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener({
            val provider = future.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewSurfaceProvider)
            }
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { it.setAnalyzer(executor, MlKitGeoQrAnalyzer(onGeoQr)) }
            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analysis
            )
        }, ContextCompat.getMainExecutor(context))
    }

    fun close() {
        executor.shutdown()
    }
}
