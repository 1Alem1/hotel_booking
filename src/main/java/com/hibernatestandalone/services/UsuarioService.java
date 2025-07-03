package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Empleado;
import com.hibernatestandalone.entity.Gerente;
import com.hibernatestandalone.entity.Usuario;
import com.hibernatestandalone.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import org.hibernate.query.Query;

public class UsuarioService extends AbstractService {

    public Usuario findById(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session.find(Usuario.class, id);
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
