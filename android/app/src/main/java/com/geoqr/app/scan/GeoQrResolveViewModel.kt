package com.geoqr.app.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geoqr.app.core.GeoQrParser
import com.geoqr.app.network.GeoQrApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ResolveState {
    data object Idle : ResolveState
    data object Loading : ResolveState
    data class Success(val qrId: String, val destination: String) : ResolveState
    data class Error(val message: String) : ResolveState
}

class GeoQrResolveViewModel(private val api: GeoQrApi) : ViewModel() {
    private val _state = MutableStateFlow<ResolveState>(ResolveState.Idle)
    val state: StateFlow<ResolveState> = _state.asStateFlow()

    fun resolve(rawValue: String) {
        val payload = GeoQrParser.parse(rawValue)
        if (payload == null) {
            _state.value = ResolveState.Error("Invalid GeoQR code")
            return
        }
        viewModelScope.launch {
            _state.value = ResolveState.Loading
            runCatching { api.resolveQr(payload.qrId) }
                .onSuccess { _state.value = ResolveState.Success(payload.qrId, it) }
                .onFailure { _state.value = ResolveState.Error(it.message ?: "Unable to resolve GeoQR") }
        }
    }
}
