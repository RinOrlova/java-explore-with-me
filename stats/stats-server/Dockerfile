FROM openjdk:11-jre-slim
COPY target/stats-server-0.0.1-SNAPSHOT.jar stats-server.jar
ENTRYPOINT ["java", "-jar", "/stats-server.jar"]