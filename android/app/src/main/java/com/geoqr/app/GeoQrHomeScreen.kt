package com.geoqr.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GeoQrHomeScreen(
    onCreate: () -> Unit,
    onScan: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("GeoQR", style = MaterialTheme.typography.headlineLarge)
        Text("Location-aware QR sharing", modifier = Modifier.padding(top = 8.dp, bottom = 28.dp))
        Button(onClick = onCreate) { Text("Create GeoQR") }
        Button(onClick = onScan, modifier = Modifier.padding(top = 12.dp)) { Text("Scan GeoQR") }
    }
}
