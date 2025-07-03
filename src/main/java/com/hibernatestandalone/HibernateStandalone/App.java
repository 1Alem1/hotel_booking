package com.hibernatestandalone.HibernateStandalone;

import com.hibernatestandalone.pantallas.InicioSesion;

public class App {

    public static void main(String[] args) {
        //Iniciar sesion 
        InicioSesion inicioSesion = new InicioSesion();
        inicioSesion.setVisible(true);
        inicioSesion.setLocationRelativeTo(null);

    }
}
