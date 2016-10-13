# agoncal-application-conference

## Maven

* `mvn versions:display-plugin-updates` checks plugin versions
* `mvn swagger:generate` generates swagger.json file

## Docker

### Building Images

* `docker build -t agoncal/conference-venue -f src/docker/Dockerfile .`
* `mvn clean package -Pbuild-docker`

### Running Images

* `docker run -d --name=microservice-attendee -p 8081:8080 agoncal/microservice-attendee:latest`
* `docker run -d --name=microservice-rating -p 8082:8080 agoncal/microservice-rating:latest`
* `docker run -d --name=microservice-schedule -p 8083:8080 agoncal/microservice-schedule:latest`
* `docker run -d --name=microservice-speaker -p 8084:8080 agoncal/microservice-speaker:latest`
* `docker run -d --name=microservice-talk -p 8085:8080 agoncal/microservice-talk:latest`
* `docker run -d --name=microservice-venue -p 8086:8080 agoncal/microservice-venue:latest`

Go to the following URLs

* http://localhost:8081/conference-attendee/api/attendees
* http://localhost:8082/conference-rating/api/ratings
* http://localhost:8083/conference-schedule/api/sessions
* http://localhost:8084/conference-speaker/api/speakers
* http://localhost:8085/conference-talk/api/talks
* http://localhost:8086/conference-venue/api/rooms

## References

* [Link Relations](http://www.iana.org/assignments/link-relations/link-relations.xml)
