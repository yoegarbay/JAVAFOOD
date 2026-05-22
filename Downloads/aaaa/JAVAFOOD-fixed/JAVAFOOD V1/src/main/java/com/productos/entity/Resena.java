package com.productos.entity;

import java.time.LocalDateTime;

public class Resena {

    private Integer id_resena;
    private Integer id_producto;
    private Integer id_cliente;
    private String  nombre_cliente;
    private int     puntuacion;
    private String  comentario;
    private LocalDateTime fecha;

    public Resena(Integer id_resena, Integer id_producto, Integer id_cliente,
                  String nombre_cliente, int puntuacion, String comentario,
                  LocalDateTime fecha) {
        this.id_resena      = id_resena;
        this.id_producto    = id_producto;
        this.id_cliente     = id_cliente;
        this.nombre_cliente = nombre_cliente;
        this.puntuacion     = puntuacion;
        this.comentario     = comentario;
        this.fecha          = fecha;
    }

    public Integer getId_resena()       { return id_resena; }
    public Integer getId_producto()     { return id_producto; }
    public Integer getId_cliente()      { return id_cliente; }
    public String  getNombre_cliente()  { return nombre_cliente; }
    public int     getPuntuacion()      { return puntuacion; }
    public String  getComentario()      { return comentario; }
    public LocalDateTime getFecha()     { return fecha; }
}
