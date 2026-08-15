package com.geoqr.app.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geoqr.app.location.LocationProvider
import com.geoqr.app.model.CreateQrRequest
import com.geoqr.app.model.GeoPoint
import com.geoqr.app.network.GeoQrApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CreateQrState {
    data object Idle : CreateQrState
    data object RequestingLocation : CreateQrState
    data object Creating : CreateQrState
    data class Created(val payload: String) : CreateQrState
    data class Error(val message: String) : CreateQrState
}

class GeoQrCreateViewModel(
    private val api: GeoQrApi,
    private val locationProvider: LocationProvider
) : ViewModel() {
    private val _state = MutableStateFlow<CreateQrState>(CreateQrState.Idle)
    val state: StateFlow<CreateQrState> = _state.asStateFlow()

    fun create(type: String, destination: String, includeLocation: Boolean) {
        if (destination.isBlank()) {
            _state.value = CreateQrState.Error("Destination is required")
            return
        }
        viewModelScope.launch {
            val location = if (includeLocation) {
                _state.value = CreateQrState.RequestingLocation
                locationProvider.currentLocation()
            } else null
            if (includeLocation && location == null) {
                _state.value = CreateQrState.Error("Unable to obtain generation location")
                return@launch
            }
            _state.value = CreateQrState.Creating
            runCatching {
                api.createQr(
                    CreateQrRequest(
                        type = type,
                        destination = destination,
                        generationLocation = location?.let {
                            GeoPoint(it.latitude, it.longitude, it.accuracyMeters)
                        }
                    )
                )
            }.onSuccess { _state.value = CreateQrState.Created(it) }
                .onFailure { _state.value = CreateQrState.Error(it.message ?: "Unable to create GeoQR") }
        }
    }
}
