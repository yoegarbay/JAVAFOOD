package com.productos.dto;

public class UsuarioResumen {

    private int    id;
    private String nom;
    private String apellidos;
    private String direccion;
    private int    telefono;
    private String email;
    private String tipo;
    private int    puntos;

    public UsuarioResumen(int id, String nom, String apellidos, String direccion,
                          int telefono, String email, String tipo, int puntos) {
        this.id        = id;
        this.nom       = nom;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono  = telefono;
        this.email     = email;
        this.tipo      = tipo;
        this.puntos    = puntos;
    }

    public int    getId()        { return id; }
    public String getNom()       { return nom; }
    public String getApellidos() { return apellidos; }
    public String getDireccion() { return direccion; }
    public int    getTelefono()  { return telefono; }
    public String getEmail()     { return email; }
    public String getTipo()      { return tipo; }
    public int    getPuntos()    { return puntos; }
}