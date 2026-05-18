package main.java.usuarios.dto.auth;

public record LoginRequest(
	String email,
	String contrasenya
) {}
