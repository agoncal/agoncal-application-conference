package org.agoncal.application.conference.commons.rest;

import org.agoncal.application.conference.commons.domain.Identifiable;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
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

    public URI getURIForCollection() {
        return uriInfo.getBaseUriBuilder().path(type).build();
    }

    public URI getURIForPage(Integer pageNumber) {
        return uriInfo.getBaseUriBuilder().path(type).queryParam("page", pageNumber).build();
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    public GenericEntity<List<E>> buildEntity(final List<E> roomList) {
        return new GenericEntity<List<E>>(roomList) {
        };
    }
}
