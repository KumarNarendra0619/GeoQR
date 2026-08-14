# GeoQR V0.4 API Test Plan

Before Android integration, the backend must pass these cases:

- Health endpoint returns 200.
- Valid GeoQR creation returns a unique ID and opaque token.
- Invalid content type is rejected.
- Invalid coordinates are rejected.
- Expired GeoQR cannot resolve.
- Revoked/inactive GeoQR cannot resolve.
- Valid scan creates one scan event.
- Scanner coordinates are accepted only when consent is true.
- Coordinates without consent are rejected or discarded according to the privacy contract.
- Owner analytics cannot be accessed by an unauthenticated or different owner.
- SQL/PostGIS writes use parameterized queries.
- Rate limiting is required before production exposure.
- Authentication tokens are validated for issuer, audience, signature and expiry.
