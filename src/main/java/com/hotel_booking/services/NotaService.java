package com.hotel_booking.services;

import com.hotel_booking.entity.Nota;
import com.hotel_booking.entity.Usuario;
import com.hotel_booking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class NotaService extends AbstractService {

    public Nota create(String texto, Usuario usuario) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Nota nota = new Nota();
            nota.setTexto(texto);
            nota.setUsuario(usuario);
            nota.setF_creacion(new Date());
            session.persist(nota);
            session.flush();
            transaction.commit();
            return nota;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Nota create(Nota nota) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            nota.setF_creacion(new Date());
            session.persist(nota);
            session.flush();
            transaction.commit();
            return nota;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Nota refresh(Nota nota) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.refresh(nota);
        return nota;
    }

    public Nota findById(long id) {
        Session session = this.getSession();
        return session.find(Nota.class, id);
    }

    public List<Nota> getAll() {
        Session session = this.getSession();
        String hql = "FROM Nota";
        return session.createQuery(hql, Nota.class).getResultList();
    }

    public void delete(long id) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Nota nota = session.getReference(Nota.class, id);
            session.remove(nota);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void delete(Nota nota) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(nota);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void update(Nota nota, String texto) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            nota.setTexto(texto);
            session.merge(nota);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void update(int id, String texto) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Nota nota = session.getReference(Nota.class, id);
            if (nota != null) {
                nota.setTexto(texto);
                session.merge(nota);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

}