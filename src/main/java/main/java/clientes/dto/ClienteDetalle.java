package main.java.clientes.dto;

public record ClienteDetalle(
	    int id,
	    String nombre,
	    String apellidos,
	    String direccion,
	    int telefono,
	    String email
	) {}
