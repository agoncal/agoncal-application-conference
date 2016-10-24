package org.agoncal.application.conference.commons.rest;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class CORSFilterTest {

    // ======================================
    // =          Business methods          =
    // ======================================

    public static void checkCORSHeaders(Response response) {
        assertEquals("[*]", response.getHeaders().get("Access-Control-Allow-Origin").toString());
        assertEquals("[origin, content-type, accept, authorization, Etag, if-none-match]", response.getHeaders().get("Access-Control-Allow-Headers").toString());
        assertEquals("[true]", response.getHeaders().get("Access-Control-Allow-Credentials").toString());
        assertEquals("[GET, POST, PUT, DELETE, OPTIONS, HEAD]", response.getHeaders().get("Access-Control-Allow-Methods").toString());
        assertEquals("[1209600]", response.getHeaders().get("Access-Control-Max-Age").toString());
        assertEquals("[origin, content-type, accept, authorization, ETag, if-none-match]", response.getHeaders().get("Access-Control-Expose-Headers").toString());
    }
}
