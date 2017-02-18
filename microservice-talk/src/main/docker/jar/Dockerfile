FROM openjdk:8-jre-alpine

MAINTAINER agoncal

LABEL name="Talk Micro Service (jar)"
LABEL description="Talk Micro Service giving all the talks"
LABEL version=${project.version}
LABEL url="http://localhost:8085/conference-talk/api/talks"
LABEL vendor="@agoncal"

EXPOSE 8085

COPY conference-talk-swarm.jar /opt/conference-talk-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/conference-talk-swarm.jar"]
