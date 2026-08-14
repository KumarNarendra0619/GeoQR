const $ = (id) => document.getElementById(id);
let locationData = null;
let lastQrData = null;

function randomId(bytes = 9) {
  const a = new Uint8Array(bytes); crypto.getRandomValues(a);
  return [...a].map(x => x.toString(16).padStart(2, '0')).join('');
}

async function sha256(text) {
  const data = new TextEncoder().encode(text);
  const hash = await crypto.subtle.digest('SHA-256', data);
  return [...new Uint8Array(hash)].map(x => x.toString(16).padStart(2, '0')).join('');
}

async function encryptPreview(text) {
  // Demonstration only: production E2EE requires a real key lifecycle and server protocol.
  const key = await crypto.subtle.generateKey({ name: 'AES-GCM', length: 256 }, true, ['encrypt']);
  const iv = crypto.getRandomValues(new Uint8Array(12));
  const ciphertext = await crypto.subtle.encrypt({ name: 'AES-GCM', iv }, key, new TextEncoder().encode(text));
  const exported = await crypto.subtle.exportKey('raw', key);
  return {
    ciphertext: btoa(String.fromCharCode(...new Uint8Array(ciphertext))),
    iv: btoa(String.fromCharCode(...iv)),
    key: btoa(String.fromCharCode(...new Uint8Array(exported)))
  };
}

$('capture').addEventListener('click', () => {
  if (!navigator.geolocation) return alert('Geolocation is not supported by this browser.');
  $('locationText').textContent = 'Requesting location permission…';
  navigator.geolocation.getCurrentPosition(
    p => {
      locationData = { latitude: p.coords.latitude, longitude: p.coords.longitude, accuracy: p.coords.accuracy, capturedAt: new Date().toISOString() };
      $('locationText').textContent = `${locationData.latitude.toFixed(6)}, ${locationData.longitude.toFixed(6)} · ±${Math.round(locationData.accuracy)} m`;
    },
    e => { $('locationText').textContent = `Location not captured: ${e.message}`; },
    { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
  );
});

$('generate').addEventListener('click', async () => {
  const destination = $('destination').value.trim();
  if (!destination) return alert('Enter a destination or file URL first.');
  if (!/^https?:\/\//i.test(destination)) return alert('V0.1 expects an HTTPS/HTTP destination URL.');

  const qrId = randomId();
  const contentHash = await sha256(destination);
  const encrypted = await encryptPreview(JSON.stringify({ destination, location: locationData }));
  const payload = `GQR://v1/${qrId}/${encrypted.ciphertext.slice(0, 32)}`;
  lastQrData = payload;

  const canvas = $('qrCanvas');
  await QRCode.toCanvas(canvas, payload, { width: 220, margin: 2, errorCorrectionLevel: 'H' });
  $('qrLabel').textContent = `GeoQR · ${qrId.slice(0, 8)}`;
  $('payload').hidden = false;
  $('payload').textContent = JSON.stringify({ version: 1, qrId, type: $('type').value, contentHash, generationLocation: locationData, payload }, null, 2);
  $('download').disabled = false;
});

$('download').addEventListener('click', () => {
  if (!lastQrData) return;
  const a = document.createElement('a');
  a.download = 'geoqr.png';
  a.href = $('qrCanvas').toDataURL('image/png');
  a.click();
});
