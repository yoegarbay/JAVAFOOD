package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.productos.dto.PedidoRequest;
import com.productos.dto.PedidoResponse;
import com.productos.exception.DataAccessException;
import com.productos.repository.PedidoRepository;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final DataSource ds;

    public PedidoController(DataSource ds) {
        this.ds = ds;
    }

    /**
     * POST /api/pedidos
     * Body JSON:
     * {
     *   "nombreCliente": "Juan",
     *   "metodoPago": "TARJETA",
     *   "items": [
     *     { "nombre": "Nachos", "precio": 4.80, "cantidad": 2, "total": 9.60 }
     *   ]
     * }
     */
    @PostMapping
    public PedidoResponse pagar(@RequestBody PedidoRequest request) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.guardarPedido(
                request.getItems(),
                request.getNombreCliente(),
                request.getMetodoPago()
            );
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}