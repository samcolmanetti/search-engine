# Build stage: JDK 21 + Maven. Tests and style checks run in CI/locally, not here.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package -DskipTests -Dspotless.check.skip -Dcheckstyle.skip -Djacoco.skip

# Run stage: just the JRE and the jar.
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
