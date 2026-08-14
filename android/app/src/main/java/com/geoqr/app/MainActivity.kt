package com.geoqr.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GeoQrHome() }
    }
}

@Composable
private fun GeoQrHome() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("GeoQR", style = MaterialTheme.typography.headlineLarge)
            Text("Location-aware QR infrastructure", style = MaterialTheme.typography.bodyLarge)
            Text("Create, scan and map GeoQR codes. Location is permission-based.")
            Button(onClick = { /* V0.2: open creator */ }) { Text("Create GeoQR") }
            Button(onClick = { /* V0.2: open scanner */ }) { Text("Scan GeoQR") }
        }
    }
}
