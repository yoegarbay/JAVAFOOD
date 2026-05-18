package main.java.usuarios.dto;

public record UsuarioDetalle(
	    int id,
	    String nombre,
	    String apellidos,
	    String direccion,
	    int telefono,
	    String email
	) {}
