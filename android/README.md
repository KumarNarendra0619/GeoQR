# GeoQR Android MVP

Kotlin + Jetpack Compose application foundation for GeoQR.

## V0.5 scope

- Create a dynamic GeoQR payload.
- Capture generation location with explicit Android permission.
- Scan GeoQR payloads.
- Resolve a GeoQR through the backend API.
- Request scanner location only with explicit consent.
- Send a scan event to the backend.

## Security boundary

This MVP does not claim production end-to-end encryption. Encryption/key management is a later milestone. No API secrets or private keys belong in the Android source tree.

## Recommended production stack

- Kotlin
- Jetpack Compose
- Android Keystore
- Fused Location Provider
- CameraX / ML Kit or ZXing for scanning
- Retrofit/OkHttp for API transport

The first implementation should keep domain logic independent from UI and Android framework details so the API and cryptographic layers can be hardened without rewriting the UI.
