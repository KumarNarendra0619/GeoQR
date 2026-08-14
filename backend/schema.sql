CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS geoqr (
  id UUID PRIMARY KEY,
  owner_id UUID REFERENCES users(id),
  token_hash TEXT NOT NULL UNIQUE,
  qr_type TEXT NOT NULL,
  encrypted_payload TEXT,
  generation_point geography(Point, 4326),
  generation_accuracy_m NUMERIC,
  generated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at TIMESTAMPTZ,
  status TEXT NOT NULL DEFAULT 'active' CHECK (status IN ('active','suspended','revoked'))
);

CREATE TABLE IF NOT EXISTS objects (
  id UUID PRIMARY KEY,
  qr_id UUID NOT NULL REFERENCES geoqr(id) ON DELETE CASCADE,
  object_type TEXT NOT NULL,
  mime_type TEXT,
  size_bytes BIGINT,
  content_hash TEXT,
  encrypted_storage_ref TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS scan_events (
  id UUID PRIMARY KEY,
  qr_id UUID NOT NULL REFERENCES geoqr(id) ON DELETE CASCADE,
  scanned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  location_consent BOOLEAN NOT NULL DEFAULT false,
  scanner_point geography(Point, 4326),
  scanner_accuracy_m NUMERIC
);

CREATE INDEX IF NOT EXISTS geoqr_generation_point_gix ON geoqr USING GIST (generation_point);
CREATE INDEX IF NOT EXISTS scan_events_scanner_point_gix ON scan_events USING GIST (scanner_point);
CREATE INDEX IF NOT EXISTS scan_events_qr_id_idx ON scan_events(qr_id);
