FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
RUN mkdir -p app/source
COPY . /app/source
WORKDIR /app/source
RUN mvn clean install

FROM openjdk:21-jdk-oracle
EXPOSE 8081
COPY --from=builder /app/source/target/*.jar app/app.jar
ENTRYPOINT ["java", "-jar","app/app.jar"]