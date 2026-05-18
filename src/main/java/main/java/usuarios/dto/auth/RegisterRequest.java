package main.java.usuarios.dto.auth;

public record RegisterRequest(
	String nombre, 
	String apellidos, 
	String direccion,
	int telefono,
    String email,
	String contrasenya
) {}
