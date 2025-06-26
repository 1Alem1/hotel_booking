package com.hibernatestandalone.services;

import com.hibernatestandalone.entity.Factura;
import com.hibernatestandalone.entity.Reserva;
import com.hibernatestandalone.services.AbstractService;
import com.hibernatestandalone.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class FacturaService extends AbstractService {
    public Factura crearYGuardarFactura(Reserva reserva, String medioPago) {
        // 1) cálculo de noches
        long millisPorNoche = 1000L * 60 * 60 * 24;
        long diff = reserva.getFechaFin().getTime() - reserva.getFechaInicio().getTime();
        long noches = diff / millisPorNoche;
        if (noches < 1) noches = 1;

        // 2) precio y total
        double precioNoche = reserva.getHabitacion().getPrecio_por_noche();
        double total = noches * precioNoche;

        // 3) crear factura
        Factura factura = new Factura();
        factura.setMedioDePago(medioPago);
        factura.setTotal(total);
        factura.setReserva(reserva);
        reserva.setFactura(factura);

        // 4) persistir factura (cascade en reserva NO se activa aquí,
        //    así que guardamos la factura directamente)
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(factura);
            tx.commit();
            return factura;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}