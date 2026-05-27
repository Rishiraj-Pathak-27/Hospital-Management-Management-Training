# Environment Variables Reference

Copy these templates and fill in with your values.

## Neon PostgreSQL Variables

Get these from: https://console.neon.tech

```
NEON_CONNECTION_STRING=postgresql://user:password@ep-xxxxx.neon.tech/dbname?sslmode=require
```

Split into:
```
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/authdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password
```

## Eureka Server Variables

After deploying Eureka on Render:

```
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/
```

## Auth Service Variables

```
PORT=8085
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/authdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password
JWT_SECRET=your-secret-key-minimum-32-characters-long-is-required!!!
JWT_EXPIRATION=86400000
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/
```

## Patient Service Variables

```
PORT=8086
DB_URL=postgresql://user:password@ep-xxxxx.neon.tech/patientdb?sslmode=require
DB_USERNAME=user
DB_PASSWORD=password
AUTH_SERVICE_URL=https://auth-service-xxxx.onrender.com
EUREKA_SERVER_URL=https://eureka-server-xxxx.onrender.com/eureka/
```

## Frontend Variables (in app.js)

```javascript
const API_BASE_URL = 'https://patient-service-xxxx.onrender.com/api/patients';
```

---

## How to Set on Render

1. Go to your service in Render dashboard
2. Click "Environment" tab
3. Add each variable as Key=Value
4. Click "Deploy" to apply changes

## How to Set locally (.env file)

Create `.env` file in service directory:

```bash
# For auth-service: microservices/auth-service/.env
PORT=8085
DB_URL=...
DB_USERNAME=...
DB_PASSWORD=...
JWT_SECRET=...
JWT_EXPIRATION=...
EUREKA_SERVER_URL=...
```

Then load with:
```bash
set -a
. .env
set +a
```

## Security Best Practices

1. **Never commit secrets to GitHub**
2. **Use environment variables for sensitive data**
3. **Keep JWT_SECRET strong (32+ characters)**
4. **Use unique passwords for each database**
5. **Rotate JWT_SECRET regularly**
6. **Use HTTPS for all connections** (Render auto-provides)

## Where to Get Values

| Variable | Source |
|----------|--------|
| DB_URL | Neon console |
| DB_USERNAME | Neon console |
| DB_PASSWORD | Neon console |
| JWT_SECRET | Generate random string (min 32 chars) |
| EUREKA_SERVER_URL | Render deployed service URL |
| AUTH_SERVICE_URL | Render deployed service URL |
| API_BASE_URL | Render deployed patient-service URL |

## Testing Variables

To verify your environment variables are set:

**On Render dashboard:**
- Go to Environment tab
- All variables should be listed

**In running service:**
- Check logs for startup parameters
- Health endpoint should be accessible

## Common Mistakes

❌ **Don't do this:**
- Hardcode URLs in source code
- Commit .env files to Git
- Use weak JWT secrets
- Mix database URLs

✅ **Do this:**
- Use environment variables
- Add .env to .gitignore
- Use strong secrets
- Separate DB per service
