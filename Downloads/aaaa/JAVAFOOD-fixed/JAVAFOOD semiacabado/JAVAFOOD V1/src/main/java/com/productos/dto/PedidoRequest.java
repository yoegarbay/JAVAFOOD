package com.productos.dto;

import java.util.List;

public class PedidoRequest {

    private List<LineaPedido> items;
    private String  nombreCliente;
    private String  metodoPago;
    private Integer idCliente;     // null si el usuario no ha iniciado sesión

    public List<LineaPedido> getItems()            { return items; }
    public void setItems(List<LineaPedido> items)  { this.items = items; }
    public String getNombreCliente()               { return nombreCliente; }
    public void setNombreCliente(String v)         { this.nombreCliente = v; }
    public String getMetodoPago()                  { return metodoPago; }
    public void setMetodoPago(String v)            { this.metodoPago = v; }
    public Integer getIdCliente()                  { return idCliente; }
    public void setIdCliente(Integer v)            { this.idCliente = v; }

    public static class LineaPedido {
        private Integer idProducto;  // puede ser null si viene de fallback JS
        private String nombre;
        private float  precio;
        private int    cantidad;
        private float  total;

        public Integer getIdProducto()            { return idProducto; }
        public void setIdProducto(Integer v)      { this.idProducto = v; }
        public String getNombre()                 { return nombre; }
        public void setNombre(String v)           { this.nombre = v; }
        public float getPrecio()                  { return precio; }
        public void setPrecio(float v)            { this.precio = v; }
        public int getCantidad()                  { return cantidad; }
        public void setCantidad(int v)            { this.cantidad = v; }
        public float getTotal()                   { return total; }
        public void setTotal(float v)             { this.total = v; }
    }
}
