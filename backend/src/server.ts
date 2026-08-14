import Fastify from 'fastify';

const app = Fastify({ logger: true });

app.get('/health', async () => ({
  service: 'geoqr-backend',
  version: '0.2.0',
  status: 'ok'
}));

app.post('/v1/qr', async (_request, reply) => {
  return reply.code(501).send({
    error: 'not_implemented',
    message: 'Persistence/authentication are enabled in the next backend milestone.'
  });
});

app.get('/v1/qr/:qrId', async (request, reply) => {
  const { qrId } = request.params as { qrId: string };
  return reply.code(501).send({
    error: 'not_implemented',
    qrId
  });
});

app.post('/v1/qr/:qrId/scans', async (request, reply) => {
  const { qrId } = request.params as { qrId: string };
  return reply.code(501).send({
    error: 'not_implemented',
    qrId,
    message: 'Scan persistence is intentionally not enabled until consent validation and rate limiting are wired.'
  });
});

const port = Number(process.env.PORT ?? 8080);
const host = process.env.HOST ?? '0.0.0.0';

app.listen({ port, host }).catch((error) => {
  app.log.error(error);
  process.exit(1);
});
