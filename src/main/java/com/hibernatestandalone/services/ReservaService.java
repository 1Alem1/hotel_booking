package com.hibernatestandalone.services;

import com.hibernatestandalone.HibernateStandalone.EstadoReserva;
import com.hibernatestandalone.entity.Habitacion;
import com.hibernatestandalone.entity.Huesped;
import com.hibernatestandalone.entity.Reserva;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.Date;
import java.util.List;

public class ReservaService extends AbstractService {

    public List<Habitacion> buscarDisponibles(Date fechaInicio, Date fechaFin) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        String hql = "FROM Habitacion h WHERE h.id NOT IN (" +
                     "SELECT r.habitacion.id FROM Reserva r " +
                     "WHERE r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio)";
        Query<Habitacion> query = session.createQuery(hql, Habitacion.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.list();
    }
}
    
    public Reserva cargarReserva(Date fechaInicio, Date fechaFin, Habitacion habitacion, Huesped huesped) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            // Crear la reserva
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(fechaInicio);
            reserva.setFechaFin(fechaFin);
            reserva.setEstado(EstadoReserva.CONFIRMADA);
            reserva.setHabitacion(habitacion);
            reserva.setHuesped(huesped);

            session.persist(reserva);
            tx.commit();
            return reserva;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<Reserva> getReservasConfirmadas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
         String hql = """
             SELECT r 
             FROM Reserva r
             JOIN FETCH r.huesped
             JOIN FETCH r.habitacion
             WHERE r.estado = :estado
         """;
         return session.createQuery(hql, Reserva.class)
                 .setParameter("estado", EstadoReserva.CONFIRMADA)
                 .getResultList();
        }
    }
}