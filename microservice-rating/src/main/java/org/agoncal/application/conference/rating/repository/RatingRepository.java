package org.agoncal.application.conference.rating.repository;


import org.agoncal.application.conference.rating.domain.Rating;

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

    public List<Rating> findAllRatings() {
        TypedQuery<Rating> query = em.createNamedQuery(Rating.FIND_ALL, Rating.class);
        return query.getResultList();
    }

    public Rating findById(String id) {
        return em.find(Rating.class, id);
    }

    public void delete(String id) {
        em.remove(em.getReference(Rating.class, id));
    }
}
