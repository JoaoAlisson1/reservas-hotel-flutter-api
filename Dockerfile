FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY src/main/java/br/csi/dormez_back_api .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]