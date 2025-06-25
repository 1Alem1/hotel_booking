package com.hibernatestandalone.HibernateStandalone;

import com.hibernatestandalone.entity.Huesped;
import com.hibernatestandalone.pantallas.InicioSesion;
import com.hibernatestandalone.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class App {
    public static void main(String[] args) {
        
        InicioSesion inicioSesion = new InicioSesion();
        inicioSesion.setVisible(true);
        inicioSesion.setLocationRelativeTo(null);
        
    }
}
