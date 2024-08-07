FROM openjdk:21-jdk-slim
WORKDIR /app
COPY app/build/libs/app.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
