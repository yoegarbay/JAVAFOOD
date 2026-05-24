package com.productos.entity;

public class Usuario {

    private Integer id;
    private String  nom;
    private String  apellidos;
    private String  direccion;
    private int     telefono;
    private String  email;
    private String  contrasena;
    private String  tipo;

    public Usuario() {}

    public Usuario(Integer id, String nom, String apellidos, String direccion,
                   int telefono, String email, String contrasena, String tipo) {
        this.id        = id;
        this.nom       = nom;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono  = telefono;
        this.email     = email;
        this.contrasena = contrasena;
        this.tipo      = tipo;
    }

    public Integer getId()          { return id; }
    public void    setId(Integer v) { this.id = v; }

    public String getNom()          { return nom; }
    public void   setNom(String v)  { this.nom = v; }

    public String getApellidos()           { return apellidos; }
    public void   setApellidos(String v)   { this.apellidos = v; }

    public String getDireccion()           { return direccion; }
    public void   setDireccion(String v)   { this.direccion = v; }

    public int  getTelefono()       { return telefono; }
    public void setTelefono(int v)  { this.telefono = v; }

    public String getEmail()           { return email; }
    public void   setEmail(String v)   { this.email = v; }

    public String getContrasena()           { return contrasena; }
    public void   setContrasena(String v)   { this.contrasena = v; }

    public String getTipo()           { return tipo; }
    public void   setTipo(String v)   { this.tipo = v; }
}
