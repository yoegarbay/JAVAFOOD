package com.productos.fichar.dto;

public record FichajeConNombre(
    int id, String nombre, String iniciales, String color,
    String tipo, String fecha, String hora, Double horasCalc
) {}
