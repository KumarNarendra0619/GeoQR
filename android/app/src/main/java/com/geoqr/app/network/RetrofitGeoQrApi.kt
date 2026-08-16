package com.geoqr.app.network

import com.geoqr.app.model.CreateQrRequest
import com.geoqr.app.model.ScanRequest

class RetrofitGeoQrApi(
    private val service: GeoQrRetrofitService
) : GeoQrApi {
    override suspend fun createQr(request: CreateQrRequest): String =
        service.createQr(request)

    override suspend fun resolveQr(qrId: String): String =
        service.resolveQr(qrId)

    override suspend fun recordScan(qrId: String, request: ScanRequest) {
        service.recordScan(qrId, request)
    }
}
