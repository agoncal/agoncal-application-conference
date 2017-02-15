package org.agoncal.application.conference.commons.rest;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
//@Provider
public class IPFilter implements ContainerResponseFilter {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private Logger logger;

    // ======================================
    // =          Business methods          =
    // ======================================

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        logger.info("### " + InetAddress.getLocalHost().toString());
        responseContext.getHeaders().add(HttpHeaders.HOST, InetAddress.getLocalHost().toString());
    }
}
