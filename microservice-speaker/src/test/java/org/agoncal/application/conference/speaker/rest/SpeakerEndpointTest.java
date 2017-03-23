package org.agoncal.application.conference.speaker.rest;

import org.agoncal.application.conference.commons.rest.CORSFilterTest;
import org.agoncal.application.conference.speaker.domain.AcceptedTalk;
import org.agoncal.application.conference.speaker.domain.Speaker;
import org.agoncal.application.conference.speaker.domain.Speakers;
import org.agoncal.application.conference.speaker.repository.SpeakerRepository;
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
import java.util.Arrays;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.*;
import static org.agoncal.application.conference.commons.domain.Links.*;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@RunAsClient
public class SpeakerEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Speaker TEST_SPEAKER = new Speaker("last name", "first name", "bio", "en", "twitter", "avatar url", "company", "blog");
    private static final AcceptedTalk TEST_ACCEPTED_TALK = new AcceptedTalk("id", "title", "en");
    private static String speakerId;

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
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldFailGetingSpeakersWithZeroPage(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        Response response = webTarget.queryParam("page", 0).request(APPLICATION_JSON_TYPE).get();
        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(2)
    public void shouldGetNoSpeakers(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(3)
    public void shouldFailCreatingInvalidSpeaker(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(null);
        assertEquals(UNSUPPORTED_MEDIA_TYPE.getStatusCode(), response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(4)
    public void shouldCreateSpeaker(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        TEST_SPEAKER.setAcceptedTalks(Arrays.asList(TEST_ACCEPTED_TALK));
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_SPEAKER, APPLICATION_JSON_TYPE));
        assertEquals(CREATED.getStatusCode(), response.getStatus());
        speakerId = getSpeakerId(response);
        checkHeaders(response);
    }

    @Test
    @InSequence(5)
    public void shouldGetAlreadyCreatedSpeaker(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 11 attributes", 11, jsonObject.size());
        assertEquals(speakerId, jsonObject.getString("id"));
        assertEquals("Should have 4 links", 4, jsonObject.getJsonObject("links").size());
        assertTrue(jsonObject.getJsonObject("links").getString(SWAGGER).contains("swagger.json"));
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
    public void shouldGetCreatedSpeakerWithEtag(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
        EntityTag etag = response.getEntityTag();
        assertNotNull(etag);
        assertEquals(OK.getStatusCode(), response.getStatus());
        response.close();
        Response response2 = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).header("If-None-Match", etag).get();
        assertNotNull(response2.getEntityTag());
        assertEquals(NOT_MODIFIED.getStatusCode(), response2.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(7)
    public void shouldCheckCollectionOfSpeakers(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(OK.getStatusCode(), response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 6 links", 6, jsonObject.getJsonObject("links").size());
        assertEquals("Should have 1 talk", 1, jsonObject.getJsonArray("data").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(8)
    public void shouldRemoveSpeaker(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());
        Response checkResponse = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(NOT_FOUND.getStatusCode(), checkResponse.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(9)
    public void shouldRemoveWithInvalidInput(@ArquillianResteasyResource("api/speakers") WebTarget webTarget) throws Exception {
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
