// 'use strict';

// //Import express Module in Node
// //var express = require('express');
// //var app=express();
// //app.use(express.static(__dirname + '/public'));
// //Node Server Config
// //var port = process.env.PORT || 7000;
// //var server=app.listen(port,function(req,res){
// //    console.log("Catch the action at http://localhost:"+port);
// //});

// var express = require('express');
// var app = express();
// var bodyParser = require('body-parser');
// var fs = require('fs');
// var express = require('express');
// var http = require('http');
// var https = require('https');
// var path = require('path');
// var key = fs.readFileSync('privatekey.pem');
// var cert = fs.readFileSync('certificate.pem');
// var https_options = {
//   key: key,
//   cert: cert,
//   passphrase: 'Welcome123',
// };
// var httpsPort = 7200;
// var httpport = process.env.PORT || 7201;
// app.use(express.static(__dirname + '/public'));
// app.use('/', express.static(path.join(__dirname + '/')));
// app.use('/public', express.static(path.join(__dirname + '/public')));
// app.use(
//   '/public/index.html',
//   express.static(path.join(__dirname + '/public/index.html'))
// );
// app.get('/', function (req, res) {
//   res.sendFile(__dirname + '/' + '/public' + '/index.html');
// });
// https.createServer(https_options, app).listen(httpsPort, function (req, res) {
//   console.log('Catch the action at https://localhost:' + httpsPort);
// });
// http.createServer(app).listen(httpport, function (req, res) {
//   console.log('Catch the action at http://localhost:' + httpport);
// });

('use strict');

const express = require('express');
const fs = require('fs');
const path = require('path');
const http = require('http');
const https = require('https');

// Initialize Express app
const app = express();

// Define ports
const HTTP_PORT = process.env.PORT || 7201;
const HTTPS_PORT = 7200;

// Serve static files from /public
app.use(express.static(path.join(__dirname, 'public')));

// Fallback route: serve index.html for root
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// Optional: catch-all for SPA (uncomment if using React/Vue/Angular)
// app.get('*', (req, res) => {
//   res.sendFile(path.join(__dirname, 'public', 'index.html'));
// });

// === SSL Setup ===
let httpsServer;

try {
  const privateKeyPath = path.join(__dirname, 'privatekey.pem');
  const certificatePath = path.join(__dirname, 'certificate.pem');

  // Check if files exist
  if (!fs.existsSync(privateKeyPath)) {
    throw new Error(`SSL private key not found at: ${privateKeyPath}`);
  }
  if (!fs.existsSync(certificatePath)) {
    throw new Error(`SSL certificate not found at: ${certificatePath}`);
  }

  const privateKey = fs.readFileSync(privateKeyPath, 'utf8');
  const certificate = fs.readFileSync(certificatePath, 'utf8');

  const httpsOptions = {
    key: privateKey,
    cert: certificate,
    // passphrase: 'Welcome123'  // ← uncomment only if your key is encrypted
  };

  httpsServer = https.createServer(httpsOptions, app);
} catch (err) {
  console.error('❌ Failed to start HTTPS server:', err.message);
  console.warn(
    '💡 Tip: Generate dev certs with:\n   openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout privatekey.pem -out certificate.pem'
  );
}

// === Start Servers ===

// HTTP Server (always starts)
const httpServer = http.createServer(app);
httpServer.listen(HTTP_PORT, () => {
  console.log(`✅ HTTP server running at http://localhost:${HTTP_PORT}`);
});

// HTTPS Server (starts only if certs are valid)
if (httpsServer) {
  httpsServer.listen(HTTPS_PORT, () => {
    console.log(`🔒 HTTPS server running at https://localhost:${HTTPS_PORT}`);
  });
}

// ('use strict');

// const express = require('express');
// const path = require('path');

// const app = express();
// const PORT = process.env.PORT || 7201;

// app.use(express.static(path.join(__dirname, 'public')));
// app.get('/', (req, res) => {
//   res.sendFile(path.join(__dirname, 'public', 'index.html'));
// });

// app.get('*', (req, res) => {
//   res.sendFile(path.join(__dirname, 'public', 'index.html'));
// });

// app.listen(PORT, () => {
//   console.log(`✅ Server is running!`);
//   console.log(`👉 Open in your browser: http://localhost:${PORT}`);
// });

// app.js;
