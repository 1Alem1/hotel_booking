package com.hibernatestandalone.services;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import com.hibernatestandalone.util.HibernateUtil;

public abstract class AbstractService {

    private final SessionFactory sessionFactory;

    public AbstractService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();

    }

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
