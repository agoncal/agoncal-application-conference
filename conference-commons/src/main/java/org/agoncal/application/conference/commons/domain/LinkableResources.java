package org.agoncal.application.conference.commons.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.agoncal.application.conference.commons.domain.Links.*;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 *         Marks REST resources that represent a list of linkable resources.
 */
@XmlType(name = "links")
public class LinkableResources<R extends LinkableResource> {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final Integer PAGE_SIZE = 20;

    // ======================================
    // =             Attributes             =
    // ======================================

    private Map<String, URI> links;
    @XmlElement(name = "data")
    private List<R> linkableResources;

    // ======================================
    // =            Constructors            =
    // ======================================

    public LinkableResources() {
    }

    public LinkableResources(List<R> resources) {
        linkableResources = resources;
    }

    // ======================================
    // =          Business methods          =
    // ======================================

    public void addSelfLink(URI uri) {
        addLink(SELF, uri);
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

    public List<R> getLinkableResources() {
        return linkableResources;
    }

    public void setLinkableResources(List<R> linkableResources) {
        this.linkableResources = linkableResources;
    }

    public void addLinkableResource(R linkableResource) {
        if (linkableResources == null)
            linkableResources = new ArrayList<R>();
        linkableResources.add(linkableResource);
    }
}
