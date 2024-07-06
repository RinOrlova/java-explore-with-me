FROM openjdk:11-jre-slim

COPY ../wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

COPY target/statistics-service-0.0.1-SNAPSHOT.jar statistics-service.jar

ENTRYPOINT ["/wait-for-it.sh", "stats-db:5432", "--", "java", "-jar", "/statistics-service.jar"]