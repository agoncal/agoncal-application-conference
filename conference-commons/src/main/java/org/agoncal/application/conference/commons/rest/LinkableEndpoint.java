package org.agoncal.application.conference.commons.rest;

import org.agoncal.application.conference.commons.domain.Identifiable;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public abstract class LinkableEndpoint<E extends Identifiable> {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Context
    private UriInfo uriInfo;

    // ======================================
    // =             Attributes             =
    // ======================================

    protected Class type;

    // ======================================
    // =            Constructors            =
    // ======================================

    public LinkableEndpoint(Class type) {
        this.type = type;
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    public UriBuilder getUriBuilderForRoot() {
        return uriInfo.getBaseUriBuilder().path(type);
    }

    public URI getURIForSelf(E identiable) {
        return uriInfo.getBaseUriBuilder().path(type).path(identiable.getId()).build();
    }

    public UriBuilder getURIBuilderForSelf(E identiable) {
        return uriInfo.getBaseUriBuilder().path(type).path(identiable.getId());
    }

    public URI getURIForCollection() {
        return uriInfo.getBaseUriBuilder().path(type).build();
    }

    public URI getURIForPage(Integer pageNumber) {
        return uriInfo.getBaseUriBuilder().path(type).queryParam("page", pageNumber).build();
    }

    public URI getURIForSwagger() {
        try {
            return new URI(uriInfo.getBaseUri().toString().replace("api/", "swagger.json"));
        } catch (URISyntaxException e) {
            return uriInfo.getBaseUriBuilder().path(type).build();
        }
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    public GenericEntity<List<E>> buildEntities(final List<E> entities) {
        return new GenericEntity<List<E>>(entities) {
        };
    }
}
