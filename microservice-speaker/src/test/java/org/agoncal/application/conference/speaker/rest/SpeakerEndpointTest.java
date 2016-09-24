package org.agoncal.application.conference.speaker.rest;

import org.agoncal.application.conference.speaker.domain.AcceptedTalk;
import org.agoncal.application.conference.speaker.domain.Speaker;
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
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.URI;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.agoncal.application.conference.commons.domain.Links.COLLECTION;
import static org.agoncal.application.conference.commons.domain.Links.SELF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@RunAsClient
public class SpeakerEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Speaker TEST_SPEAKER = new Speaker("id", "last name");
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
            .addClasses(Speaker.class, AcceptedTalk.class, SpeakerEndpoint.class, SpeakerRepository.class, Application.class)
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
    public void shouldGetAllSpeakers() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
    }


    @Test
    @InSequence(2)
    public void shouldCreateSpeaker() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_SPEAKER, APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
        speakerId = getSpeakerId(response);
    }

    @Test
    @InSequence(3)
    public void shouldGetAlreadyCreatedRoom() throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(speakerId, jsonObject.getString("id"));
        assertEquals("Should have 2 links", 2, jsonObject.getJsonObject("links").size());
        assertTrue(jsonObject.getJsonObject("links").getString(SELF).contains("/api/speakers/" + speakerId));
        assertTrue(jsonObject.getJsonObject("links").getString(COLLECTION).contains("/api/speakers"));
        assertEquals(TEST_SPEAKER.getLastName(), jsonObject.getString("lastName"));
    }

    @Test
    @InSequence(4)
    public void shouldRemoveRoom() throws Exception {
        Response response = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(204, response.getStatus());
        Response checkResponse = webTarget.path(speakerId).request(APPLICATION_JSON_TYPE).get();
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
