package com.geoqr.app.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GeoQrDestinationScreen(
    state: ResolveState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            ResolveState.Idle -> Text("Scan a GeoQR code")
            ResolveState.Loading -> CircularProgressIndicator()
            is ResolveState.Success -> {
                Text("GeoQR resolved", style = MaterialTheme.typography.headlineSmall)
                Text(state.destination, modifier = Modifier.padding(top = 12.dp))
            }
            is ResolveState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
        }
    }
}
