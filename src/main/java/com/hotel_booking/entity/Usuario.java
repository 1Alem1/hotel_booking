package com.hotel_booking.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String apellido;
	@Column(unique = true)
	private String mail;
	private String password;
	private Date f_registro;
        @OneToMany(mappedBy = "usuario")
        private List<Nota> notas;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getFRegistro() {
		return f_registro;
	}
	public void setFRegistro(Date f_registro) {
		this.f_registro = f_registro;
	}

        public List<Nota> getNotas() {
            return notas;
        }

        public void setNotas(List<Nota> notas) {
            this.notas = notas;
        }
        
	@Override
	public String toString() {
		return "Nombre: "+this.getNombre()+". Apellido: "+ this.getApellido();
	}
}

