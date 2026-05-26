# Eureka Server

This is a minimal Eureka server for local development.

- Dashboard: http://localhost:8761
- REST registry: http://localhost:8761/eureka/apps

Run:

```
./mvnw -DskipTests package
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
```
