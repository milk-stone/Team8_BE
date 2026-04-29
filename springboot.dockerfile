# Build stage

FROM bellsoft/liberica-openjdk-alpine:17 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies --no-daemon || true

COPY src src

RUN ./gradlew clean build -x test

VOLUME /tmp

# Run stage
FROM bellsoft/liberica-openjdk-alpine:17 AS runner

COPY --from=builder /app/build/libs/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]