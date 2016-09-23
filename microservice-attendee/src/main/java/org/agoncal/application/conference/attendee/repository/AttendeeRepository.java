package org.agoncal.application.conference.attendee.repository;

import org.agoncal.application.conference.attendee.domain.Attendee;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Transactional
public class AttendeeRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Attendee create(Attendee speaker) {
        em.persist(speaker);
        return speaker;
    }

    public List<Attendee> findAllSpeakers() {
        TypedQuery<Attendee> query = em.createNamedQuery(Attendee.FIND_ALL, Attendee.class);
        return query.getResultList();
    }

    public Attendee findById(String id) {
        return em.find(Attendee.class, id);
    }

    public Attendee update(Attendee room) {
        return null;
    }

    public void delete(String id) {
        em.remove(em.getReference(Attendee.class, id));
    }
}
