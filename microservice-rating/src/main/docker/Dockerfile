FROM jboss/wildfly:10.1.0.Final

MAINTAINER agoncal

LABEL name="Rating Micro Service"
LABEL description="Rating Micro Service allowing rating for a talk and gives all the details about the ratings"
LABEL version=${project.version}
LABEL url="http://localhost:8080/conference-rating/api/ratings"
LABEL vendor="@agoncal"

COPY ${project.build.finalName}.war $JBOSS_HOME/standalone/deployments/

