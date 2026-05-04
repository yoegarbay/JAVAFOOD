package com.productos.dto;

// Record Java: clase inmutable para devolver la respuesta del pedido
public record PedidoResponse(
    int    id_pedido,
    String fecha,
    float  total,
    String estado,
    String nombreCliente,
    String metodoPago,
    String mensaje
) {}