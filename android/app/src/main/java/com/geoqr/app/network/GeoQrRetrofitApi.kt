package com.geoqr.app.network

import com.geoqr.app.model.GeoPoint
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GeoQrRetrofitService {
    @POST("/v1/qr")
    suspend fun createQr(@Body request: CreateQrDto): CreateQrResponseDto

    @GET("/v1/qr/{qrId}")
    suspend fun resolveQr(@Path("qrId") qrId: String): ResolveQrResponseDto

    @POST("/v1/qr/{qrId}/scans")
    suspend fun recordScan(
        @Path("qrId") qrId: String,
        @Body request: ScanDto
    )
}

class RetrofitGeoQrApi(
    private val service: GeoQrRetrofitService
) : GeoQrApi {
    override suspend fun createQr(request: com.geoqr.app.model.CreateQrRequest): String {
        val response = service.createQr(request.toDto())
        return "GQR://v1/${response.qrId}/${response.token}"
    }

    override suspend fun resolveQr(qrId: String): String =
        service.resolveQr(qrId).destination

    override suspend fun recordScan(
        qrId: String,
        request: com.geoqr.app.model.ScanRequest
    ) {
        service.recordScan(qrId, request.toDto())
    }
}
