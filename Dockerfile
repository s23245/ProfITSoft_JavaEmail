FROM openjdk:17-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl
COPY build/libs/emailService-0.0.1-SNAPSHOT.jar app.jar
RUN ls -l /app
RUN java -jar /app.jar --version || true
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
