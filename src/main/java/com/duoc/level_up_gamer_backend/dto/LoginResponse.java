package com.duoc.level_up_gamer_backend.dto;

import com.duoc.level_up_gamer_backend.model.Rol;

public class LoginResponse {
    private String token;
    private Long id;
    private String username;
    private String nombre;
    private String apellidos;
    private String email;
    private Rol rol;
    private Integer puntos;
    private Boolean esDuoc;

    public LoginResponse() {}

    public LoginResponse(String token, Long id, String username, String nombre, String apellidos, String email, Rol rol, Integer puntos, Boolean esDuoc) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.rol = rol;
        this.puntos = puntos;
        this.esDuoc = esDuoc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Boolean getEsDuoc() {
        return esDuoc;
    }

    public void setEsDuoc(Boolean esDuoc) {
        this.esDuoc = esDuoc;
    }
}
