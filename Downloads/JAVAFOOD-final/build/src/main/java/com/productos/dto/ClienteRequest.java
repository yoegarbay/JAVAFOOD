package com.productos.dto;

public class ClienteRequest {
    private String nombre;
    private String email;
    private String password;

    public String getNombre()           { return nombre; }
    public void   setNombre(String v)   { this.nombre = v; }
    public String getEmail()            { return email; }
    public void   setEmail(String v)    { this.email = v; }
    public String getPassword()         { return password; }
    public void   setPassword(String v) { this.password = v; }
}
