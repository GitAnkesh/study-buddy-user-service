FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw && ./mvnw -B -DskipTests package

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/user-service-0.0.1-SNAPSHOT.jar"]
