package com.productos.dto;

/**
 * Actualizado para incluir `tipo` (rol) y `puntos`.
 * Necesario para que login.html guarde el rol en sessionStorage
 * y auth-rol.js muestre el botón correcto (ADMIN, EMPLEADO, CLIENTE).
 */
public class ClienteResponse {

    private int    id_cliente;
    private String nombre;
    private String email;
    private String tipo;    // ADMIN | EMPLEADO | CLIENTE
    private int    puntos;

    public ClienteResponse(int id_cliente, String nombre, String email, String tipo, int puntos) {
        this.id_cliente = id_cliente;
        this.nombre     = nombre;
        this.email      = email;
        this.tipo       = tipo;
        this.puntos     = puntos;
    }

    // Constructor legacy (2 campos) — por si algo lo sigue usando
    public ClienteResponse(int id_cliente, String nombre, String email) {
        this(id_cliente, nombre, email, "CLIENTE", 0);
    }

    public int    getId_cliente() { return id_cliente; }
    public String getNombre()     { return nombre; }
    public String getEmail()      { return email; }
    public String getTipo()       { return tipo; }
    public int    getPuntos()     { return puntos; }
}