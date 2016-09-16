package org.agoncal.application.conference.talk.repository;

import org.agoncal.application.conference.talk.domain.Talk;

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

    public List<Talk> getAllTalks() {
        TypedQuery<Talk> query = em.createNamedQuery(Talk.FIND_ALL, Talk.class);
        return query.getResultList();
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
