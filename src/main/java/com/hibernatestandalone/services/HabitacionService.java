package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Habitacion;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


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
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }


}