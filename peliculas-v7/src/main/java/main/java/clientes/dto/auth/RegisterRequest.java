package main.java.clientes.dto.auth;

public record RegisterRequest(
	String nombre, 
	String apellidos, 
	String direccion,
	int telefono,
    String email,
	String contrasenya
) {}
