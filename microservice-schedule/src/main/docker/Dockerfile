FROM jboss/wildfly:10.1.0.Final

MAINTAINER agoncal

LABEL name="Schedule Micro Service"
LABEL description="Schedule Micro Service giving all the details of the schedule"
LABEL version=${project.version}
LABEL url="http://localhost:8080/conference-schedule/api/sessions"
LABEL vendor="@agoncal"

COPY ${project.build.finalName}.war $JBOSS_HOME/standalone/deployments/

