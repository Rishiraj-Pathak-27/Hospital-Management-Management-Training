# Patient Service Microservice

A Spring Boot microservice for the Hospital Management System that exposes the HMS workflows as REST APIs and validates access through the auth service.

## What it does

- Add and view patients
- Add and view doctors
- Book and view appointments
- Admit patients with ward and bed allocation
- View wards, beds, and admissions
- Validate JWT tokens through the auth service before serving protected requests

## Ports

- Auth service: `8085`
- Patient service: `8086`

## Authentication flow

This service does not issue tokens.

1. Register and log in through the auth service.
2. Send the returned JWT as `Authorization: Bearer <token>` to patient service requests.
3. Patient service forwards the token to `GET /api/auth/validate` on the auth service and only serves the request if the token is valid.

## Database

The service uses its own `patientdb` MySQL database and the HMS tables required by the patient-service entities.

The datasource URL enables `createDatabaseIfNotExist=true`, so MySQL can create the database automatically if needed.

Tables created in `patientdb`:

- `patients`
- `doctors`
- `appointments`
- `wards`
- `beds`
- `admissions`


## Configuration

Environment variables:

```bash
DB_URL=jdbc:mysql://localhost:3306/patientdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=<mysql-password>
AUTH_SERVICE_URL=http://localhost:8085
```

This service also accepts the workspace-style names `DATABASE_URL`, `DATABASE_USER`, and `DATABASE_PASSWORD`.
For the local setup in this repo, `Root@1234` is the password used in the sample `.env` files.
The startup script also reads `../auth-service/.env` first, so the patient service can reuse the shared secret values already maintained for the auth service while still targeting `patientdb`.

## Build

```bash
cd /media/rishiraj/New\ Volume/CRT_Phase_2/microservices/patient-service
mvn clean package -DskipTests
```

## Run

```bash
set -a
. .env
set +a
java -jar target/patient-service-0.0.1-SNAPSHOT.jar
```

## Example requests

### Health

```bash
curl http://localhost:8086/api/patients/health
```

### View patients

```bash
curl http://localhost:8086/api/patients/patients \
  -H "Authorization: Bearer $TOKEN"
```

### Add a patient

```bash
curl -X POST http://localhost:8086/api/patients/patients \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 3,
    "name": "Asha",
    "age": 28,
    "disease": "Fever"
  }'
```

### Admit a patient

```bash
curl -X POST http://localhost:8086/api/patients/admissions \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 3,
    "severity": "high",
    "notes": "Requires observation"
  }'
```
