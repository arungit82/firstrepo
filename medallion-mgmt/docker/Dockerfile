FROM openjdk:8-jre-alpine

WORKDIR /app

ADD ./target/medallion-mgmt-0.1.0.jar /app

ENTRYPOINT ["java", "-jar", "/app/medallion-mgmt-0.1.0.jar"]
