package com.geoqr.app.qr

import com.geoqr.app.core.GeoQrParser
import com.geoqr.app.model.GeoQrPayload

class GeoQrScanUseCase {
    fun parse(frame: GeoQrScanFrame): GeoQrPayload? {
        val raw = GeoQrScanValidator.accept(frame) ?: return null
        return GeoQrParser.parse(raw)
    }
}
