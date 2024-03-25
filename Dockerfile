FROM adoptopenjdk/openjdk11:alpine-jre AS builder

ARG JAR_FILE=target/*-*.jar

# Copy Jar to container
COPY ${JAR_FILE} fullstack-coe-mgmt.jar

# Extract Layers of Jar
RUN  java -Djarmode=layertools -jar fullstack-coe-mgmt.jar extract

FROM adoptopenjdk/openjdk11:alpine-jre AS build

# Copy Layers form Builder Stage
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

EXPOSE 8099

ENTRYPOINT ["java","-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","org.springframework.boot.loader.JarLauncher"]
