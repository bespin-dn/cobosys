FROM openjdk:17-alpine

CMD java -jar ./build/libs/demo-0.0.1-SNAPSHOT.war
EXPOSE 8080