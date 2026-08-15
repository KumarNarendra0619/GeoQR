package com.geoqr.app.qr

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.geoqr.app.permission.GeoQrPermissions

@androidx.compose.runtime.Composable
fun GeoQrScannerScreen(
    onDetected: (String) -> Unit,
    onClose: () -> Unit,
    lifecycleOwner: LifecycleOwner
) {
    val context = LocalContext.current
    var granted by remember { mutableStateOf(GeoQrPermissions.cameraGranted(context)) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted = it }

    if (!granted) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                Text("Allow camera")
            }
        }
        return
    }

    val previewView = remember { PreviewView(context) }

    DisposableEffect(lifecycleOwner) {
        val controller = CameraScannerController(context, lifecycleOwner) {
            onDetected(it.rawValue)
        }
        controller.bind(previewView.surfaceProvider)
        onDispose { controller.close() }
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Button(
            onClick = onClose,
            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)
        ) { Text("Close") }
    }
}
