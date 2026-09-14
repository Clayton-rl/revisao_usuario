# Stage 1: Builder
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY gradle /build/gradle
COPY gradlew /build/gradlew
COPY gradlew.bat /build/gradlew.bat
COPY build.gradle /build/
COPY settings.gradle /build/
COPY src /build/src

RUN chmod +x /build/gradlew && \
    /build/gradlew build -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/build/libs/revisao_usuario-0.0.1-SNAPSHOT.jar /app/revisao_usuario.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/revisao_usuario.jar"]
