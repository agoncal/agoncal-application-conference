package org.agoncal.application.conference.speaker.rest;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@ApplicationPath("/")
public class Application extends javax.ws.rs.core.Application {

    // ======================================
    // =            Constructors            =
    // ======================================

    public Application() {
        final BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Room");
        beanConfig.setDescription("Gives all the details of the rooms of the venue");
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8081/venue");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("org.agoncal.application.conference.venue.rest");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet();

        resources.add(SpeakerEndpoint.class);
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);

        return resources;
    }
}
