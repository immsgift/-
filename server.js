const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const PUBLIC_DIR = path.join(__dirname, 'public');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.webmanifest': 'application/manifest+json'
};

const server = http.createServer((req, res) => {
  let reqPath = '/index.html';
  try {
    const rawPath = (req.url || '/').split('?')[0];
    reqPath = decodeURI(rawPath);
  } catch (e) {
    reqPath = '/index.html';
  }

  if (reqPath === '/' || reqPath === '') {
    reqPath = '/index.html';
  }

  let filePath = path.join(PUBLIC_DIR, reqPath);

  // Security check: ensure path is within PUBLIC_DIR
  if (!filePath.startsWith(PUBLIC_DIR)) {
    filePath = path.join(PUBLIC_DIR, 'index.html');
  }

  fs.stat(filePath, (err, stats) => {
    if (err || !stats.isFile()) {
      filePath = path.join(PUBLIC_DIR, 'index.html');
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    res.setHeader('Content-Type', contentType);
    res.setHeader('Access-Control-Allow-Origin', '*');

    if (ext === '.html' || ext === '.js' || ext === '.css' || ext === '.json') {
      res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
    } else {
      res.setHeader('Cache-Control', 'public, max-age=86400');
    }

    const stream = fs.createReadStream(filePath);
    stream.on('error', () => {
      res.statusCode = 500;
      res.end('Error loading file');
    });
    stream.pipe(res);
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`PWA Web Server running on http://0.0.0.0:${PORT}`);
});
