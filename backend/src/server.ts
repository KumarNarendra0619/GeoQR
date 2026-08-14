import Fastify from 'fastify';
import { Pool } from 'pg';
import crypto from 'node:crypto';

const app = Fastify({ logger: true });
const pool = new Pool({ connectionString: process.env.DATABASE_URL });
const id = (n = 12) => crypto.randomBytes(n).toString('base64url');

app.get('/health', async () => ({ service: 'geoqr-backend', version: '0.2.1', status: 'ok' }));

app.post('/v1/qr', async (request, reply) => {
  const body = request.body as any;
  if (!body?.destination || typeof body.destination !== 'string') return reply.code(400).send({ error: 'destination_required' });
  const qrId = id(9);
  const accessToken = id(24);
  const client = await pool.connect();
  try {
    await client.query('BEGIN');
    await client.query(`INSERT INTO geoqr (qr_id, owner_id, qr_type, destination_url, access_token, generation_point, generation_accuracy, generated_at, status)
      VALUES ($1,$2,$3,$4,$5,CASE WHEN $6::double precision IS NULL OR $7::double precision IS NULL THEN NULL ELSE ST_SetSRID(ST_MakePoint($7,$6),4326)::geography END,$8,NOW(),'active')`,
      [qrId, body.ownerId ?? null, body.type ?? 'url', body.destination, accessToken, body.latitude ?? null, body.longitude ?? null, body.accuracy ?? null]);
    await client.query('COMMIT');
    return reply.code(201).send({ qrId, payload: `GQR://v1/${qrId}/${accessToken}` });
  } catch (error) {
    await client.query('ROLLBACK');
    request.log.error(error);
    return reply.code(500).send({ error: 'create_failed' });
  } finally { client.release(); }
});

app.get('/v1/qr/:qrId', async (request, reply) => {
  const { qrId } = request.params as { qrId: string };
  const result = await pool.query('SELECT qr_id, qr_type, destination_url, status, expires_at, generated_at FROM geoqr WHERE qr_id=$1', [qrId]);
  if (!result.rowCount) return reply.code(404).send({ error: 'not_found' });
  const qr = result.rows[0];
  if (qr.status !== 'active' || (qr.expires_at && new Date(qr.expires_at) <= new Date())) return reply.code(410).send({ error: 'qr_inactive' });
  return { qrId: qr.qr_id, type: qr.qr_type, destination: qr.destination_url, generatedAt: qr.generated_at };
});

app.post('/v1/qr/:qrId/scans', async (request, reply) => {
  const { qrId } = request.params as { qrId: string };
  const body = request.body as any;
  const exists = await pool.query("SELECT 1 FROM geoqr WHERE qr_id=$1 AND status='active'", [qrId]);
  if (!exists.rowCount) return reply.code(404).send({ error: 'not_found' });
  const result = await pool.query(`INSERT INTO scan_events (scan_id, qr_id, scanned_at, location_consent, scan_point, scan_accuracy)
    VALUES ($1,$2,NOW(),$3,CASE WHEN $4::double precision IS NULL OR $5::double precision IS NULL THEN NULL ELSE ST_SetSRID(ST_MakePoint($5,$4),4326)::geography END,$6)
    RETURNING scan_id, scanned_at`, [id(), qrId, body?.locationConsent === true, body?.latitude ?? null, body?.longitude ?? null, body?.accuracy ?? null]);
  return reply.code(201).send(result.rows[0]);
});

app.get('/v1/qr/:qrId/analytics', async (request) => {
  const { qrId } = request.params as { qrId: string };
  const result = await pool.query(`SELECT COUNT(*)::int AS total_scans, COUNT(*) FILTER (WHERE location_consent=true)::int AS location_consented,
    MIN(scanned_at) AS first_scan, MAX(scanned_at) AS last_scan FROM scan_events WHERE qr_id=$1`, [qrId]);
  return { qrId, ...result.rows[0] };
});

const port = Number(process.env.PORT ?? 8080);
const host = process.env.HOST ?? '0.0.0.0';
app.listen({ port, host }).catch((error) => { app.log.error(error); process.exit(1); });
