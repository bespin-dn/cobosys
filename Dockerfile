FROM openjdk:17-alpine

COPY ./build/libs/demo-0.0.1-SNAPSHOT.war ./

CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.war"]
EXPOSE 8080