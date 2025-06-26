package com.hibernatestandalone.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Habitacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        
        @Column(unique = true)
	private String numero;
        
	private int piso;
	private int capacidad_personas;
        private double precio_por_noche;
        private String descripcion;
        
        @OneToMany(mappedBy = "habitacion")// Puede servir para ver todas las reservas de una habitacion
        private List<Reserva> reservas;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public int getPiso() {
            return piso;
        }

        public void setPiso(int piso) {
            this.piso = piso;
        }

        public int getCapacidad_personas() {
            return capacidad_personas;
        }

        public void setCapacidad_personas(int capacidad_personas) {
            this.capacidad_personas = capacidad_personas;
        }

        public double getPrecio_por_noche() {
            return precio_por_noche;
        }

        public void setPrecio_por_noche(double precio_por_noche) {
            this.precio_por_noche = precio_por_noche;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public List<Reserva> getReservas() {
            return reservas;
        }

        public void setReservas(List<Reserva> reservas) {
            this.reservas = reservas;
        }
}


