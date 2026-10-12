## How to Use

### Web app

1. Open the live site: https://qr-code-generator-sand-psi.vercel.app
2. Type or paste a URL (e.g. `github.com`). `https://` is added for you if you leave it out.
3. Click **GENERATE**.
4. Click **Download PNG** to save the QR code.

> The backend runs on a free Render instance that sleeps when idle. The first request after a quiet period can take up to a minute while it wakes up. Wait a moment and try again.

### Public API

The API is free and open, so you can use it in your own projects. CORS is enabled for all origins.

**Endpoint**

```
GET https://qr-code-be-l95m.onrender.com/api/qrcode/qr
```

**Query parameters**

| Parameter  | Required | Default | Description |
|------------|----------|---------|-------------|
| `url`      | Yes      | none    | The `http` or `https` URL to encode (max 2000 characters). Must be URL-encoded. |
| `size`     | No       | `300`   | Image width and height in pixels. Clamped between `100` and `1000`. |
| `download` | No       | `false` | Set to `true` to make the browser download the file as `qrcode.png` instead of displaying it. |

**Response:** a PNG image (`image/png`).

**Errors**

| Status | Meaning |
|--------|---------|
| `400`  | Missing, invalid or too-long URL, or the URL isn't `http`/`https`. |
| `500`  | The QR code couldn't be generated. |

### Examples

**In HTML**

```html
<img
  src="https://qr-code-be-l95m.onrender.com/api/qrcode/qr?url=https%3A%2F%2Fgithub.com&size=300"
  alt="QR code"
/>
```

**In JavaScript** (`encodeURIComponent` stops characters like `?` and `&` from breaking the request)

```js
const target = 'https://github.com/unknown-python3672'
const src = `https://qr-code-be-l95m.onrender.com/api/qrcode/qr?url=${encodeURIComponent(target)}&size=400`
```

**With curl**

```bash
curl "https://qr-code-be-l95m.onrender.com/api/qrcode/qr?url=https%3A%2F%2Fgithub.com&size=400" -o qrcode.png
```

**Force a download**

```
https://qr-code-be-l95m.onrender.com/api/qrcode/qr?url=https%3A%2F%2Fgithub.com&download=true
```

### Run it locally

**Backend** (Java 21, Maven)

```bash
mvn spring-boot:run
```

Runs on `http://localhost:8080`.

**Frontend** (Node.js)

```bash
npm install
npm run dev
```

Runs on `http://localhost:3000`.