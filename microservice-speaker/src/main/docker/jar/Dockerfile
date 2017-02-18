FROM openjdk:8-jre-alpine

MAINTAINER agoncal

LABEL name="Speaker Micro Service (jar)"
LABEL description="Speaker Micro Service giving all the speakers"
LABEL version=${project.version}
LABEL url="http://localhost:8084/conference-speaker/api/speakers"
LABEL vendor="@agoncal"

EXPOSE 8084

COPY conference-speaker-swarm.jar /opt/conference-speaker-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/conference-speaker-swarm.jar"]
