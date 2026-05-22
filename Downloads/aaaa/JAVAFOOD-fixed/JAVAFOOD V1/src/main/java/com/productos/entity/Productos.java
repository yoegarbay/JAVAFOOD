package com.productos.entity;

public class Productos {
    private Integer id_producto;
    private String nombre;
    private float precio;
    private Integer id_detalle;
    private int stock;

    public Productos() {}

    public Productos(Integer id_producto, String nombre, float precio, Integer id_detalle) {
        this.id_producto = id_producto;
        this.nombre      = nombre;
        this.precio      = precio;
        this.id_detalle  = id_detalle;
        this.stock       = 15;
    }

    public Productos(Integer id_producto, String nombre, float precio, Integer id_detalle, int stock) {
        this.id_producto = id_producto;
        this.nombre      = nombre;
        this.precio      = precio;
        this.id_detalle  = id_detalle;
        this.stock       = stock;
    }

    public Integer getId_producto()              { return id_producto; }
    public void setId_producto(Integer v)        { this.id_producto = v; }
    public String getNombre()                    { return nombre; }
    public void setNombre(String v)              { this.nombre = v; }
    public float getPrecio()                     { return precio; }
    public void setPrecio(float v)               { this.precio = v; }
    public Integer getId_detalle()               { return id_detalle; }
    public void setId_detalle(Integer v)         { this.id_detalle = v; }
    public int getStock()                        { return stock; }
    public void setStock(int v)                  { this.stock = v; }

    @Override
    public String toString() {
        return "Productos[id=" + id_producto + ", nombre=" + nombre + ", precio=" + precio
                + ", id_detalle=" + id_detalle + ", stock=" + stock + "]";
    }
}
