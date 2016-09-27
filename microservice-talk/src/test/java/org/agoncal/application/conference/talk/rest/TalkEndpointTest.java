package org.agoncal.application.conference.talk.rest;

import org.agoncal.application.conference.talk.domain.Speaker;
import org.agoncal.application.conference.talk.domain.Talk;
import org.agoncal.application.conference.talk.domain.Talks;
import org.agoncal.application.conference.talk.repository.TalkRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.Arrays;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.agoncal.application.conference.commons.domain.Links.COLLECTION;
import static org.agoncal.application.conference.commons.domain.Links.SELF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@RunAsClient
public class TalkEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Talk TEST_TALK = new Talk("title", "language", "talk type", "track", "summary");
    private static final Speaker TEST_SPEAKER = new Speaker("id", "name");
    private static String talkId;
    private Client client;
    private WebTarget webTarget;

    // ======================================
    // =          Injection Points          =
    // ======================================

    @ArquillianResource
    private URI baseURL;

    // ======================================
    // =         Deployment methods         =
    // ======================================

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
            .importRuntimeDependencies().resolve().withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addClasses(Talk.class, Talks.class, Speaker.class, TalkEndpoint.class, TalkRepository.class, Application.class)
            .addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsLibraries(files);
    }

    // ======================================
    // =          Lifecycle methods         =
    // ======================================

    @Before
    public void initWebTarget() {
        client = ClientBuilder.newClient();
        webTarget = client.target(baseURL).path("api/talks");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldGetAllTalks() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 5 links", 5, jsonObject.getJsonObject("links").size());
        assertEquals("Should have 0 talk", 0, jsonObject.getJsonArray("data").size());
    }

    @Test
    @InSequence(2)
    public void shouldCreateTalk() throws Exception {
        TEST_TALK.setSpeakers(Arrays.asList(TEST_SPEAKER));
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_TALK, APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
        talkId = getSpeakerId(response);
    }

    @Test
    @InSequence(3)
    public void shouldGetAlreadyCreatedTalk() throws Exception {
        Response response = webTarget.path(talkId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 8 attributes", 8, jsonObject.size());
        assertEquals(talkId, jsonObject.getString("id"));
        assertEquals("Should have 2 links", 2, jsonObject.getJsonObject("links").size());
        assertTrue(jsonObject.getJsonObject("links").getString(SELF).contains("/api/talks/" + talkId));
        assertTrue(jsonObject.getJsonObject("links").getString(COLLECTION).contains("/api/talks"));
        assertEquals(TEST_TALK.getTitle(), jsonObject.getString("title"));
        assertEquals(TEST_TALK.getLanguage(), jsonObject.getString("language"));
        assertEquals(TEST_TALK.getTalkType(), jsonObject.getString("talkType"));
        assertEquals(TEST_TALK.getTrack(), jsonObject.getString("track"));
        assertEquals(TEST_TALK.getSummary(), jsonObject.getString("summary"));
        assertEquals("Should have 1 speaker", 1, jsonObject.getJsonArray("speakers").size());
        assertEquals(TEST_SPEAKER.getId(), jsonObject.getJsonArray("speakers").getJsonObject(0).getString("id"));
        assertEquals(TEST_SPEAKER.getName(), jsonObject.getJsonArray("speakers").getJsonObject(0).getString("name"));
        assertEquals("Should have 1 link", 1, jsonObject.getJsonArray("speakers").getJsonObject(0).getJsonObject("links").size());
    }

    @Test
    @InSequence(4)
    public void shouldCheckCollectionOfTalks() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 5 links", 5, jsonObject.getJsonObject("links").size());
        assertEquals("Should have 1 talk", 1, jsonObject.getJsonArray("data").size());
    }

    @Test
    @InSequence(5)
    public void shouldRemoveTalk() throws Exception {
        Response response = webTarget.path(talkId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(204, response.getStatus());
        Response checkResponse = webTarget.path(talkId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
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
}
