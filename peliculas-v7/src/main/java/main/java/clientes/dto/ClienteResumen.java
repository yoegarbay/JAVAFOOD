package main.java.clientes.dto;

public record ClienteResumen(
    int id,
    String nombre,
    String apellidos,
    String direccion,
    int telefono,
    String email,
    String rol
    
    
) {}
