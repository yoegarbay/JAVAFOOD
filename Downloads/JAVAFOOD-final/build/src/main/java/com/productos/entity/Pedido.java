package com.productos.entity;

import java.time.LocalDateTime;

public class Pedido {
    private Integer id_pedido;
    private LocalDateTime fecha;
    private float total;
    private String estado;

    public Pedido(Integer id_pedido, LocalDateTime fecha, float total, String estado) {
        this.id_pedido = id_pedido;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    public Integer getId_pedido() { return id_pedido; }
    public void setId_pedido(Integer id_pedido) { this.id_pedido = id_pedido; }
    public LocalDateTime getFecha() { return fecha; }
    public float getTotal() { return total; }
    public String getEstado() { return estado; }
}
