package org.agoncal.application.conference.tests;

import org.agoncal.application.conference.attendee.domain.Attendee;
import org.agoncal.application.conference.attendee.domain.Attendees;
import org.agoncal.application.conference.attendee.repository.AttendeeRepository;
import org.agoncal.application.conference.attendee.rest.Application;
import org.agoncal.application.conference.attendee.rest.AttendeeEndpoint;
import org.agoncal.application.conference.commons.rest.CORSFilterTest;
import org.agoncal.application.conference.rating.domain.Rating;
import org.agoncal.application.conference.rating.domain.Ratings;
import org.agoncal.application.conference.rating.repository.RatingRepository;
import org.agoncal.application.conference.rating.rest.RatingEndpoint;
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
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@RunAsClient
public class VotingTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Attendee TEST_ATTENDEE = new Attendee("id", "last name", "first name", "login", "password");
    private static final Rating TEST_RATING = new Rating("sessionId", "attendeeId", 5);

    private static String token;
    private static String attendeeId;
    private static String ratingId;

    private Client client;
    private WebTarget attendeesWebTarget;
    private WebTarget ratingsWebTarget;

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

        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addClasses(Attendee.class, Attendees.class, AttendeeEndpoint.class, AttendeeRepository.class, Application.class)
            .addClasses(Rating.class, Ratings.class, RatingEndpoint.class, RatingRepository.class)
            .addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsLibraries(removeDependencies(files));

        System.out.println("##################");
        System.out.println(war.toString(true));
        return war;
    }

    // ======================================
    // =          Lifecycle methods         =
    // ======================================

    @Before
    public void initWebTarget() {
        client = ClientBuilder.newClient();
        attendeesWebTarget = client.target(baseURL).path("api/attendees");
        ratingsWebTarget = client.target(baseURL).path("api/ratings");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldCreateAttendee() throws Exception {
        Response response = attendeesWebTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_ATTENDEE, APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
        attendeeId = getLocationId(response);
        checkHeaders(response);
    }

    @Test
    @InSequence(2)
    public void shouldGetAlreadyCreatedAttendee() throws Exception {
        Response response = attendeesWebTarget.path(attendeeId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(attendeeId, jsonObject.getString("id"));
        assertEquals(TEST_ATTENDEE.getLastName(), jsonObject.getString("lastName"));
        checkHeaders(response);
    }

    @Test
    @InSequence(3)
    public void shouldLogUserIn() throws Exception {
        Form form = new Form();
        form.param("login", TEST_ATTENDEE.getLogin());
        form.param("password", TEST_ATTENDEE.getPassword());

        Response response = attendeesWebTarget.path("login").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertEquals(200, response.getStatus());
        assertNotNull(response.getHeaderString(HttpHeaders.AUTHORIZATION));
        token = response.getHeaderString(HttpHeaders.AUTHORIZATION);
        assertTrue(token.startsWith("Bearer"));
        checkHeaders(response);
    }

    @Test
    @InSequence(4)
    public void shouldFailTryingToRateASessionWithNoToken() throws Exception {
        Form form = new Form();
        form.param("mark", TEST_RATING.getMark().toString());

        Response response = ratingsWebTarget.path(TEST_RATING.getSessionId()).request(APPLICATION_JSON_TYPE).post(Entity.entity(form, APPLICATION_FORM_URLENCODED));
        assertEquals(401, response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(5)
    public void shouldRateASessionWithToken() throws Exception {
        Form form = new Form();
        form.param("mark", TEST_RATING.getMark().toString());

        Response response = ratingsWebTarget.path(TEST_RATING.getSessionId()).request(APPLICATION_JSON_TYPE).header(HttpHeaders.AUTHORIZATION, token).post(Entity.entity(form, APPLICATION_FORM_URLENCODED));
        assertEquals(201, response.getStatus());
        ratingId = getLocationId(response);
        checkHeaders(response);
    }

    @Test
    @InSequence(6)
    public void shouldGetAlreadyCreatedRating() throws Exception {
        Response response = ratingsWebTarget.path(ratingId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(ratingId, jsonObject.getString("id"));
        assertEquals(TEST_RATING.getMark(), new Integer(jsonObject.getInt("mark")));
        checkHeaders(response);
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private String getLocationId(Response response) {
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
        assertNotNull(response.getHeaders().get("Host"));
    }

    private static File[] removeDependencies(File[] files) {
        List<File> dependencies = Arrays.asList(files);
        int i = 0;
        File[] filteredDependencies = new File[files.length - 2];
        for (File file : dependencies) {
            if (!file.getName().contains("microservice-")) {
                filteredDependencies[i] = file;
                i++;
            }
        }
        return filteredDependencies;
    }
}
