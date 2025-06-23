package com.hotel_booking.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Nota {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String texto;
	private Date f_creacion;
	@ManyToOne
        private Usuario usuario;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTexto() {
            return texto;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }

        public Date getF_creacion() {
            return f_creacion;
        }

        public void setF_creacion(Date f_creacion) {
            this.f_creacion = f_creacion;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
        
	@Override
	public String toString() {
		return this.getTexto();
	}
}

