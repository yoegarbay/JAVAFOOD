package main.java.usuarios.dto;

public record UsuarioResumen(
    int id,
    String nombre,
    String apellidos,
    String direccion,
    int telefono,
    String email,
    String rol
    
    
) {}
