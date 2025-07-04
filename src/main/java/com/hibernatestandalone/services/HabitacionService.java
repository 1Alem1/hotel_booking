package com.hibernatestandalone.services;

import com.hibernatestandalone.HibernateStandalone.EstadoReserva;
import com.hibernatestandalone.entity.Habitacion;
import com.hibernatestandalone.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HabitacionService extends AbstractService {

    public List<Habitacion> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Habitacion> list = session
                    .createQuery("FROM Habitacion", Habitacion.class)
                    .getResultList();
            return list;
        }
    }

    public Habitacion findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session.find(Habitacion.class, id);
    }

    public void actualizar(Habitacion habitacion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(habitacion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Habitacion> buscarDisponibles(Date fechaInicio, Date fechaFin) {
        System.out.println("Buscando disponibles desde: " + fechaInicio + " hasta: " + fechaFin);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = """
                SELECT h FROM Habitacion h
                WHERE h.id NOT IN (
                    SELECT r.habitacion.id FROM Reserva r
                    WHERE r.estado = :estadoConfirmada
                    AND r.fechaInicio < :fechaFin
                    AND r.fechaFin > :fechaInicio
                )
            """;
            List<Habitacion> resultado = session.createQuery(hql, Habitacion.class)
                    .setParameter("estadoConfirmada", EstadoReserva.CONFIRMADA)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();

            System.out.println("Habitaciones disponibles encontradas: " + resultado.size());
            return resultado;
        } finally {
            session.close();
        }
    }

}
