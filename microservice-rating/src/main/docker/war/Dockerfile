FROM jboss/wildfly:10.1.0.Final

MAINTAINER agoncal

LABEL name="Rating Micro Service (war)"
LABEL description="Rating Micro Service allowing rating for a talk and gives all the details about the ratings"
LABEL version=${project.version}
LABEL url="http://localhost:8080/conference-rating/api/ratings"
LABEL vendor="@agoncal"

# Setting the Wildfly Admin consule (user/pwd admin/admin)
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

COPY ${project.build.finalName}.war $JBOSS_HOME/standalone/deployments/

