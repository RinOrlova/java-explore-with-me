FROM openjdk:11-jre-slim
RUN apt-get update && apt-get install -y curl
COPY ../target/ewm-server-0.0.1-SNAPSHOT.jar ewm-server.jar
ENTRYPOINT ["java", "-jar", "/ewm-server.jar"]