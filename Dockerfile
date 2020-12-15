FROM maven:3.6.3-jdk-11 AS builder

COPY . /

RUN mvn dependency:resolve
RUN mvn package

FROM openjdk:11

COPY --from=builder target/api-hospyboard-jar-with-dependencies.jar /api-hospyboard-jar-with-dependencies.jar

EXPOSE 8080

CMD ["java", "-jar", "api-hospyboard-jar-with-dependencies.jar"]
