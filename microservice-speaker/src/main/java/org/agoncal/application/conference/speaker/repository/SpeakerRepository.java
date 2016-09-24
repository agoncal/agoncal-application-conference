package org.agoncal.application.conference.speaker.repository;

import org.agoncal.application.conference.speaker.domain.Speaker;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.agoncal.application.conference.commons.domain.LinkableResources.PAGE_SIZE;

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

    public Speaker create(Speaker speaker) {
        em.persist(speaker);
        return speaker;
    }

    public List<Speaker> findAllSpeakers(Integer pageNumber) {
        TypedQuery<Speaker> query = em.createNamedQuery(Speaker.FIND_ALL, Speaker.class);
        query.setFirstResult((pageNumber - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    public Integer getNumberOfPages() {
        TypedQuery<Long> query = em.createNamedQuery(Speaker.COUNT_ALL, Long.class);
        Long countResult = query.getSingleResult();
        return (Integer) (int) ((countResult / PAGE_SIZE) + 1);
    }

    public Speaker findById(String id) {
        return em.find(Speaker.class, id);
    }

    public Speaker update(Speaker room) {
        return null;
    }

    public void delete(String id) {
        em.remove(em.getReference(Speaker.class, id));
    }
}
