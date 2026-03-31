FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Sécurité : utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copier le jar déjà build par GitHub Actions
COPY target/tracking-api.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
