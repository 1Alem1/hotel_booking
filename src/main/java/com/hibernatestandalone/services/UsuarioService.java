package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Usuario;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class UsuarioService extends AbstractService {

    public Usuario create(String nombre, String apellido, String email, String contrasenia, String dni, String telefono) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Usuario user = new Usuario() {};
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setEmail(email);
            user.setContrasenia(contrasenia);
            user.setDni(dni);
            user.setTelefono(telefono);
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Usuario create(Usuario user) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Usuario refresh(Usuario user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.refresh(user);
        return user;
    }
    
    public Usuario findById(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session.find(Usuario.class, id);
    }

    public List<Usuario> getAll() {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Usuario";
        List<Usuario> list = session.createQuery(hql, Usuario.class).getResultList();
        return list;
    }

    public void delete(long id) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Usuario user = session.getReference(Usuario.class, id);
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void delete(Usuario user) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void update(Usuario user, String nombre, String apellido, String email, String contrasenia, String dni, String telefono) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (nombre != null)   user.setNombre(nombre);
            if (apellido != null) user.setApellido(apellido);
            if (email != null)    user.setEmail(email);
            if (contrasenia != null) user.setContrasenia(contrasenia);
            if (dni != null) user.setDni(dni);
            if (telefono != null) user.setTelefono(telefono);
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void update(int id, String nombre, String apellido, String email, String contrasenia, String dni, String telefono) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Usuario user = session.getReference(Usuario.class, id);
            if (user != null) {
                if (nombre != null)   user.setNombre(nombre);
                if (apellido != null) user.setApellido(apellido);
                if (contrasenia != null) user.setContrasenia(contrasenia);
                if (dni != null) user.setDni(dni);
                if (telefono != null) user.setTelefono(telefono);
                session.merge(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
    
    public void modificarDatos(){
    
    }
    
    public void iniciarSesion(){
    
    }
}