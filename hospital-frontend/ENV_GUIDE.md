# Environment Configuration Guide

## Backend API Configuration

The frontend connects to your Hospital Management System backend deployed on Render.

### Current Configuration
```javascript
const API_BASE_URL = 'https://hospital-management-management-training.onrender.com/api/patients';
```

## Development vs Production

### Development Environment
For local testing with a backend running on `localhost:8086`:

1. Edit `app.js` and change the API_BASE_URL:
   ```javascript
   const API_BASE_URL = 'http://localhost:8086/api/patients';
   ```

2. Make sure your backend is running locally or on a development server

3. Start the frontend:
   ```bash
   npm run dev
   ```

### Production Environment (Vercel)
The current configuration uses the Render backend URL, which is perfect for production.

No changes needed - just deploy to Vercel!

## CORS Configuration

The frontend includes CORS headers in vercel.json:
```json
{
  "key": "Access-Control-Allow-Origin",
  "value": "*"
}
```

This allows requests from any origin.

## API Endpoints Reference

All endpoints use the base URL defined in app.js:

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/health` | Check backend health status |
| GET | `/patients` | Retrieve all patients |
| POST | `/patients` | Add new patient |
| GET | `/doctors` | Retrieve all doctors |
| POST | `/doctors` | Add new doctor |
| GET | `/appointments` | Get all appointments |
| POST | `/appointments` | Book appointment |
| GET | `/admissions` | Get all admissions |
| POST | `/admissions` | Process admission |
| GET | `/wards` | Get ward information |
| GET | `/beds` | Get bed information |

## Data Format Examples

### Add Patient
```javascript
{
  "patientId": 1,
  "name": "John Doe",
  "age": 45,
  "disease": "Diabetes"
}
```

### Add Doctor
```javascript
{
  "doctorId": 1,
  "name": "Dr. Jane Smith",
  "specialization": "Cardiology"
}
```

### Book Appointment
```javascript
{
  "appointmentId": 1,
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2026-06-15"
}
```

### Process Admission
```javascript
{
  "patientId": 1,
  "severity": "high",
  "notes": "Patient requires emergency care"
}
```

## Troubleshooting Connection Issues

### If backend is unreachable:

1. **Check backend status**
   - Visit: https://hospital-management-management-training.onrender.com/api/patients/health
   - Should return 200 status with health info

2. **Check CORS headers**
   - Open browser DevTools (F12)
   - Go to Console tab
   - Look for CORS errors

3. **Network tab debugging**
   - Open Network tab in DevTools
   - Make a request (e.g., load patients)
   - Click the failed request
   - Check Response headers for CORS configuration

4. **Timeout issues**
   - Cold start on Render takes 30-60 seconds
   - First request may be slow
   - Subsequent requests are faster

## Testing Locally

### Option 1: Using Python server (Recommended)
```bash
cd hospital-frontend
python -m http.server 8000
# Visit http://localhost:8000
```

### Option 2: Using Node.js
```bash
npm install -g http-server
http-server -p 8000 -c-1
# Visit http://localhost:8000
```

### Option 3: Using VS Code Live Server
- Install "Live Server" extension
- Right-click index.html
- Select "Open with Live Server"

## Performance Tips

1. **Cold start**: First request takes longer (Render wakes up)
2. **Caching**: Browser caches static files, reduces load
3. **Network**: Use browser DevTools to monitor API calls
4. **Pagination**: Current implementation loads all data (consider pagination for large datasets)

## Security Considerations

1. **No sensitive data in localStorage** - User data not persisted
2. **HTTPS only in production** - Vercel provides SSL by default
3. **Input sanitization** - All user input is escaped before display
4. **CORS restrictions** - Can be tightened in production

## Deployment Checklist

- [ ] Verify backend API is running and accessible
- [ ] Test all API endpoints manually
- [ ] Clone frontend repository or download files
- [ ] If using custom backend, update API_BASE_URL in app.js
- [ ] Run `npm install` to install dev dependencies
- [ ] Test locally with `npm run dev`
- [ ] Deploy to Vercel with `vercel` command
- [ ] Test all features on Vercel deployment
- [ ] Share frontend URL with team

## Helpful Resources

- Vercel Docs: https://vercel.com/docs
- Render Docs: https://render.com/docs
- MDN Web APIs: https://developer.mozilla.org/en-US/docs/Web/API
- CORS Guide: https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
