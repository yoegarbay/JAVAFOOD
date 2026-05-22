package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.productos.dto.PedidoAdminResponse;
import com.productos.dto.PedidoEditRequest;
import com.productos.dto.PedidoRequest;
import com.productos.dto.PedidoResponse;
import com.productos.exception.DataAccessException;
import com.productos.repository.PedidoRepository;

@RestController
@RequestMapping("/api/admin/pedidos")
public class PedidoAdminController {

    private final DataSource ds;
    public PedidoAdminController(DataSource ds) { this.ds = ds; }

    // GET /api/admin/pedidos
    @GetMapping
    public List<PedidoAdminResponse> index() {
        try (Connection con = ds.getConnection()) {
            return new PedidoRepository(con).findAllPedidoAdminResponse();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // GET /api/admin/pedidos/clientes  → lista de nombres distintos
    @GetMapping("/clientes")
    public List<String> clientes() {
        try (Connection con = ds.getConnection()) {
            return new PedidoRepository(con).findClientes();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // GET /api/admin/pedidos/{id}
    @GetMapping("/{id}")
    public PedidoAdminResponse show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            return new PedidoRepository(con).findById(id);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // PUT /api/admin/pedidos/{id}  → actualización completa
    @PutMapping("/{id}")
    public PedidoAdminResponse update(@PathVariable int id,
                                      @RequestBody PedidoEditRequest req) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            repo.updateFull(id, req);
            return repo.findById(id);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // POST /api/admin/pedidos  → pedido manual desde admin
    @PostMapping
    public PedidoResponse store(@RequestBody PedidoRequest request) {
        try (Connection con = ds.getConnection()) {
            return new PedidoRepository(con).guardarPedido(
                request.getItems(), request.getNombreCliente(), request.getMetodoPago(), null);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // DELETE /api/admin/pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            new PedidoRepository(con).delete(id);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}
