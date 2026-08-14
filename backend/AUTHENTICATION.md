# GeoQR V0.4 Authentication Boundary

V0.4 establishes the authentication boundary before production Android integration.

## Rules

1. Public QR resolution does not require an owner session.
2. QR creation and owner analytics require an authenticated principal.
3. Scanner location is never collected without explicit consent.
4. Access tokens are opaque and must be short-lived where they grant privileged API access.
5. JWT verification must enforce issuer and audience and reject expired tokens.
6. Signing secrets remain deployment secrets; they are never stored in the repository.
7. Production E2EE keys are not JWT secrets and must use a separate key-management design.

## Planned identity flow

Android/Web client -> identity provider or passkey-capable auth layer -> backend access token -> authenticated API.

The backend should authorize every owner operation against the authenticated user ID rather than trusting a user ID supplied in request JSON.
