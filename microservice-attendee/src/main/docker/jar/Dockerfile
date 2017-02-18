FROM openjdk:8-jre-alpine

MAINTAINER agoncal

LABEL name="Attendees Micro Service (jar)"
LABEL description="Attendees Micro Service giving all the details of the attendees"
LABEL version=${project.version}
LABEL url="http://localhost:8081/conference-attendee/api/attendees"
LABEL vendor="@agoncal"

EXPOSE 8081

COPY conference-attendee-swarm.jar /opt/conference-attendee-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/conference-attendee-swarm.jar"]
