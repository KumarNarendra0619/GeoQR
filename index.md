---
title: GeoQR
subtitle: Location-aware QR infrastructure
---

# GeoQR

GeoQR combines dynamic QR sharing with optional geospatial context. The project is being built in controlled milestones: architecture → web MVP → backend → Android → encryption → location analytics → production hardening.

## Web MVP

The first interactive creator is deployed as a lightweight static application:

[**Open GeoQR Creator →**](app/)

The current MVP supports destination URLs, content-type metadata, generation-location capture with browser permission, compact `GQR://v1/<id>/<token>` payloads, client-side QR rendering and PNG download.

> **Security status:** This is an MVP. It does not claim production end-to-end encryption, persistent backend storage, or silent scanner-location collection.

## Repository

- `web/` — GitHub Pages web MVP
- `backend/` — backend boundary and API contract
- `docs/` — architecture and milestone documentation
- `.github/workflows/` — GitHub Pages deployment
