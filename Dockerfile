FROM eclipse-temurin:21-jre-alpine

COPY ./target/funkomania-api-0.2.0-SNAPSHOT-3.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]