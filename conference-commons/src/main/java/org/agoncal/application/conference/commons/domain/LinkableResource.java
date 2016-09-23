package org.agoncal.application.conference.commons.domain;

import javax.xml.bind.annotation.XmlType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         Marks objects that are identifiable by an ID of any type.
 */
@XmlType(name = "links")
public abstract class LinkableResource implements Identifiable {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String SELF = "self";
    public static final String COLLECTION = "collection";
    public static final String FIRST = "first";
    public static final String PREVIOUS = "prev";
    public static final String NEXT = "next";
    public static final String LAST = "last";

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

    public void addFirst(URI uri) {
        addLink(FIRST, uri);
    }

    public void addPrevious(URI uri) {
        addLink(PREVIOUS, uri);
    }

    public void addNext(URI uri) {
        addLink(NEXT, uri);
    }

    public void addLast(URI uri) {
        addLink(LAST, uri);
    }

    private void addLink(String rel, URI uri) {
        if (links == null)
            links = new HashMap<>();
        links.put(rel, uri);
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public Map<String, URI> getLinks() {
        return links;
    }
}
