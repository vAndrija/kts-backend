#Dockerfile for Spring Boot,Java 16 - Build and running
FROM maven:3.8.3-eclipse-temurin-16 AS MAVEN_BUILD

MAINTAINER Andrija Vojnovic

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn clean install -DskipTests
# RUN mvn clean install

FROM openjdk:16-alpine3.13

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/restaurant-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "restaurant-0.0.1-SNAPSHOT.jar"]
