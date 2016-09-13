package org.agoncal.application.conference.venue.resources;

import org.agoncal.application.conference.venue.model.Room;
import org.agoncal.application.conference.venue.persistence.RoomDAO;
import org.agoncal.application.conference.venue.rest.Application;
import org.agoncal.application.conference.venue.rest.RoomEndpoint;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class RoomEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Room TEST_ROOM = new Room("Metroxx", "Metropolis");

    private static String scheduleId;

    // ======================================
    // =          Injection Points          =
    // ======================================

    @ArquillianResource
    private URL base;

    // ======================================
    // =         Deployment methods         =
    // ======================================

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
            .importRuntimeDependencies().resolve().withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addClasses(Room.class, RoomEndpoint.class, RoomDAO.class, Application.class)
            .addAsLibraries(files);
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldCreateRoom() throws Exception {
        Response response = createScheduledSession(TEST_ROOM);
        assertEquals(201, response.getStatus());
        scheduleId = getRoomId(response);
    }

    @Test
    @InSequence(2)
    public void shouldGetAlreadyCreatedRoom() throws Exception {
        URL url = new URL(base, "rooms/" + scheduleId);
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(scheduleId, jsonObject.getInt("id"));
        assertEquals(TEST_ROOM.getName(), jsonObject.getJsonObject("venue").getString("name"));
    }

    @Test
    @InSequence(6)
    public void shouldRemoveRoom() throws Exception {
        URL url = new URL(base, "rooms/" + scheduleId);
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response deleteResponse = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
        assertEquals(204, deleteResponse.getStatus());

        URL checkUrl = new URL(base, "rooms/" + scheduleId);
        WebTarget checkTarget = ClientBuilder.newClient().target(checkUrl.toExternalForm());
        Response checkResponse = checkTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private String getRoomId(Response response) {
        String location = response.getHeaderString("location");
        return location.substring(location.lastIndexOf("/") + 1);
    }

    private static JsonObject readJsonContent(Response response) {
        JsonReader jsonReader = readJsonStringFromResponse(response);
        return jsonReader.readObject();
    }

    private static JsonReader readJsonStringFromResponse(Response response) {
        String competitionJson = response.readEntity(String.class);
        StringReader stringReader = new StringReader(competitionJson);
        return Json.createReader(stringReader);
    }

    private Response createScheduledSession(Room room) throws MalformedURLException {
        URL url = new URL(base, "rooms");
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        return target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(room, MediaType.APPLICATION_JSON_TYPE));
    }
}
