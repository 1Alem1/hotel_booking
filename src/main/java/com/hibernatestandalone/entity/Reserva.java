package com.hibernatestandalone.entity;

import com.hibernatestandalone.HibernateStandalone.EstadoReserva;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reserva;
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkIn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOut;
    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    // Relación con Factura (1 a 1)
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Factura factura;

    // Relación con Habitacion (una misma habitacion puede tener varias reservas pero en distintas fechas)
    @ManyToOne
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    // Relación con Huesped (muchas reservas pueden ser del mismo huésped)
    @ManyToOne
    @JoinColumn(name = "id_huesped", nullable = false)
    private Huesped huesped;

    public Long getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(Long id_reserva) {
        this.id_reserva = id_reserva;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

}
