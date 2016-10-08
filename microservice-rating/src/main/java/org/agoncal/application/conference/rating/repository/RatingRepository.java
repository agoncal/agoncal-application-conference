package org.agoncal.application.conference.rating.repository;


import org.agoncal.application.conference.rating.domain.Rating;

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
public class RatingRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Rating rateSession(Rating rating) {
        em.persist(rating);
        return rating;
    }

    public Rating updateRating(Rating rating) {
        return em.merge(em.getReference(Rating.class, rating.getId()));
    }

    public List<Rating> findRatingsBySession(String sessionId) {
        TypedQuery<Rating> query = em.createNamedQuery(Rating.FIND_BY_SESSION, Rating.class);
        query.setParameter("sessionId", sessionId);
        return query.getResultList();
    }

    public List<Rating> findRatingsByAttendee(String attendeeId) {
        TypedQuery<Rating> query = em.createNamedQuery(Rating.FIND_BY_ATTENDEE, Rating.class);
        query.setParameter("attendeeId", attendeeId);
        return query.getResultList();
    }

    public List<Rating> findAllRatings(Integer pageNumber) {
        TypedQuery<Rating> query = em.createNamedQuery(Rating.FIND_ALL, Rating.class);
        query.setFirstResult((pageNumber - 1) * PAGE_SIZE);
        query.setMaxResults(PAGE_SIZE);
        return query.getResultList();
    }

    public Integer getNumberOfPages() {
        TypedQuery<Long> query = em.createNamedQuery(Rating.COUNT_ALL, Long.class);
        Long countResult = query.getSingleResult();
        return (Integer) (int) ((countResult / PAGE_SIZE) + 1);
    }

    public Rating findById(String id) {
        return em.find(Rating.class, id);
    }

    public void delete(String id) {
        em.remove(em.getReference(Rating.class, id));
    }
}
