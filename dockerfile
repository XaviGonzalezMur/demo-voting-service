FROM openjdk:17-jdk-slim
COPY target/*.jar demo-microservices.jar
ENTRYPOINT ["java", "-jar", "demo-microservices.jar"]
