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

## References

* [Link Relations](http://www.iana.org/assignments/link-relations/link-relations.xml)
