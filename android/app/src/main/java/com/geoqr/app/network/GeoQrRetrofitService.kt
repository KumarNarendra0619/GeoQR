package com.geoqr.app.network

import com.geoqr.app.model.CreateQrRequest
import com.geoqr.app.model.ScanRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GeoQrRetrofitService {
    @POST("v1/qr")
    suspend fun createQr(@Body request: CreateQrRequest): String

    @GET("v1/qr/{qrId}")
    suspend fun resolveQr(@Path("qrId") qrId: String): String

    @POST("v1/qr/{qrId}/scans")
    suspend fun recordScan(@Path("qrId") qrId: String, @Body request: ScanRequest)
}
