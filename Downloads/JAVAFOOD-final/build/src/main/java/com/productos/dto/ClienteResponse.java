package com.productos.dto;

public class ClienteResponse {
    private int    id_cliente;
    private String nombre;
    private String email;

    public ClienteResponse(int id_cliente, String nombre, String email) {
        this.id_cliente = id_cliente;
        this.nombre     = nombre;
        this.email      = email;
    }
    public int    getId_cliente() { return id_cliente; }
    public String getNombre()     { return nombre; }
    public String getEmail()      { return email; }
}
