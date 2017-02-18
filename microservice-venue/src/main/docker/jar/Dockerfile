FROM openjdk:8-jre-alpine

MAINTAINER agoncal

LABEL name="Venue Micro Service (jar)"
LABEL description="Venue Micro Service giving all the details of the rooms of the venue"
LABEL version=${project.version}
LABEL url="http://localhost:8086/conference-venue/api/rooms"
LABEL vendor="@agoncal"

EXPOSE 8086

COPY conference-venue-swarm.jar /opt/conference-venue-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/conference-venue-swarm.jar"]
