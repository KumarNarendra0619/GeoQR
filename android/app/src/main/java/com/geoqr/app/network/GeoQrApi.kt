package com.geoqr.app.network

import com.geoqr.app.model.CreateQrRequest
import com.geoqr.app.model.ScanRequest

/** API boundary. Wire implementation will be added after the backend contract is stabilized. */
interface GeoQrApi {
    suspend fun createQr(request: CreateQrRequest): String
    suspend fun resolveQr(qrId: String): String
    suspend fun recordScan(qrId: String, request: ScanRequest)
}
