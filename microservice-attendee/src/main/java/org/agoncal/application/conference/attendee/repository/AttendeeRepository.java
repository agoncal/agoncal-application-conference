package org.agoncal.application.conference.attendee.repository;

import org.agoncal.application.conference.attendee.domain.Attendee;
import org.agoncal.application.conference.commons.jwt.PasswordUtils;

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
public class AttendeeRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Attendee create(Attendee attendee) {
        em.persist(attendee);
        return attendee;
    }

    public Attendee findByLoginPassWord(String login, String password) {
        TypedQuery<Attendee> query = em.createNamedQuery(Attendee.FIND_BY_LOGIN_PASSWORD, Attendee.class);
        query.setParameter("login", login);
        query.setParameter("password", PasswordUtils.digestPassword(password));
        return query.getSingleResult();
    }

    public List<Attendee> findAllAttendees(Integer pageNumber) {
        TypedQuery<Attendee> query = em.createNamedQuery(Attendee.FIND_ALL, Attendee.class);
        query.setFirstResult((pageNumber - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    public Integer getNumberOfPages() {
        TypedQuery<Long> query = em.createNamedQuery(Attendee.COUNT_ALL, Long.class);
        Long countResult = query.getSingleResult();
        return (Integer) (int) ((countResult / PAGE_SIZE) + 1);
    }

    public Attendee findById(String id) {
        return em.find(Attendee.class, id);
    }

    public Attendee update(Attendee attendee) {
        return null;
    }

    public void delete(String id) {
        em.remove(em.getReference(Attendee.class, id));
    }
}
