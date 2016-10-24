# agoncal-application-conference

## Maven

* `mvn versions:display-plugin-updates` checks plugin versions
* `mvn swagger:generate` generates swagger.json file
* `/bin/bash -c 'mvn -f microservice-attendee/pom.xml swagger:generate && mvn -f microservice-rating/pom.xml swagger:generate && mvn -f microservice-schedule/pom.xml swagger:generate && mvn -f microservice-speaker/pom.xml swagger:generate && mvn -f microservice-talk/pom.xml swagger:generate && mvn -f microservice-venue/pom.xml swagger:generate'`

## Docker

* `/bin/bash -c 'docker rm -fv $(docker ps -aq)'`
* `/bin/bash -c 'docker rmi -f $(docker images -q --filter dangling=true)'`
* `/bin/bash -c 'docker rmi -f $(docker images "agoncal/microservice*" -q)'`
* `/bin/bash -c 'docker push agoncal/microservice-attendee && docker push agoncal/microservice-rating && docker push agoncal/microservice-schedule && docker push agoncal/microservice-speaker && docker push agoncal/microservice-talk && docker push agoncal/microservice-venue'`

### Building Images

* `docker build -t agoncal/conference-venue -f src/docker/Dockerfile .`
* `mvn clean package -Pdocker`

### Running Images

* `docker run -d --name=microservice-attendee -p 8081:8080 -p 9991:9990 agoncal/microservice-attendee:latest`
* `docker run -d --name=microservice-rating   -p 8082:8080 -p 9992:9990 agoncal/microservice-rating:latest`
* `docker run -d --name=microservice-schedule -p 8083:8080 -p 9993:9990 agoncal/microservice-schedule:latest`
* `docker run -d --name=microservice-speaker  -p 8084:8080 -p 9994:9990 agoncal/microservice-speaker:latest`
* `docker run -d --name=microservice-talk     -p 8085:8080 -p 9995:9990 agoncal/microservice-talk:latest`
* `docker run -d --name=microservice-venue    -p 8086:8080 -p 9996:9990 agoncal/microservice-venue:latest`

Go to the following URLs to test the APIs

* http://localhost:8081/conference-attendee/api/attendees
* http://localhost:8082/conference-rating/api/ratings
* http://localhost:8083/conference-schedule/api/sessions
* http://localhost:8084/conference-speaker/api/speakers
* http://localhost:8085/conference-talk/api/talks
* http://localhost:8086/conference-venue/api/rooms

Go to the following URLs to test the swagger.json

* http://localhost:8081/conference-attendee/swagger.json
* http://localhost:8082/conference-rating/swagger.json
* http://localhost:8083/conference-schedule/swagger.json
* http://localhost:8084/conference-speaker/swagger.json
* http://localhost:8085/conference-talk/swagger.json
* http://localhost:8086/conference-venue/swagger.json

## Docker Compose

* `docker-compose up`
* `docker-compose up -d` (in detached mode)
* `docker-compose down`
* `docker-compose ps`
* `docker-compose scale ms-attendee=5`

Go to the following URLs to test the APIs

* http://conference.docker.localhost:8080/dashboard/ (Traeffik console)
* http://conference.docker.localhost:90/conference-attendee/api/attendees
* http://conference.docker.localhost:90/conference-rating/api/ratings
* http://conference.docker.localhost:90/conference-schedule/api/sessions
* http://conference.docker.localhost:90/conference-speaker/api/speakers
* http://conference.docker.localhost:90/conference-talk/api/talks
* http://conference.docker.localhost:90/conference-venue/api/rooms


## cURL

* `curl http://localhost:8080/conference-attendee/api/attendees -H 'Accept: application/json'` 
* `curl -X POST http://localhost:8080/conference-attendee/api/attendees/login -H 'Content-Type: application/x-www-form-urlencoded' -d 'login=agoncal&password=agoncal'`
* `curl -X POST http://localhost:8080/conference-rating/api/ratings/uni_room9_tuesday_8_9h30_12h30 -H 'Content-Type: application/x-www-form-urlencoded' -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZ29uY2FsIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2NvbmZlcmVuY2UtYXR0ZW5kZWUvYXBpL2F0dGVuZGVlcy9sb2dpbiIsImlhdCI6MTQ3NzIzNTYwNiwiZXhwIjoxNDc3MjM2NTA2fQ.GxV4IHsx99ln-hWdGaPMqAjkNNdvpl3DvykdZqbQUv0wqLxnto2G11BBMw-2UyNMA4T84UFpYVeMOxYcp-EEQg' -d 'mark=4'`

## References

* [Link Relations](http://www.iana.org/assignments/link-relations/link-relations.xml)
