FROM openjdk:17
EXPOSE 10020
COPY build/libs/board-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application-docker.yml application.yml
ENTRYPOINT ["java", "-jar", "app.jar"]