FROM eclipse-temurin:21.0.5_11-jdk-ubi9-minimal
WORKDIR /cont
COPY ./pom.xml /cont
ADD ./target/L-Airline-0.0.1-SNAPSHOT.jar /flightms.jar
COPY ./src /cont/src
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "/flightms.jar"]