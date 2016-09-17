package org.agoncal.application.conference.schedule.repository;


import org.agoncal.application.conference.schedule.domain.Schedule;

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
public class ScheduleRepository {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @PersistenceContext
    private EntityManager em;

    // ======================================
    // =          Business methods          =
    // ======================================

    public Schedule create(Schedule schedule) {
        em.persist(schedule);
        return schedule;
    }

    public List<Schedule> getAllSchedules() {
        TypedQuery<Schedule> query = em.createNamedQuery(Schedule.FIND_ALL, Schedule.class);
        return query.getResultList();
    }

    public List<Schedule> getAllSchedulesByDay(String day) {
        TypedQuery<Schedule> query = em.createNamedQuery(Schedule.FIND_BY_DAY, Schedule.class);
        query.setParameter("day", day);
        return query.getResultList();
    }

    public List<Schedule> getAllSchedulesByDayAndRoom(String day, String roomId) {
        TypedQuery<Schedule> query = em.createNamedQuery(Schedule.FIND_BY_DAY_AND_ROOM, Schedule.class);
        query.setParameter("day", day);
        query.setParameter("roomId", roomId);
        return query.getResultList();
    }

    public Schedule findById(String id) {
        return em.find(Schedule.class, id);
    }

    public Schedule update(Schedule schedule) {
        return null;
    }

    public void delete(String id) {
        em.remove(em.getReference(Schedule.class, id));
    }
}
