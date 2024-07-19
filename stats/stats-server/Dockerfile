FROM openjdk:11-jre-slim
RUN apt-get update && apt-get install -y curl
COPY target/stats-server-0.0.1-SNAPSHOT.jar stats-server.jar
ENTRYPOINT ["java", "-jar", "/stats-server.jar"]