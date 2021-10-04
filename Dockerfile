FROM maven:3.6.3-jdk-11-slim AS MAVEN_BUILD

COPY pom.xml /build/
COPY sonar-project.properties /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app
COPY src/main/resources/templates/mail/* /var/ssibk/

COPY --from=MAVEN_BUILD /build/target/company-controller-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "company-controller-0.0.1-SNAPSHOT.jar"]
