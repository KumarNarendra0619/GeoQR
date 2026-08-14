# GeoQR API Contract — V0.1

Base path: `/api/v1`

## QR records

`POST /qrs`

Creates a dynamic QR record.

```json
{
  "type": "url",
  "destination": "https://example.org",
  "generationLocation": {
    "latitude": 30.123,
    "longitude": 78.456,
    "accuracy": 12.4
  }
}
```

Response:

```json
{
  "qrId": "01J...",
  "status": "active",
  "resolver": "https://app.example/qr/01J..."
}
```

## Resolve QR

`GET /qrs/{qrId}`

Returns only the public resolver state required for the scanner. Private owner data is never exposed.

## Scan event

`POST /qrs/{qrId}/scans`

```json
{
  "locationConsent": "granted",
  "location": {
    "latitude": 30.124,
    "longitude": 78.457,
    "accuracy": 18.2
  }
}
```

The API must reject malformed coordinates and enforce rate limits.

## QR lifecycle

`POST /qrs/{qrId}/revoke`

`POST /qrs/{qrId}/restore`

`PATCH /qrs/{qrId}`

## Production additions

- `/auth/*`
- `/objects/*`
- `/analytics/*`
- `/users/me`
- audit events
