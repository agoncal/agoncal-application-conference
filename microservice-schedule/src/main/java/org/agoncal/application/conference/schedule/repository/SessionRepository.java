package org.agoncal.application.conference.schedule.repository;


import org.agoncal.application.conference.schedule.domain.Session;

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
public class SessionRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Session create(Session session) {
        em.persist(session);
        return session;
    }

    public List<Session> findAllSessions(Integer pageNumber) {
        TypedQuery<Session> query = em.createNamedQuery(Session.FIND_ALL, Session.class);
        query.setFirstResult((pageNumber - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    public Integer getNumberOfPages() {
        TypedQuery<Long> query = em.createNamedQuery(Session.COUNT_ALL, Long.class);
        Long countResult = query.getSingleResult();
        return (Integer) (int) ((countResult / PAGE_SIZE) + 1);
    }

    public List<Session> findAllSessionsByDay(String day) {
        TypedQuery<Session> query = em.createNamedQuery(Session.FIND_BY_DAY, Session.class);
        query.setParameter("day", day);
        return query.getResultList();
    }

    public List<Session> findAllSessionsByDayAndRoom(String day, String roomId) {
        TypedQuery<Session> query = em.createNamedQuery(Session.FIND_BY_DAY_AND_ROOM, Session.class);
        query.setParameter("day", day);
        query.setParameter("roomId", roomId);
        return query.getResultList();
    }

    public Session findById(String id) {
        return em.find(Session.class, id);
    }

    public Session update(Session session) {
        return null;
    }

    public void delete(String id) {
        em.remove(em.getReference(Session.class, id));
    }
}
