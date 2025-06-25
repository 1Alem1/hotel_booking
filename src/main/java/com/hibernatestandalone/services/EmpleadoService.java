package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


public class EmpleadoService extends AbstractService {
    
    public Empleado create(String nombre, String apellido, String email, String contrasenia, String dni, String telefono) {
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

            return empleado; // aún válido al salir del try
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
    
    public List<Empleado> getAll() {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        String hql = "FROM Empleado";
        List<Empleado> list = session.createQuery(hql, Empleado.class).getResultList();
        return list;
    }
}