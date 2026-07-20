# === STAGE 1: Build ===
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# 1. chance-less
COPY pom.xml .

# 2. dowload dependencies before and cache them
RUN mvn dependency:go-offline

# 3. chance-more
COPY src ./src

# 4. build packet
RUN mvn package -DskipTests

# === STAGE 2: Runtime ===
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]