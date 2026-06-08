FROM eclipse-temurin:21-jre-alpine

COPY ./target/funkomania-api-0.6.0-SNAPSHOT-1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]