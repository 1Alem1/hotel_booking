package com.hibernatestandalone.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_huesped;
    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String email;     //El email es unico
    private String dni;
    private String telefono;

    public Long getId_huesped() {
        return id_huesped;
    }

    public void setId_huesped(Long id_huesped) {
        this.id_huesped = id_huesped;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Nombre: " + this.getNombre() + ". Apellido: " + this.getApellido();
    }
}
