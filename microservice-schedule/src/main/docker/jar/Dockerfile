FROM openjdk:8-jre-alpine

MAINTAINER agoncal

LABEL name="Schedule Micro Service (jar)"
LABEL description="Schedule Micro Service giving all the details of the schedule"
LABEL version=${project.version}
LABEL url="http://localhost:8083/conference-schedule/api/sessions"
LABEL vendor="@agoncal"

EXPOSE 8083

COPY conference-schedule-swarm.jar /opt/conference-schedule-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/conference-schedule-swarm.jar"]
