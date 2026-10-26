FROM eclipse-temurin:21-jre-alpine

COPY ./target/funkomania-api-1.1.0-SNAPSHOT-2.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]