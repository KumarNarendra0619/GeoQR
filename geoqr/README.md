# GeoQR V0.1

**GeoQR — Location-aware QR infrastructure**

## Locked architecture

- Android: Kotlin + Jetpack Compose
- Web: lightweight TypeScript/JavaScript static frontend
- Web hosting: GitHub Pages
- Backend: REST API (production stage)
- Database: PostgreSQL + PostGIS (production stage)
- Storage: encrypted object storage (production stage)
- Maps: MapLibre (production dashboard stage)
- Transport: HTTPS/TLS
- Content protection: application-layer authenticated encryption with a defined key lifecycle
- Scanner location: explicit permission and consent; never silent collection

## V0.1 implemented in this branch

- Modern responsive GeoQR web interface
- Client-side QR generation
- URL/Google Form/file URL content types
- Optional generation-location capture
- QR download as PNG
- Stable `GQR://v1/<id>/<token>` payload shape
- Browser Web Crypto demonstration layer
- Android project skeleton
- Architecture and API/security documentation

## Important security boundary

The browser prototype does **not** claim production end-to-end encryption. Production E2EE requires a documented key-management protocol, authenticated encryption, recovery/revocation rules, and security testing. Do not use the prototype for sensitive material.

## Planned V0.2

1. Dynamic QR resolver API
2. PostgreSQL/PostGIS schema
3. Authentication
4. Encrypted object storage
5. Scan-event API
6. Consent-based scan location
7. MapLibre scan map
8. Android QR scanning and creation workflow
