FROM amazoncorretto:17-alpine-jdk
COPY build/libs/supportly-backend-0.0.1-SNAPSHOT.jar supportly.jar

EXPOSE 8080
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "supportly.jar"]