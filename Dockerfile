FROM maven:3.8.6-eclipse-temurin-17 as build
WORKDIR /app
COPY mvnw pom.xml ./
COPY /src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-jammy

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/user-management-0.0.1-SNAPSHOT.jar /app/application.jar
ENTRYPOINT ["java", "-jar", "/app/application.jar"]