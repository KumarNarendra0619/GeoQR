package com.geoqr.app.network

// Intentionally left empty.
// The canonical GeoQrRetrofitService interface and RetrofitGeoQrApi implementation
// live in GeoQrRetrofitService.kt and RetrofitGeoQrApi.kt respectively, operating
// directly on domain models (CreateQrRequest/ScanRequest) as wired up in
// GeoQrAppContainer.kt. This file previously duplicated those declarations using
// DTOs, which caused compiler "Redeclaration" errors.
