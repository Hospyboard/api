FROM maven:3-openjdk-17 AS MAVEN

WORKDIR /container/hospyboard/

COPY pom.xml .
COPY app/pom.xml ./app/
COPY app/src ./app/src

RUN mvn clean package -Dspring-boot.run.profiles=docker

FROM openjdk:17 AS FINAL

WORKDIR /container/java

COPY --from=MAVEN /container/hospyboard/app/target/hospyboard-api-app-*.jar /container/java/server.jar

ENTRYPOINT ["java", "-jar", "/container/java/server.jar", "-Dspring.profiles.active=docker"]
