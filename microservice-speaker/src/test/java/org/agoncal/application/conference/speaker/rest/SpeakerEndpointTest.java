package org.agoncal.application.conference.speaker.rest;

import org.agoncal.application.conference.speaker.domain.AcceptedTalk;
import org.agoncal.application.conference.speaker.domain.Speaker;
import org.agoncal.application.conference.speaker.domain.Speakers;
import org.agoncal.application.conference.speaker.repository.SpeakerRepository;
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
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.Arrays;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.agoncal.application.conference.commons.domain.Links.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@RunAsClient
public class SpeakerEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Speaker TEST_SPEAKER = new Speaker("last name", "first name", "bio", "en", "twitter", "avatar url", "company", "blog");
    private static final AcceptedTalk TEST_ACCEPTED_TALK = new AcceptedTalk("id", "title", "en");
    private static String speakerId;
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
            .addClasses(Speaker.class, Speakers.class, AcceptedTalk.class, SpeakerEndpoint.class, SpeakerRepository.class, Application.class)
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
        webTarget = client.target(baseURL).path("api/speakers");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldFailGetingSpeakersWithZeroPage() throws Exception {
        Response response = webTarget.queryParam("page", 0).request(APPLICATION_JSON_TYPE).get();
        assertEquals(400, response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(2)
    public void shouldGetNoSpeakers() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(404, response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(3)
    public void shouldFailCreatingInvalidSpeaker() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(null, APPLICATION_JSON_TYPE));
        assertEquals(400, response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(4)
    public void shouldCreateSpeaker() throws Exception {
        TEST_SPEAKER.setAcceptedTalks(Arrays.asList(TEST_ACCEPTED_TALK));
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_SPEAKER, APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
        speakerId = getSpeakerId(response);
        checkHeaders(response);
    }

    @Test
    @InSequence(5)
    public void shouldGetAlreadyCreatedSpeaker() throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 11 attributes", 11, jsonObject.size());
        assertEquals(speakerId, jsonObject.getString("id"));
        assertEquals("Should have 3 links", 3, jsonObject.getJsonObject("links").size());
        assertTrue(jsonObject.getJsonObject("links").getString(SELF).contains("/api/speakers/" + speakerId));
        assertTrue(jsonObject.getJsonObject("links").getString(COLLECTION).contains("/api/speakers"));
        assertTrue(jsonObject.getJsonObject("links").getString(SUMMARY).contains("/api/speakers"));
        assertEquals(TEST_SPEAKER.getLastName(), jsonObject.getString("lastName"));
        assertEquals(TEST_SPEAKER.getFirstName(), jsonObject.getString("firstName"));
        assertEquals(TEST_SPEAKER.getBio(), jsonObject.getString("bio"));
        assertEquals(TEST_SPEAKER.getLanguage(), jsonObject.getString("language"));
        assertEquals(TEST_SPEAKER.getTwitter(), jsonObject.getString("twitter"));
        assertEquals(TEST_SPEAKER.getAvatarUrl(), jsonObject.getString("avatarUrl"));
        assertEquals(TEST_SPEAKER.getCompany(), jsonObject.getString("company"));
        assertEquals(TEST_SPEAKER.getBlog(), jsonObject.getString("blog"));
        assertEquals("Should have 1 talk", 1, jsonObject.getJsonArray("acceptedTalks").size());
        assertEquals(TEST_ACCEPTED_TALK.getId(), jsonObject.getJsonArray("acceptedTalks").getJsonObject(0).getString("id"));
        assertEquals(TEST_ACCEPTED_TALK.getTitle(), jsonObject.getJsonArray("acceptedTalks").getJsonObject(0).getString("title"));
        assertEquals(TEST_ACCEPTED_TALK.getLanguage(), jsonObject.getJsonArray("acceptedTalks").getJsonObject(0).getString("language"));
        assertEquals("Should have 1 link", 1, jsonObject.getJsonArray("acceptedTalks").getJsonObject(0).getJsonObject("links").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(6)
    public void shouldGetCreatedSpeakerWithEtag() throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
        EntityTag etag = response.getEntityTag();
        assertNotNull(etag);
        assertEquals(200, response.getStatus());
        response.close();
        Response response2 = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).header("If-None-Match", etag).get();
        assertNotNull(response2.getEntityTag());
        assertEquals(304, response2.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(7)
    public void shouldCheckCollectionOfSpeakers() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 5 links", 5, jsonObject.getJsonObject("links").size());
        assertEquals("Should have 1 talk", 1, jsonObject.getJsonArray("data").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(8)
    public void shouldRemoveSpeaker() throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(204, response.getStatus());
        Response checkResponse = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(9)
    public void shouldRemoveWithInvalidInput() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).delete();
        assertEquals(405, response.getStatus());
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
        // TODO FIXME
        // assertEquals("[*]", response.getHeaders().get("Access-Control-Allow-Origin").toString());
        // assertEquals("[origin, content-type, accept, authorization]", response.getHeaders().get("Access-Control-Allow-Headers").toString());
        // assertEquals("[true]", response.getHeaders().get("Access-Control-Allow-Credentials").toString());
        // assertEquals("[GET, POST, PUT, DELETE, OPTIONS, HEAD]", response.getHeaders().get("Access-Control-Allow-Methods").toString());
        // assertEquals("[1209600]", response.getHeaders().get("Access-Control-Max-Age").toString());
    }
}
