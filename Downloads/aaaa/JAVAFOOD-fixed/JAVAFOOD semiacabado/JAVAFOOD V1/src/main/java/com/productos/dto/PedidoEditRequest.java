package com.productos.dto;

import java.util.List;

public class PedidoEditRequest {

    private String estado;
    private String nombreCliente;
    private String metodoPago;
    private List<LineaEdit> items;

    public String getEstado()                         { return estado; }
    public void   setEstado(String estado)            { this.estado = estado; }
    public String getNombreCliente()                  { return nombreCliente; }
    public void   setNombreCliente(String v)          { this.nombreCliente = v; }
    public String getMetodoPago()                     { return metodoPago; }
    public void   setMetodoPago(String v)             { this.metodoPago = v; }
    public List<LineaEdit> getItems()                 { return items; }
    public void   setItems(List<LineaEdit> items)     { this.items = items; }

    public static class LineaEdit {
        private String nombre;
        private int    cantidad;
        private float  precio;
        private float  total;

        public String getNombre()             { return nombre; }
        public void   setNombre(String v)     { this.nombre = v; }
        public int    getCantidad()           { return cantidad; }
        public void   setCantidad(int v)      { this.cantidad = v; }
        public float  getPrecio()             { return precio; }
        public void   setPrecio(float v)      { this.precio = v; }
        public float  getTotal()              { return total; }
        public void   setTotal(float v)       { this.total = v; }
    }
}
