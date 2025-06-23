package com.hibernatestandalone.HibernateStandalone;

import com.hibernatestandalone.entity.Huesped;
import com.hibernatestandalone.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class App {
    public static void main(String[] args) {
try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            // Abrir transacción
            Transaction tx = session.beginTransaction();
            
            // Crear un nuevo huésped (solo para probar)
            Huesped huesped = new Huesped();
            huesped.setNombre("Juan");
            huesped.setApellido("Pérez");
            huesped.setEmail("juan.perez@example.com");
            huesped.setDni("12345678");
            huesped.setTelefono("123456789");
            
            // Guardar el huésped en la DB
            session.persist(huesped);
            
            // Confirmar la transacción
            tx.commit();
            
            System.out.println("Huésped creado con id: " + huesped.getId_huesped());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown(); // Cerrar SessionFactory
        }
    }
}
