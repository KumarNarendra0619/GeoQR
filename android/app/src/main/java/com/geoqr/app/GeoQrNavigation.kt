package com.geoqr.app

/** Screen destinations for the Android MVP. */
sealed interface GeoQrRoute {
    data object Home : GeoQrRoute
    data object Create : GeoQrRoute
    data object Scan : GeoQrRoute
    data class Result(val payload: String) : GeoQrRoute
}
