package com.productos.dto;

public record ProductosDetalle(
	int id_producto,
	String nombre,
	float precio,
	Integer id_detalle,
	String categoria
) {}
