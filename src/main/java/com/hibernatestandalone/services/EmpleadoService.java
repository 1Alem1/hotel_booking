package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.entity.Huesped;
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

    public Huesped cargarHuesped(String nombre, String apellido, String email, String dni, String telefono) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Buscar huésped por email
            Query<Huesped> query = session.createQuery(
                    "FROM Huesped h WHERE h.email = :email", Huesped.class
            );
            query.setParameter("email", email.trim().toLowerCase());
            Huesped existente = query.uniqueResult();

            if (existente != null) {
                return existente; //
            }

            // Crear y guardar nuevo huésped
            Huesped huesped = new Huesped();
            huesped.setNombre(nombre);
            huesped.setApellido(apellido);
            huesped.setEmail(email.trim().toLowerCase());
            huesped.setDni(dni);
            huesped.setTelefono(telefono);

            session.persist(huesped);
            transaction.commit();

            return huesped;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
