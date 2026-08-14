# GeoQR Backend — V0.2

The backend is intentionally separate from the GitHub Pages frontend. GitHub Pages must never contain database credentials, signing keys, encryption master keys, or privileged API secrets.

## Responsibilities

- Create and resolve dynamic GeoQR records.
- Store content metadata and encrypted-object references.
- Authenticate owners and authorize QR management.
- Receive consented scan events.
- Receive consented scanner location with accuracy and timestamp.
- Issue short-lived signed object-download URLs.
- Apply rate limits, validation and abuse controls.

## Planned stack

- API: TypeScript + Fastify
- Database: PostgreSQL + PostGIS
- Object storage: S3-compatible storage
- Authentication: passkey/OAuth-compatible identity layer
- Validation: JSON Schema / TypeBox
- Deployment: containerized managed service

## V0.2 rule

No production encryption claim is made until the key-management protocol is implemented and independently reviewed. The current web MVP is a client-side UX prototype.
