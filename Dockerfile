FROM openjdk

WORKDIR /app

COPY target/termo-0.0.1-SNAPSHOT.jar /app/termo.jar

ENTRYPOINT ["java", "-jar", "termo.jar"]