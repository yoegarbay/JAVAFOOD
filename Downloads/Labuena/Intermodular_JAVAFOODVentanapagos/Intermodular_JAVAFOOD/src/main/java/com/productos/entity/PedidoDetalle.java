package com.productos.entity;

public class PedidoDetalle {
    private Integer id_linea;
    private Integer id_pedido;
    private String nombre_producto;
    private int cantidad;
    private float precio_unitario;
    private float subtotal;

    public PedidoDetalle(Integer id_linea, Integer id_pedido, String nombre_producto, int cantidad, float precio_unitario, float subtotal) {
        this.id_linea = id_linea;
        this.id_pedido = id_pedido;
        this.nombre_producto = nombre_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
    }

    public Integer getId_linea() { return id_linea; }
    public void setId_linea(Integer id_linea) { this.id_linea = id_linea; }
    public Integer getId_pedido() { return id_pedido; }
    public String getNombre_producto() { return nombre_producto; }
    public int getCantidad() { return cantidad; }
    public float getPrecio_unitario() { return precio_unitario; }
    public float getSubtotal() { return subtotal; }
}
