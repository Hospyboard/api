FROM maven:latest AS MAVEN

WORKDIR /container/src/app/
COPY . .
RUN mvn package

FROM openjdk:16 AS FINAL

WORKDIR /container/java

COPY --from=MAVEN /container/src/app/target/Hospital-*.jar /container/java/server.jar

ENTRYPOINT ["java", "-jar", "/container/java/server.jar"]