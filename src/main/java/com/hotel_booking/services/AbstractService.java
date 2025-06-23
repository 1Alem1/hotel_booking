package com.hotel_booking.services;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import com.hotel_booking.util.HibernateUtil;

public abstract class AbstractService {
    private final SessionFactory sessionFactory;

	public AbstractService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();

	}
	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();	
	}
}
