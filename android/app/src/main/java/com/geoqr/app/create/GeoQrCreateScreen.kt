package com.geoqr.app.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GeoQrCreateScreen(
    onCreate: (type: String, destination: String, includeLocation: Boolean) -> Unit
) {
    var type by remember { mutableStateOf("website") }
    var destination by remember { mutableStateOf("") }
    var includeLocation by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Create GeoQR")
        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Content type") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Link or content destination") },
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = includeLocation, onCheckedChange = { includeLocation = it })
            Text("Mark generation location")
        }
        Button(
            onClick = { onCreate(type, destination, includeLocation) },
            enabled = destination.isNotBlank()
        ) { Text("Generate GeoQR") }
    }
}
