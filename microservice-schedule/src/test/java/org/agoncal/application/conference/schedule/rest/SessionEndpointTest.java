package org.agoncal.application.conference.schedule.rest;

import org.agoncal.application.conference.commons.rest.CORSFilterTest;
import org.agoncal.application.conference.schedule.domain.Room;
import org.agoncal.application.conference.schedule.domain.Session;
import org.agoncal.application.conference.schedule.domain.Speaker;
import org.agoncal.application.conference.schedule.domain.Talk;
import org.agoncal.application.conference.schedule.repository.SessionRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
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
public class SessionEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Speaker TEST_SPEAKER = new Speaker("idspeaker", "name");
    private static final Room TEST_ROOM = new Room("idroom");
    private static final Talk TEST_TALK = new Talk("idtalk", "title", "talkType", "track", TEST_SPEAKER);
    private static final Session TEST_SESSION = new Session(true, true, 12345L, "12345", 23456L, "23456", "monday", TEST_ROOM, TEST_TALK);
    private static String sessionId;

    // ======================================
    // =         Deployment methods         =
    // ======================================

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
            .importRuntimeDependencies().resolve().withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addPackage(Session.class.getPackage())
            .addClasses(SessionEndpoint.class, SessionRepository.class, Application.class)
            .addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsLibraries(files);
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldFailGetingSessionsWithZeroPage(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.queryParam("page", 0).request(APPLICATION_JSON_TYPE).get();
        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(2)
    public void shouldGetNoSessions(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(3)
    public void shouldFailCreatingInvalidSession(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(null);
        assertEquals(UNSUPPORTED_MEDIA_TYPE.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(4)
    public void shouldCreateSession(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_SESSION, APPLICATION_JSON_TYPE));
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        sessionId = getSpeakerId(response);
        checkHeaders(response);
    }

    @Test
    @InSequence(5)
    public void shouldGetAlreadyCreatedSessions(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(sessionId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 13 links", 13, jsonObject.getJsonObject("links").size());
        assertEquals(sessionId, jsonObject.getString("id"));
        assertTrue(jsonObject.getJsonObject("links").getString(SWAGGER).contains("swagger.json"));
        assertTrue(jsonObject.getJsonObject("links").getString(SELF).contains("/api/sessions/" + sessionId));
        assertTrue(jsonObject.getJsonObject("links").getString(COLLECTION).contains("/api/sessions"));
        assertTrue(jsonObject.getJsonObject("links").getString("vote").contains("/api/ratings"));
        assertEquals(TEST_SESSION.getNotAllocated(), jsonObject.getBoolean("notAllocated"));
        assertEquals(TEST_SESSION.getIsaBreak(), jsonObject.getBoolean("isaBreak"));
        assertEquals(TEST_SESSION.getFromTime(), jsonObject.getString("fromTime"));
        assertEquals(TEST_SESSION.getFromTimeMillis(), new Long(jsonObject.getInt("fromTimeMillis")));
        assertEquals(TEST_SESSION.getToTime(), jsonObject.getString("toTime"));
        assertEquals(TEST_SESSION.getToTimeMillis(), new Long(jsonObject.getInt("toTimeMillis")));
        assertEquals(TEST_SESSION.getDay(), jsonObject.getString("day"));
        assertEquals(TEST_ROOM.getId(), jsonObject.getJsonObject("room").getString("id"));
        assertEquals("Should have 1 link", 1, jsonObject.getJsonObject("room").getJsonObject("links").size());
        assertEquals(TEST_TALK.getId(), jsonObject.getJsonObject("talk").getString("id"));
        assertEquals(TEST_TALK.getTitle(), jsonObject.getJsonObject("talk").getString("title"));
        assertEquals(TEST_TALK.getTalkType(), jsonObject.getJsonObject("talk").getString("talkType"));
        assertEquals(TEST_TALK.getTrack(), jsonObject.getJsonObject("talk").getString("track"));
        assertEquals("Should have 1 link", 1, jsonObject.getJsonObject("talk").getJsonObject("links").size());
        assertEquals("Should have 1 speaker", 1, jsonObject.getJsonObject("talk").getJsonArray("speakers").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(6)
    public void shouldGetCreatedSessionWithEtag(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(sessionId).request(APPLICATION_JSON_TYPE).get();
        EntityTag etag = response.getEntityTag();
        assertNotNull(etag);
        assertEquals(OK.getStatusCode(), response.getStatus());
        response.close();
        Response response2 = webTarget.path(sessionId).request(APPLICATION_JSON_TYPE).header("If-None-Match", etag).get();
        assertNotNull(response2.getEntityTag());
        assertEquals(NOT_MODIFIED.getStatusCode(), response2.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(7)
    public void shouldCheckCollectionOfSessions(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 11 links", 11, jsonObject.getJsonObject("links").size());
        assertEquals("Should have 1 talk", 1, jsonObject.getJsonArray("data").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(8)
    public void shouldRemoveSession(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(sessionId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        Response checkResponse = webTarget.path(sessionId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(NOT_FOUND.getStatusCode(), checkResponse.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(9)
    public void shouldRemoveWithInvalidInput(@ArquillianResteasyResource("api/sessions") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).delete();
        assertEquals(METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private String getSpeakerId(Response response) {
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
