package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.entity.Usuario;
import com.hibernatestandalone.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class GerenteService extends AbstractService {

    public Empleado cargarEmpleado(String nombre, String apellido, String email, String contrasenia, String dni, String telefono) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Verificación previa
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(e) FROM Empleado e WHERE e.email = :email", Long.class
            );
            query.setParameter("email", email.trim().toLowerCase());
            Long count = query.uniqueResult();

            if (count != null && count > 0) {
                throw new IllegalArgumentException("Ya existe un empleado con ese email.");
            }

            // Crear y guardar empleado dentro de la misma sesión
            Empleado empleado = new Empleado();
            empleado.setNombre(nombre);
            empleado.setApellido(apellido);
            empleado.setEmail(email.trim().toLowerCase());
            empleado.setContrasenia(contrasenia);
            empleado.setDni(dni);
            empleado.setTelefono(telefono);

            session.persist(empleado);
            transaction.commit();

            return empleado; 
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void modificarEmpleado(Usuario user) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Verificar si ya existe otro usuario con el mismo email
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario WHERE email = :email AND id_usuario <> :id", Usuario.class);
            query.setParameter("email", user.getEmail());
            query.setParameter("id", user.getId_usuario());
            List<Usuario> usuariosConMismoEmail = query.getResultList();

            if (!usuariosConMismoEmail.isEmpty()) {
                throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico.");
            }

            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

    }

    public void removerEmpleado(Long id) {
        Session session = this.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Usuario user = session.getReference(Usuario.class, id);
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
