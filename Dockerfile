FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

COPY src src

RUN mvn clean package

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/target/social_media_service-0.0.1-SNAPSHOT.jar .
COPY .env .
