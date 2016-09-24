package org.agoncal.application.conference.talk.repository;

import org.agoncal.application.conference.talk.domain.Talk;

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
public class TalkRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Talk create(Talk talk) {
        em.persist(talk);
        return talk;
    }

    public List<Talk> findAllTalks(Integer pageNumber) {
        TypedQuery<Talk> query = em.createNamedQuery(Talk.FIND_ALL, Talk.class);
        query.setFirstResult((pageNumber - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    public Integer getNumberOfPages() {
        TypedQuery<Long> query = em.createNamedQuery(Talk.COUNT_ALL, Long.class);
        Long countResult = query.getSingleResult();
        return (Integer) (int) ((countResult / PAGE_SIZE) + 1);
    }

    public Talk findById(String id) {
        return em.find(Talk.class, id);
    }

    public Talk update(Talk room) {
        return null;
    }

    public void delete(String id) {
        em.remove(em.getReference(Talk.class, id));
    }
}
