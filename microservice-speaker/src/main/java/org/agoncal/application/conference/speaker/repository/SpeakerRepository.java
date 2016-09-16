package org.agoncal.application.conference.speaker.repository;

import org.agoncal.application.conference.speaker.domain.Speaker;
import org.agoncal.application.conference.speaker.resource.SpeakerResource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Transactional
public class SpeakerRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Speaker create(SpeakerResource speakerResource) {
        return null;
    }

    public List<Speaker> getAllSpeakers() {
        return null;
    }

    public Speaker findById(String id) {
        return em.find(Speaker.class, id);
    }

    public Speaker update(SpeakerResource room) {
        return null;
    }

    public void delete(String id) {
    }
}
