package com.geolens.camera

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as UiColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.geolens.camera.data.CaptureRecord
import com.geolens.camera.data.GeoLensDatabase
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.Executor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GeoLensApp() }
    }
}

@Composable
private fun GeoLensApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { GeoLensDatabase.get(context) }
    val fused = remember { LocationServices.getFusedLocationProviderClient(context) }
    val executor = remember { ContextCompat.getMainExecutor(context) }

    var cameraGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    var locationGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var status by remember { mutableStateOf("READY") }
    var accuracy by remember { mutableStateOf<Float?>(null) }
    var locationText by remember { mutableStateOf("Location acquiring…") }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var captureId by remember { mutableStateOf(newCaptureId()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        cameraGranted = result[Manifest.permission.CAMERA] == true || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        locationGranted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(Unit) {
        if (!cameraGranted || !locationGranted) {
            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    LaunchedEffect(locationGranted) {
        if (locationGranted) {
            fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        accuracy = loc.accuracy
                        locationText = "%.6f, %.6f".format(Locale.US, loc.latitude, loc.longitude)
                    } else locationText = "Location unavailable"
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = UiColor.Black) {
        if (!cameraGranted) {
            PermissionGate { permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)) }
        } else {
            CameraScreen(
                executor = executor,
                onImageCaptureReady = { imageCapture = it },
                captureId = captureId,
                locationText = locationText,
                accuracy = accuracy,
                qrBitmap = qrBitmap,
                status = status,
                onCapture = {
                    val currentCaptureId = captureId
                    status = "CAPTURING"
                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, "GEOLENS_$currentCaptureId.jpg")
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GeoLens")
                    }
                    val output = ImageCapture.OutputFileOptions.Builder(
                        context.contentResolver,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    ).build()

                    imageCapture?.takePicture(output, executor, object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exception: ImageCaptureException) { status = "CAPTURE FAILED" }

                        override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                            val uri = result.savedUri ?: return
                            val now = System.currentTimeMillis()
                            val save = { loc: android.location.Location? ->
                                val record = CaptureRecord(
                                    captureId = currentCaptureId,
                                    uri = uri.toString(),
                                    latitude = loc?.latitude,
                                    longitude = loc?.longitude,
                                    accuracyMeters = loc?.accuracy,
                                    altitudeMeters = loc?.altitude,
                                    headingDegrees = if (loc?.hasBearing() == true) loc.bearing else null,
                                    capturedAtEpochMs = now,
                                    locationStatus = if (loc != null) "RECORDED" else "UNAVAILABLE",
                                    qrReference = currentCaptureId
                                )
                                scope.launch(Dispatchers.IO) { db.captureDao().insert(record) }
                                accuracy = loc?.accuracy
                                locationText = loc?.let { "%.6f, %.6f".format(Locale.US, it.latitude, it.longitude) } ?: "Location unavailable"
                                qrBitmap = generateQr("geolens://verify/$currentCaptureId")
                                captureId = newCaptureId()
                                status = "SAVED"
                            }
                            if (locationGranted) {
                                fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                                    .addOnSuccessListener { save(it) }
                                    .addOnFailureListener { save(null) }
                            } else save(null)
                        }
                    })
                }
            )
        }
    }
}

@Composable
private fun PermissionGate(onGrant: () -> Unit) {
    Box(Modifier.fillMaxSize().background(UiColor.Black), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text("GeoLens", color = UiColor.White, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(12.dp))
            Text("Professional camera with location intelligence.", color = UiColor.LightGray)
            Spacer(Modifier.height(20.dp))
            Button(onClick = onGrant) { Text("Grant Camera & Location") }
        }
    }
}

@Composable
private fun CameraScreen(
    executor: Executor,
    onImageCaptureReady: (ImageCapture) -> Unit,
    captureId: String,
    locationText: String,
    accuracy: Float?,
    qrBitmap: Bitmap?,
    status: String,
    onCapture: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    Box(Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).also { previewView ->
                    val future = ProcessCameraProvider.getInstance(ctx)
                    future.addListener({
                        val provider = future.get()
                        val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }
                        val capture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
                        try {
                            provider.unbindAll()
                            provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, capture)
                            onImageCaptureReady(capture)
                        } catch (_: Exception) { }
                    }, executor)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("GPS ${accuracy?.let { "%.1f m".format(Locale.US, it) } ?: "—"}", color = UiColor.White,
                    modifier = Modifier.background(UiColor.Black.copy(alpha = .55f), RoundedCornerShape(14.dp)).padding(10.dp))
                Text(status, color = UiColor.White,
                    modifier = Modifier.background(UiColor.Black.copy(alpha = .55f), RoundedCornerShape(14.dp)).padding(10.dp))
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Text(locationText, color = UiColor.White, maxLines = 1)
                    Text(captureId, color = UiColor.White.copy(alpha = .8f), style = MaterialTheme.typography.labelSmall)
                }
                if (qrBitmap != null) {
                    Image(bitmap = qrBitmap.asImageBitmap(), contentDescription = "GeoQR", modifier = Modifier.size(72.dp).background(UiColor.White))
                }
                Spacer(Modifier.size(12.dp))
                Box(Modifier.size(76.dp).background(UiColor.White, CircleShape), contentAlignment = Alignment.Center) {
                    Button(onClick = onCapture, shape = CircleShape, modifier = Modifier.size(64.dp)) { }
                }
            }
        }
    }
}

private fun newCaptureId(): String = "GL-${SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())}-${UUID.randomUUID().toString().take(6).uppercase(Locale.US)}"

private fun generateQr(text: String): Bitmap? = try {
    val matrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 256, 256, mapOf(EncodeHintType.MARGIN to 1))
    Bitmap.createBitmap(matrix.width, matrix.height, Bitmap.Config.ARGB_8888).also { bitmap ->
        for (x in 0 until matrix.width) for (y in 0 until matrix.height) bitmap.setPixel(x, y, if (matrix[x, y]) Color.BLACK else Color.WHITE)
    }
} catch (_: Exception) { null }
