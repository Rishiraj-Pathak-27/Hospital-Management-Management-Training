# Eureka Server - Render Configuration

## Service Details
- **Name:** eureka-server
- **Port:** 8761
- **Type:** Service Registry (no database needed)

## Build & Start Commands

**Build Command:**
```bash
cd microservices/eureka-server && mvn clean package -DskipTests
```

**Start Command:**
```bash
cd microservices/eureka-server && java -jar target/eureka-server-*.jar
```

## Environment Variables

```
PORT=8761
```

## Render Dashboard Setup

1. Click "New +" → "Web Service"
2. Connect your GitHub repository
3. Fill in:
   - **Name:** eureka-server
   - **Root Directory:** (leave blank, uses repo root)
   - **Build Command:** See above
   - **Start Command:** See above
   - **Environment:** Java 17
   - **Instance Type:** Free tier

4. Add Environment Variables:
   - PORT=8761

5. Click "Create Web Service"

## Verification

After deployment (3-5 minutes):

```bash
# Check if running
curl https://eureka-server-xxxx.onrender.com/

# Should return Eureka dashboard
```

## Important Notes

- This starts FIRST
- No database needed
- Auth and Patient services register with this
- Keep the URL safe - you'll need it for other services

## Troubleshooting

**Service won't start:**
- Check Java version (should be 17)
- Check build logs for compilation errors
- Verify pom.xml is valid

**Port issue:**
- Render automatically assigns PORT via environment variable
- Don't hardcode port in application.properties

## Next Step

Once this is running, deploy Auth Service and update its eureka.client.service-url.defaultZone to point here.
