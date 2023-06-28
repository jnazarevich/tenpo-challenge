FROM maven:3.8.7-eclipse-temurin-19 as builder

WORKDIR /project
COPY pom.xml ./
ADD src ./src
RUN mvn clean install

FROM eclipse-temurin:19.0.1_10-jdk

WORKDIR /app
COPY --from=builder project/target/app.jar ./app.jar

ENTRYPOINT [ "sh", "-c", "java -Xmx768m -Xms256m -jar app.jar" ]