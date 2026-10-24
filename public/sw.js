const CACHE_NAME = 'imani-quran-v2.7';
const STATIC_ASSETS = [
  '/',
  '/index.html',
  '/style.css?v=2.6',
  '/app.js?v=2.7',
  '/stories-data.js?v=2.7',
  '/adhkar-data.js?v=2.7',
  '/icon.svg',
  '/manifest.json',
  '/apple-touch-icon.png',
  '/icon-192.png',
  '/icon-512.png'
];

self.addEventListener('install', (event) => {
  self.skipWaiting();
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(STATIC_ASSETS))
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.map((key) => {
          if (key !== CACHE_NAME) {
            return caches.delete(key);
          }
        })
      );
    }).then(() => self.clients.claim())
  );
});

self.addEventListener('fetch', (event) => {
  const url = new URL(event.request.url);

  // Cache-first for Surahs
  if (url.pathname.startsWith('/quran/')) {
    event.respondWith(
      caches.match(event.request).then((cached) => {
        if (cached) return cached;
        return fetch(event.request).then((netRes) => {
          if (netRes && netRes.status === 200) {
            const clone = netRes.clone();
            caches.open(CACHE_NAME).then((c) => c.put(event.request, clone));
          }
          return netRes;
        });
      })
    );
    return;
  }

  // Network-first for HTML and scripts to always get updates, falling back to cache offline
  event.respondWith(
    fetch(event.request).catch(() => caches.match(event.request))
  );
});
