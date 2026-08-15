package com.geoqr.app.core

import com.geoqr.app.model.GeoQrPayload

object GeoQrParser {
    private const val PREFIX = "GQR://v1/"

    fun parse(value: String): GeoQrPayload? {
        if (!value.startsWith(PREFIX)) return null
        val parts = value.removePrefix(PREFIX).split('/')
        if (parts.size != 2 || parts.any { it.isBlank() }) return null
        return GeoQrPayload(qrId = parts[0], token = parts[1])
    }

    fun encode(payload: GeoQrPayload): String =
        "$PREFIX${payload.qrId}/${payload.token}"
}
