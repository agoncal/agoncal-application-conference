FROM jboss/wildfly:10.1.0.Final

MAINTAINER agoncal

LABEL name="Talk Micro Service"
LABEL description="Talk Micro Service giving all the talks"
LABEL version=${project.version}
LABEL url="http://localhost:8080/conference-talk/api/talks"
LABEL vendor="@agoncal"

COPY ${project.build.finalName}.war $JBOSS_HOME/standalone/deployments/

