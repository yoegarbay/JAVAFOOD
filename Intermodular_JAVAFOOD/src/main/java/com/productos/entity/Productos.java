package com.productos.entity;

public class Productos {
	private Integer id_producto;
    private String nombre;
    private float precio;
    private Integer id_categoria;
    
	public Productos(Integer id_producto, String nombre, float precio, Integer id_categoria) {
		this.id_producto = id_producto;
		this.nombre = nombre;
		this.precio = precio;
		this.id_categoria = id_categoria;
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



	public void setPrecio(int precio) {
		this.precio = precio;
	}



	public Integer getId_categoria() {
		return id_categoria;
	}



	public void setId_categoria(Integer id_categoria) {
		this.id_categoria = id_categoria;
	}



	@Override
	public String toString() {
		return "Productos [id_producto=" + id_producto + ", nombre=" + nombre + ", precio=" + precio + ", id_categoria="
				+ id_categoria + "]";
	}
	
    
}
