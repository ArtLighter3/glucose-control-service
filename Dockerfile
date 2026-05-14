#Создание .jar через maven
FROM maven:3.9.15-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests=true

#Разделение .jar на слои зависимостей и самого приложения
FROM eclipse-temurin:21-jre-alpine AS layers
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

#Запуск .jar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=layers /app/extracted/dependencies/ ./
COPY --from=layers /app/extracted/spring-boot-loader/ ./
COPY --from=layers /app/extracted/snapshot-dependencies/ ./
COPY --from=layers /app/extracted/application/ ./

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENV CORS_FRONTEND_URL='http://localhost:5173'
ENV SPRING_DATASOURCE_URL='jdbc:postgresql://localhost:5432/glucose_control_service'
ENV SPRING_DATASOURCE_USERNAME='postgres'
ENV SPRING_DATASOURCE_PASSWORD='postgres'

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]