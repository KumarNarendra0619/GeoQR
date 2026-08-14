# GeoQR System Architecture

```text
Android / Web Client
        |
     HTTPS/TLS
        |
     API Gateway
        |
  +-----+------+----------------+
  |            |                |
 QR Service  Scan Service   Auth Service
  |            |                |
  +------------+----------------+
               |
       PostgreSQL + PostGIS
               |
       Encrypted Object Store
```

## Geographic events

### Generation event

Stored separately from scans:

- latitude
- longitude
- accuracy
- timestamp
- QR ID

### Scan event

Only after explicit user consent:

- QR ID
- scan timestamp
- latitude/longitude when permission is granted
- location accuracy
- consent state
- short-lived scan/session identifier

Avoid persistent device identifiers unless there is a justified security requirement.

## Dynamic QR principle

The printed QR should contain a short, non-sensitive reference rather than the file or location itself:

`GQR://v1/<qr-id>/<token>`

The resolver maps the reference to the current resource. This allows expiry, revocation, destination updates and analytics without reprinting the QR.
