FROM gradle:8.8-jdk21-alpine AS build
WORKDIR /src
COPY . .
RUN gradle clean bootJar --no-daemon

FROM gcr.io/distroless/java21
WORKDIR /app
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
