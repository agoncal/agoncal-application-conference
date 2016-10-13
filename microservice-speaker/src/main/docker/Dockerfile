FROM jboss/wildfly:10.1.0.Final

MAINTAINER agoncal

LABEL name="Speaker Micro Service"
LABEL description="Speaker Micro Service giving all the speakers"
LABEL version=${project.version}
LABEL url="http://localhost:8080/conference-speaker/api/speakers"
LABEL vendor="@agoncal"

COPY ${project.build.finalName}.war $JBOSS_HOME/standalone/deployments/

