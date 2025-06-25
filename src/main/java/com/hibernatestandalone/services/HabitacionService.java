package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Habitacion;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class HabitacionService extends AbstractService {
    
    public List<Habitacion> getAll() {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Habitacion";
        List<Habitacion> list = session.createQuery(hql, Habitacion.class).getResultList();
        return list;
    }
}