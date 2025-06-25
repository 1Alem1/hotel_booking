package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.entity.Gerente;
import com.hibernatestandalone.entity.Usuario;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;
import org.hibernate.query.Query;

public class UsuarioService extends AbstractService {

    //No hacen falta los metodos de crear Usuario porque es abstracto, se crean simplemente empleados en GerenteService
    /*public Usuario create(String nombre, String apellido, String email, String contrasenia, String dni, String telefono) {
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
    }*/

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

    public void delete(Long id) {
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
    
    
    
    
    //Servicio para iniciar sesion
    public Usuario iniciarSesion(String email, String contrasenia) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Buscar primero en Gerente
            Query<Gerente> queryGerente = session.createQuery(
                "FROM Gerente WHERE email = :email AND contrasenia = :contrasenia", Gerente.class);
            queryGerente.setParameter("email", email);
            queryGerente.setParameter("contrasenia", contrasenia);

            List<Gerente> gerentes = queryGerente.getResultList();
            if (!gerentes.isEmpty()) {
                return gerentes.get(0);
            }

            // Si no es gerente, buscar en Empleado
            Query<Empleado> queryEmpleado = session.createQuery(
                "FROM Empleado WHERE email = :email AND contrasenia = :contrasenia", Empleado.class);
            queryEmpleado.setParameter("email", email);
            queryEmpleado.setParameter("contrasenia", contrasenia);

            List<Empleado> empleados = queryEmpleado.getResultList();
            if (!empleados.isEmpty()) {
                return empleados.get(0);
            }
            return null; // No se encontró el usuario
        }
    }
    
    public Usuario obtenerPorEmailYClave(String email, String contrasenia) {
        // lógica para buscar en BD
        // por ejemplo usando Hibernate:
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Gerente> query = session.createQuery("FROM Gerente WHERE email = :email AND contrasenia = :contrasenia", Gerente.class);
        query.setParameter("email", email);
        query.setParameter("contrasenia", contrasenia);
        Gerente gerente = query.uniqueResult();
        session.close();
        return gerente;
    }
}
