package com.productos.dto;

import java.util.List;

public class PedidoAdminResponse {

    private int    id_pedido;
    private String fecha;
    private float  total;
    private String estado;
    private List<LineaDetalle> lineas;
    private String nombreCliente;
    private String metodoPago;

    public PedidoAdminResponse() {}

    // Me cargaría este para estar obligado a usar el constructor con todos los parámetros
    /* 
    public PedidoAdminResponse(int id_pedido, String fecha, float total,
                                String estado, List<LineaDetalle> lineas) {
        this.id_pedido = id_pedido; this.fecha = fecha;
        this.total = total; this.estado = estado; this.lineas = lineas;
    }
    */
    public PedidoAdminResponse(int id_pedido, String fecha, float total, String estado, List<LineaDetalle> lineas, String nombreCliente, String metodoPago) {
        this.id_pedido = id_pedido;
        this.fecha=fecha;
        this.total=total;
        this.estado=estado;
        this.lineas=lineas;
        this.nombreCliente = nombreCliente;
        this.metodoPago    = metodoPago;
    }

    public int    getId_pedido()           { return id_pedido; }
    public String getFecha()               { return fecha; }
    public float  getTotal()               { return total; }
    public String getEstado()              { return estado; }
    public List<LineaDetalle> getLineas()  { return lineas; }
    public String getNombreCliente()       { return nombreCliente; }
    public String getMetodoPago()          { return metodoPago; }

    public static class LineaDetalle {
        private int    id_linea;
        private String nombre_producto;
        private int    cantidad;
        private float  precio_unitario;
        private float  subtotal;

        public LineaDetalle() {}
        public LineaDetalle(int id_linea, String nombre_producto,
                            int cantidad, float precio_unitario, float subtotal) {
            this.id_linea = id_linea; this.nombre_producto = nombre_producto;
            this.cantidad = cantidad; this.precio_unitario = precio_unitario;
            this.subtotal = subtotal;
        }
        public int    getId_linea()        { return id_linea; }
        public String getNombre_producto() { return nombre_producto; }
        public int    getCantidad()        { return cantidad; }
        public float  getPrecio_unitario() { return precio_unitario; }
        public float  getSubtotal()        { return subtotal; }
    }
}
