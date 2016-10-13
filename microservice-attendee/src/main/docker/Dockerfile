FROM jboss/wildfly:10.1.0.Final

MAINTAINER agoncal

LABEL name="Attendees Micro Service"
LABEL description="Attendees Micro Service giving all the details of the attendees"
LABEL version=${project.version}
LABEL url="http://localhost:8080/conference-attendee/api/attendees"
LABEL vendor="@agoncal"

COPY ${project.build.finalName}.war $JBOSS_HOME/standalone/deployments/

