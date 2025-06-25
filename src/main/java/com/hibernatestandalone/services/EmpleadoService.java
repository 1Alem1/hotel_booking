package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


public class EmpleadoService extends AbstractService {
    
    public List<Empleado> getAll() {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Empleado";
        List<Empleado> list = session.createQuery(hql, Empleado.class).getResultList();
        return list;
    }
}