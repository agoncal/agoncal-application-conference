package org.agoncal.application.conference.commons.domain;

import javax.xml.bind.annotation.XmlType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.agoncal.application.conference.commons.domain.Links.*;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         Marks REST resources that are identifiable by an ID.
 */
@XmlType(name = "links")
public abstract class LinkableResource implements Identifiable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Map<String, URI> links;

    // ======================================
    // =          Business methods          =
    // ======================================

    public void addSelfLink(URI uri) {
        addLink(SELF, uri);
    }

    public void addCollectionLink(URI uri) {
        addLink(COLLECTION, uri);
    }

    public void addSwaggerLink(URI uri) {
        addLink(SWAGGER, uri);
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public Map<String, URI> getLinks() {
        return links;
    }

    public void addLink(String rel, URI uri) {
        if (links == null)
            links = new HashMap<>();
        if (!links.containsKey(rel))
            links.put(rel, uri);
    }
}
