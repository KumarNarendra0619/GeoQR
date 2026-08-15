package com.geoqr.app.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geoqr.app.location.GeoLocation
import com.geoqr.app.location.LocationProvider
import com.geoqr.app.model.GeoPoint
import com.geoqr.app.model.ScanRequest
import com.geoqr.app.network.GeoQrApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface ScanEventState {
    data object Idle : ScanEventState
    data object RequestingLocation : ScanEventState
    data object Recording : ScanEventState
    data object Recorded : ScanEventState
    data class Error(val message: String) : ScanEventState
}

class GeoQrScanEventViewModel(
    private val api: GeoQrApi,
    private val locationProvider: LocationProvider
) : ViewModel() {
    private val _state = MutableStateFlow<ScanEventState>(ScanEventState.Idle)
    val state: StateFlow<ScanEventState> = _state.asStateFlow()

    fun submit(qrId: String, consentGranted: Boolean) {
        if (!consentGranted) {
            _state.value = ScanEventState.Error("Location consent was not granted")
            return
        }
        viewModelScope.launch {
            _state.value = ScanEventState.RequestingLocation
            val location = runCatching { locationProvider.currentLocation() }.getOrNull()
            if (location == null || !location.isValid()) {
                _state.value = ScanEventState.Error("Unable to obtain a valid location")
                return@launch
            }
            _state.value = ScanEventState.Recording
            runCatching {
                api.recordScan(
                    qrId,
                    ScanRequest(
                        sessionId = UUID.randomUUID().toString(),
                        locationConsent = true,
                        location = GeoPoint(location.latitude, location.longitude, location.accuracyMeters)
                    )
                )
            }.onSuccess { _state.value = ScanEventState.Recorded }
                .onFailure { _state.value = ScanEventState.Error(it.message ?: "Unable to record scan") }
        }
    }
}

private fun GeoLocation.isValid(): Boolean =
    latitude in -90.0..90.0 && longitude in -180.0..180.0
