package org.agoncal.application.conference.rating.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.agoncal.application.conference.commons.jwt.SimpleKeyGenerator;
import org.agoncal.application.conference.commons.rest.CORSFilterTest;
import org.agoncal.application.conference.rating.domain.Rating;
import org.agoncal.application.conference.rating.domain.Ratings;
import org.agoncal.application.conference.rating.repository.RatingRepository;
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
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.agoncal.application.conference.commons.domain.Links.COLLECTION;
import static org.agoncal.application.conference.commons.domain.Links.SELF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@RunAsClient
public class RatingEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Rating TEST_RATING = new Rating("sessionId", "attendeeId", 5);
    private static String ratingId;
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
            .addClasses(Rating.class, Ratings.class)
            .addClasses(RatingEndpoint.class, RatingRepository.class, Application.class)
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
        webTarget = client.target(baseURL).path("api/ratings");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void shouldFailGetingRatingsWithZeroPage() throws Exception {
        Response response = webTarget.queryParam("page", 0).request(APPLICATION_JSON_TYPE).get();
        assertEquals(400, response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(2)
    public void shouldGetNoRatings() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(404, response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(3)
    public void shouldFailTryingToRateASessionWithNoToken() throws Exception {
        Form form = new Form();
        form.param("mark", TEST_RATING.getMark().toString());

        Response response = webTarget.path(TEST_RATING.getSessionId()).request(APPLICATION_JSON_TYPE).post(Entity.entity(form, APPLICATION_FORM_URLENCODED));
        assertEquals(401, response.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(4)
    public void shouldRateASessionWithToken() throws Exception {
        Form form = new Form();
        form.param("mark", TEST_RATING.getMark().toString());

        Response response = webTarget.path(TEST_RATING.getSessionId()).request(APPLICATION_JSON_TYPE).header(HttpHeaders.AUTHORIZATION, issueToken()).post(Entity.entity(form, APPLICATION_FORM_URLENCODED));
        assertEquals(201, response.getStatus());
        ratingId = getRatingId(response);
        checkHeaders(response);
    }

    @Test
    @InSequence(5)
    public void shouldGetAlreadyCreatedRating() throws Exception {
        Response response = webTarget.path(ratingId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(ratingId, jsonObject.getString("id"));
        assertEquals("Should have 2 links", 2, jsonObject.getJsonObject("links").size());
        assertTrue(jsonObject.getJsonObject("links").getString(SELF).contains("/api/ratings/" + ratingId));
        assertTrue(jsonObject.getJsonObject("links").getString(COLLECTION).contains("/api/ratings"));
        assertEquals(TEST_RATING.getMark(), new Integer(jsonObject.getInt("mark")));
        checkHeaders(response);
    }

    @Test
    @InSequence(6)
    public void shouldCheckCollectionOfRatings() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals("Should have 5 links", 5, jsonObject.getJsonObject("links").size());
        assertEquals("Should have 1 talk", 1, jsonObject.getJsonArray("data").size());
        checkHeaders(response);
    }

    @Test
    @InSequence(7)
    public void shouldRemoveRating() throws Exception {
        Response response = webTarget.path(ratingId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(204, response.getStatus());
        Response checkResponse = webTarget.path(ratingId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
        checkHeaders(response);
    }

    @Test
    @InSequence(8)
    public void shouldRemoveWithInvalidInput() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).delete();
        assertEquals(405, response.getStatus());
        checkHeaders(response);
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private String getRatingId(Response response) {
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

    private String issueToken() {
        Key key = new SimpleKeyGenerator().generateKey();
        String jwtToken = Jwts.builder()
            .setSubject(TEST_RATING.getAttendeeId())
            .setIssuer(baseURL.toString())
            .setIssuedAt(new Date())
            .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();
        return "Bearer " + jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void checkHeaders(Response response) {
        CORSFilterTest.checkCORSHeaders(response);
    }
}
