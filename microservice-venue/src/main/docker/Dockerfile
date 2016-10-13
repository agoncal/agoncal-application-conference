FROM jboss/wildfly:10.1.0.Final

MAINTAINER agoncal

LABEL name="Venue Micro Service"
LABEL description="Venue Micro Service giving all the details of the rooms of the venue"
LABEL version=${project.version}
LABEL url="http://localhost:8080/conference-venue/api/rooms"
LABEL vendor="@agoncal"

COPY ${project.build.finalName}.war $JBOSS_HOME/standalone/deployments/

