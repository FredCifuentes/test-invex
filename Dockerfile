# ====== Build stage ======
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia archivos de Maven (mejor cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Copia código fuente
COPY src src

# Genera el jar
RUN mvn clean package -DskipTests

# ====== Runtime stage ======
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]