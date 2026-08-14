const state={type:'url',lat:null,lng:null,accuracy:null,qrId:null};
const $=s=>document.querySelector(s);
const types=$('#types');
types.addEventListener('click',e=>{const b=e.target.closest('[data-type]');if(!b)return;document.querySelectorAll('.type').forEach(x=>x.classList.remove('active'));b.classList.add('active');state.type=b.dataset.type;});

$('#locate').addEventListener('click',()=>{
  const status=$('#location-status'),detail=$('#location-detail');
  if(!navigator.geolocation){status.textContent='Geolocation unavailable';detail.textContent='Use the Android app for native location capture.';return;}
  status.textContent='Requesting location…';
  navigator.geolocation.getCurrentPosition(p=>{state.lat=p.coords.latitude;state.lng=p.coords.longitude;state.accuracy=p.coords.accuracy;status.textContent='Location captured';detail.textContent=`${state.lat.toFixed(5)}, ${state.lng.toFixed(5)} · ±${Math.round(state.accuracy)} m`;},()=>{status.textContent='Location permission denied';detail.textContent='You can still generate a QR without generation coordinates.';},{enableHighAccuracy:true,timeout:10000,maximumAge:0});
});

function randomId(n=8){const a=new Uint8Array(n);crypto.getRandomValues(a);return [...a].map(x=>x.toString(16).padStart(2,'0')).join('').slice(0,n).toUpperCase();}
function token(){const a=new Uint8Array(16);crypto.getRandomValues(a);return [...a].map(x=>x.toString(16).padStart(2,'0')).join('');}
function qrUrl(payload){return `https://api.qrserver.com/v1/create-qr-code/?size=700x700&margin=20&data=${encodeURIComponent(payload)}`;}

$('#generate').addEventListener('click',()=>{
  const target=$('#target').value.trim(),name=$('#title').value.trim()||'Untitled GeoQR';
  if(!/^https?:\/\//i.test(target)){alert('Enter a valid HTTPS/HTTP destination URL.');return;}
  state.qrId=randomId();
  const payload=`GQR://v1/${state.qrId}/${token()}`;
  const img=document.createElement('img');img.alt='Generated GeoQR';img.loading='eager';img.src=qrUrl(payload);
  const stage=$('#qr-stage');stage.replaceChildren(img);stage.dataset.payload=payload;
  $('#qr-name').textContent=name;$('#qr-id').textContent=`${state.qrId} · ${state.type.toUpperCase()} · ${state.lat?'GEO':'NO-GEO'}`;$('#download').disabled=false;
  localStorage.setItem(`geoqr:${state.qrId}`,JSON.stringify({version:1,id:state.qrId,type:state.type,name,target,generationLocation:state.lat?{lat:state.lat,lng:state.lng,accuracy:state.accuracy}:null,recordScanLocation:$('#scan-location').checked,createdAt:new Date().toISOString()}));
});

$('#download').addEventListener('click',()=>{const img=$('#qr-stage img');if(!img)return;const a=document.createElement('a');a.href=img.src;a.download=`GeoQR-${state.qrId||'code'}.png`;a.target='_blank';a.click();});
