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

    public List<Reserva> getReservasActivas() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        String hql = "FROM Reserva r WHERE r.estado = :estado1 OR r.estado = :estado2";
        Query<Reserva> query = session.createQuery(hql, Reserva.class);
        query.setParameter("estado1", EstadoReserva.CONFIRMADA);
        query.setParameter("estado2", EstadoReserva.CHECK_IN);
        return query.list();
    }
}
    
    public void actualizarReserva(Reserva reserva) {
    Transaction tx = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        tx = session.beginTransaction();
        session.merge(reserva);
        tx.commit();
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        throw e;
    }
}
    
    public void hacerCheckIn(Long idReserva) {
    Transaction tx = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        tx = session.beginTransaction();

        Reserva reserva = session.find(Reserva.class, idReserva);

        if (reserva != null) {
            reserva.setEstado(EstadoReserva.CHECK_IN);
            reserva.setCheckIn(new Date());
            session.merge(reserva);
            tx.commit();
        } else {
            throw new RuntimeException("Reserva no encontrada con id: " + idReserva);
        }
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        throw e;
    }
    }
    
    public void hacerCheckOut(Long idReserva) {
    Transaction tx = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        tx = session.beginTransaction();

        Reserva reserva = session.find(Reserva.class, idReserva);

        if (reserva != null) {
            reserva.setEstado(EstadoReserva.CHECK_OUT);
            reserva.setCheckOut(new Date()); // 🕒 guarda fecha y hora actual
            session.merge(reserva);
            tx.commit();
        } else {
            throw new RuntimeException("Reserva no encontrada con id: " + idReserva);
        }
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        throw e;
    }
}
     public List<Object[]> getIngresosPorHabitacion(Date desde, Date hasta) {
       try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                SELECT h.numero, COUNT(r), SUM(f.total)
                FROM Reserva r
                JOIN r.habitacion h
                JOIN r.factura f
                WHERE r.estado = :estadoReservaFinalizada
                AND r.checkIn <= :hasta AND r.checkOut >= :desde
                GROUP BY h.numero
                """;

            Query<Object[]> query = session.createQuery(hql, Object[].class);

            query.setParameter("estadoReservaFinalizada", EstadoReserva.CHECK_OUT);
            query.setParameter("desde", desde);
            query.setParameter("hasta", hasta);

            return query.getResultList();
        }
    }
}
