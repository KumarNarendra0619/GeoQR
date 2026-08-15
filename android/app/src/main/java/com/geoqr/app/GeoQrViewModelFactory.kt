package com.geoqr.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geoqr.app.create.GeoQrCreateViewModel
import com.geoqr.app.scan.GeoQrResolveViewModel
import com.geoqr.app.scan.GeoQrScanEventViewModel

class GeoQrViewModelFactory(private val container: GeoQrAppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(GeoQrCreateViewModel::class.java) ->
            GeoQrCreateViewModel(container.api, container.locationProvider) as T
        modelClass.isAssignableFrom(GeoQrResolveViewModel::class.java) ->
            GeoQrResolveViewModel(container.api) as T
        modelClass.isAssignableFrom(GeoQrScanEventViewModel::class.java) ->
            GeoQrScanEventViewModel(container.api, container.locationProvider) as T
        else -> error("Unsupported ViewModel: ${modelClass.name}")
    }
}
