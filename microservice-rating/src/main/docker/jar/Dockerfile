FROM openjdk:8-jre-alpine

MAINTAINER agoncal

LABEL name="Rating Micro Service (jar)"
LABEL description="Rating Micro Service allowing rating for a talk and gives all the details about the ratings"
LABEL version=${project.version}
LABEL url="http://localhost:8082/conference-rating/api/ratings"
LABEL vendor="@agoncal"

EXPOSE 8082

COPY conference-rating-swarm.jar /opt/conference-rating-swarm.jar
ENTRYPOINT ["java", "-jar", "/opt/conference-rating-swarm.jar"]
