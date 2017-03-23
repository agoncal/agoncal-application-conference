package org.agoncal.application.conference.venue.rest;

import org.agoncal.application.conference.commons.rest.CORSFilterTest;
import org.agoncal.application.conference.venue.domain.Room;
import org.agoncal.application.conference.venue.domain.Rooms;
import org.agoncal.application.conference.venue.repository.RoomRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.*;
import static org.agoncal.application.conference.commons.domain.Links.COLLECTION;
import static org.agoncal.application.conference.commons.domain.Links.SELF;
import static org.agoncal.application.conference.commons.domain.Links.SWAGGER;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@RunAsClient
public class RoomEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Room TEST_ROOM = new Room("name", 123, "setup");
    private static String roomId;

    // ======================================
    // =         Deployment methods         =
    // ======================================

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
            .importRuntimeDependencies().resolve().withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addClasses(Room.class, Rooms.class, RoomEndpoint.class, RoomRepository.class, Application.class)
            .addAsLibraries(files);
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldFailCreatingInvalidRoom(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(null);
        assertEquals(UNSUPPORTED_MEDIA_TYPE.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(2)
    public void shouldCreateARoom(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_ROOM, APPLICATION_JSON_TYPE));
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        roomId = getRoomId(response);
        TEST_ROOM.setId(roomId);
        checkHeaders(response);
    }

    @Test
    @InSequence(3)
    public void shouldGetAlreadyCreatedRoom(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(roomId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 5 attributes", 5, jsonObject.size());
        assertEquals(roomId, jsonObject.getString("id"));
        assertEquals("Should have 3 links", 3, jsonObject.getJsonObject("links").size());
        assertTrue(jsonObject.getJsonObject("links").getString(SWAGGER).contains("swagger.json"));
        assertTrue(jsonObject.getJsonObject("links").getString(SELF).contains("/api/rooms/" + roomId));
        assertTrue(jsonObject.getJsonObject("links").getString(COLLECTION).contains("/api/rooms"));
        assertEquals(TEST_ROOM.getName(), jsonObject.getString("name"));
        assertEquals(TEST_ROOM.getCapacity(), new Integer(jsonObject.getInt("capacity")));
        assertEquals(TEST_ROOM.getSetup(), jsonObject.getString("setup"));
        checkHeaders(response);
    }

    @Test
    @InSequence(4)
    public void shouldGetCreatedRoomWithEtag(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(roomId).request(APPLICATION_JSON_TYPE).get();
        EntityTag etag = response.getEntityTag();
        assertNotNull(etag);
        assertEquals(OK.getStatusCode(), response.getStatus());
        response.close();
        Response response2 = webTarget.path(roomId).request(APPLICATION_JSON_TYPE).header("If-None-Match", etag).get();
        assertNotNull(response2.getEntityTag());
        assertEquals(NOT_MODIFIED.getStatusCode(), response2.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(5)
    public void shouldUpdateCreatedRoom(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        TEST_ROOM.setName("updated name");
        Response response = webTarget.request(APPLICATION_JSON_TYPE).put(Entity.entity(TEST_ROOM, APPLICATION_JSON_TYPE));
        assertEquals(OK.getStatusCode(), response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(roomId, jsonObject.getString("id"));
        assertEquals(TEST_ROOM.getName(), jsonObject.getString("name"));
        assertEquals(3, jsonObject.getJsonObject("links").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(7)
    public void shouldCheckCollectionOfRooms(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 1 link", 1, jsonObject.getJsonObject("links").size());
        assertEquals("Should have 11 rooms", 11, jsonObject.getJsonArray("data").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(6)
    public void shouldRemoveRoom(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(roomId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        Response checkResponse = webTarget.path(roomId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(NOT_FOUND.getStatusCode(), checkResponse.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(7)
    public void shouldRemoveWithInvalidInput(@ArquillianResteasyResource("api/rooms") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).delete();
        assertEquals(METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
        checkHeaders(response);
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

    private void checkHeaders(Response response) {
        CORSFilterTest.checkCORSHeaders(response);
    }
}
