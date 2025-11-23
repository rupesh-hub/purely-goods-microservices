#Build stage
FROM maven:3.8.7-openjdk-18 as build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /build/target/discovery-server-*.jar /app/

EXPOSE 8070
ARG APP_VERSION=1.0.0

ENV JAR_VERSION=${APP_VERSION}

CMD java -jar discovery-server-${JAR_VERSION}.jar
