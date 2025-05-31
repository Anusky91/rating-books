# Etapa de build (compila el proyecto)
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de runtime (imagen más ligera solo con el jar)
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/bookstar.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
