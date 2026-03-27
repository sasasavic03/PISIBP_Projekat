FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

# Build the Spring Boot jar
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/api-gateway-*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]