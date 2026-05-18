package com.productos.entity;

public class Productos {
	private Integer id_producto;
    private String nombre;
    private float precio;
    private Integer id_detalle;
    
	public Productos(Integer id_producto, String nombre, float precio, Integer id_detalle) {
		this.id_producto = id_producto;
		this.nombre = nombre;
		this.precio = precio;
		this.id_detalle = id_detalle;
	}

	public Integer getId_producto() {
		return id_producto;
	}

	public void setId_producto(Integer id_producto) {
		this.id_producto = id_producto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public Integer getId_detalle() {
		return id_detalle;
	}

	public void setId_detalle(Integer id_detalle) {
		this.id_detalle = id_detalle;
	}

	@Override
	public String toString() {
		return "Productos [id_producto=" + id_producto + ", nombre=" + nombre + ", precio=" + precio + ", id_detalle="
				+ id_detalle + "]";
	}
}
