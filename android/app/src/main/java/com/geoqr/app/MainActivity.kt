package com.geoqr.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.geoqr.app.create.GeoQrCreateScreen
import com.geoqr.app.qr.GeoQrScannerHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var route by remember { mutableStateOf<GeoQrRoute>(GeoQrRoute.Home) }
            MaterialTheme {
                Surface {
                    when (val current = route) {
                        GeoQrRoute.Home -> GeoQrHomeScreen(
                            onCreate = { route = GeoQrRoute.Create },
                            onScan = { route = GeoQrRoute.Scan }
                        )
                        GeoQrRoute.Create -> GeoQrCreateScreen(
                            onCreate = { _, _, _ -> }
                        )
                        GeoQrRoute.Scan -> GeoQrScannerHost(
                            onDetected = { _ -> },
                            onClose = { route = GeoQrRoute.Home }
                        )
                        is GeoQrRoute.Result -> route = GeoQrRoute.Home
                    }
                }
            }
        }
    }
}
