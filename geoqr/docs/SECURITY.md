# GeoQR Security Model

## Non-negotiable rules

1. TLS is mandatory for all production traffic.
2. Secrets never live in GitHub Pages JavaScript.
3. QR payloads must not contain raw scanner PII or sensitive content.
4. Scanner location is collected only after explicit permission/consent.
5. File downloads use short-lived authorized URLs.
6. QR records support expiry and revocation.
7. File MIME type, size and content are validated server-side.
8. Rate limiting and abuse controls protect QR resolution and scan APIs.

## Encryption

Production content encryption should use authenticated encryption such as AES-256-GCM or an equivalent modern AEAD construction. Each object gets a unique nonce. Key generation, storage, rotation, sharing and recovery must be specified before claiming E2EE.

A suitable public-key primitive for key agreement is X25519. Cryptographic primitives must come from maintained libraries; do not implement cryptography manually.

## Location privacy

The product must clearly distinguish:

- QR generation location
- scanner location
- location accuracy
- location consent

A scan without location permission must remain a valid scan unless the QR owner has explicitly configured location as mandatory and the user has been informed before proceeding.

## Threats considered

- QR phishing
- malicious file distribution
- unauthorized QR enumeration
- location harvesting
- replayed scan tokens
- stolen session tokens
- compromised object storage
- accidental secret exposure in frontend code
