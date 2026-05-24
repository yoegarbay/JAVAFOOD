package com.productos.dto;

/**
 * Actualizado para recibir todos los campos que manda register.html:
 * nombre, apellidos, direccion, telefono, email, password.
 */
public class ClienteRequest {

    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String email;
    private String password;

    public String getNombre()           { return nombre; }
    public void   setNombre(String v)   { this.nombre = v; }
    public String getApellidos()        { return apellidos; }
    public void   setApellidos(String v){ this.apellidos = v; }
    public String getDireccion()        { return direccion; }
    public void   setDireccion(String v){ this.direccion = v; }
    public String getTelefono()         { return telefono; }
    public void   setTelefono(String v) { this.telefono = v; }
    public String getEmail()            { return email; }
    public void   setEmail(String v)    { this.email = v; }
    public String getPassword()         { return password; }
    public void   setPassword(String v) { this.password = v; }
}