package com.geoqr.app.create

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.geoqr.app.qr.GeoQrGenerator

@Composable
fun GeoQrResultScreen(
    payload: String,
    onShare: (Bitmap) -> Unit,
    onDone: () -> Unit
) {
    val bitmap = GeoQrGenerator.generate(payload)
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("GeoQR created")
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Generated GeoQR")
        Text(payload)
        Button(onClick = { onShare(bitmap) }) { Text("Share QR") }
        Button(onClick = onDone) { Text("Done") }
    }
}
