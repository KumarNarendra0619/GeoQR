package com.geoqr.app

import android.content.Context
import com.geoqr.app.location.FusedLocationProvider
import com.geoqr.app.location.LocationProvider
import com.geoqr.app.network.GeoQrApi
import com.geoqr.app.network.GeoQrRetrofitService
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GeoQrAppContainer(context: Context) {
    private val moshi = Moshi.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.geoqr.app/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: GeoQrApi = com.geoqr.app.network.RetrofitGeoQrApi(
        retrofit.create(GeoQrRetrofitService::class.java)
    )
    val locationProvider: LocationProvider = FusedLocationProvider(context.applicationContext)
}
