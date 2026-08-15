package com.geoqr.app.scan

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun GeoQrLocationConsentDialog(
    onAllow: () -> Unit,
    onDecline: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDecline,
        title = { Text("Share scan location?") },
        text = {
            Text("GeoQR can record your approximate device location with this scan. Your location is not submitted unless you allow it.")
        },
        confirmButton = { TextButton(onClick = onAllow) { Text("Allow") } },
        dismissButton = { TextButton(onClick = onDecline) { Text("Not now") } }
    )
}
