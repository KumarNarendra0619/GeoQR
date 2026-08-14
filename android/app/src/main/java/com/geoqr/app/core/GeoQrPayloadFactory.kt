package com.geoqr.app.core

import com.geoqr.app.model.GeoQrPayload

object GeoQrPayloadFactory {
    fun create(qrId: String, token: String): GeoQrPayload {
        require(qrId.isNotBlank()) { "qrId must not be blank" }
        require(token.isNotBlank()) { "token must not be blank" }
        return GeoQrPayload(qrId = qrId, token = token)
    }
}
