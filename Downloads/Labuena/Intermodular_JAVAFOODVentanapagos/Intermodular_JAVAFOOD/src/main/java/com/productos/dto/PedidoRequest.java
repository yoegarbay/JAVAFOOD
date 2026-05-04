package com.productos.dto;

import java.util.List;

public class PedidoRequest {

    private List<LineaPedido> items;
    private String nombreCliente;   // nombre que mete el usuario en el formulario
    private String metodoPago;      // "EFECTIVO" o "TARJETA"

    // ── Getters / Setters ──────────────────────────────────
    public List<LineaPedido> getItems()            { return items; }
    public void setItems(List<LineaPedido> items)  { this.items = items; }

    public String getNombreCliente()                      { return nombreCliente; }
    public void setNombreCliente(String nombreCliente)    { this.nombreCliente = nombreCliente; }

    public String getMetodoPago()                         { return metodoPago; }
    public void setMetodoPago(String metodoPago)          { this.metodoPago = metodoPago; }

    // ── Clase interna: una línea del pedido ───────────────
    public static class LineaPedido {
        private String nombre;
        private float  precio;
        private int    cantidad;
        private float  total;

        public String getNombre()              { return nombre; }
        public void setNombre(String nombre)   { this.nombre = nombre; }
        public float getPrecio()               { return precio; }
        public void setPrecio(float precio)    { this.precio = precio; }
        public int getCantidad()               { return cantidad; }
        public void setCantidad(int cantidad)  { this.cantidad = cantidad; }
        public float getTotal()                { return total; }
        public void setTotal(float total)      { this.total = total; }
    }
}