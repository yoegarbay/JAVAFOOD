package com.productos.dto;

public class ResenaAdminResponse {

    private int    id_resena;
    private int    id_producto;
    private String nombre_producto;
    private int    id_cliente;
    private String nombre_cliente;
    private int    puntuacion;
    private String comentario;
    private String fecha;

    public ResenaAdminResponse() {}

    public ResenaAdminResponse(int id_resena, int id_producto, String nombre_producto,
                               int id_cliente, String nombre_cliente,
                               int puntuacion, String comentario, String fecha) {
        this.id_resena       = id_resena;
        this.id_producto     = id_producto;
        this.nombre_producto = nombre_producto;
        this.id_cliente      = id_cliente;
        this.nombre_cliente  = nombre_cliente;
        this.puntuacion      = puntuacion;
        this.comentario      = comentario;
        this.fecha           = fecha;
    }

    public int    getId_resena()       { return id_resena; }
    public int    getId_producto()     { return id_producto; }
    public String getNombre_producto() { return nombre_producto; }
    public int    getId_cliente()      { return id_cliente; }
    public String getNombre_cliente()  { return nombre_cliente; }
    public int    getPuntuacion()      { return puntuacion; }
    public String getComentario()      { return comentario; }
    public String getFecha()           { return fecha; }
}
