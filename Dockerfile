#Dockerfile for Spring Boot,Java 16 - Build and running
FROM adoptopenjdk/maven-openjdk11:latest AS MAVEN_BUILD

MAINTAINER Andrija Vojnovic

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn clean install -DskipTests
# RUN mvn clean install

FROM eclipse-temurin

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/restaurant-0.0.1-SNAPSHOT.jar /app/
COPY --from=MAVEN_BUILD /build /app/


ENTRYPOINT ["java", "-jar", "restaurant-0.0.1-SNAPSHOT.jar"]
