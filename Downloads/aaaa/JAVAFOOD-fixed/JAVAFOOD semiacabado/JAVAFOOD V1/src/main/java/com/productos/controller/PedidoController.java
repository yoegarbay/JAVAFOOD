package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.productos.dto.PedidoAdminResponse;
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
     */
    @PostMapping
    public PedidoResponse pagar(@RequestBody PedidoRequest request) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.guardarPedido(
                request.getItems(),
                request.getNombreCliente(),
                request.getMetodoPago(),
                request.getIdCliente()
            );
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * GET /api/pedidos/mis/{idCliente}
     * Devuelve solo los pedidos del cliente logueado.
     * El frontend lee idCliente de sessionStorage y llama a este endpoint.
     */
    @GetMapping("/mis/{idCliente}")
    public List<PedidoAdminResponse> misPedidos(@PathVariable int idCliente) {
        try (Connection con = ds.getConnection()) {
            return new PedidoRepository(con).findByCliente(idCliente);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}