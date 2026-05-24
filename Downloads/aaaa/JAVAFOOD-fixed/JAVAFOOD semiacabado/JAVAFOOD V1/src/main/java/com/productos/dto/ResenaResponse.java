package com.productos.dto;

import java.util.List;

/** Respuesta de GET /api/resenas/producto/{id} */
public record ResenaResponse(
    double       promedio,   // 0.0 si no hay reseñas
    int          total,
    List<Item>   resenas
) {
    public record Item(
        int    id_resena,
        String nombre_cliente,
        int    puntuacion,
        String comentario,
        String fecha
    ) {}
}
