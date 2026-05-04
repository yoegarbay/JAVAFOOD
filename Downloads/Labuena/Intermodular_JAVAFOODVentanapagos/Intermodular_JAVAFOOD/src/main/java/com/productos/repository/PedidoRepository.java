package com.productos.repository;

import java.sql.*;
import java.util.List;

import com.productos.dto.PedidoRequest;
import com.productos.dto.PedidoResponse;
import com.productos.exception.DataAccessException;

public class PedidoRepository {

    private final Connection con;

    public PedidoRepository(Connection con) {
        this.con = con;
    }

    /**
     * Guarda el pedido completo (cabecera + líneas) en una transacción.
     * Ahora también guarda nombre_cliente y metodo_pago.
     */
    public PedidoResponse guardarPedido(List<PedidoRequest.LineaPedido> items,
                                        String nombreCliente,
                                        String metodoPago) {

        // Validaciones básicas
        if (items == null || items.isEmpty()) {
            throw new DataAccessException("El pedido está vacío");
        }
        if (nombreCliente == null || nombreCliente.isBlank()) {
            nombreCliente = "Cliente";
        }
        // Solo aceptamos EFECTIVO o TARJETA, por seguridad
        if (!"TARJETA".equalsIgnoreCase(metodoPago)) {
            metodoPago = "EFECTIVO";
        } else {
            metodoPago = "TARJETA";
        }

        // Calcular total sumando los subtotales de cada línea
        float total = (float) items.stream()
                .mapToDouble(PedidoRequest.LineaPedido::getTotal)
                .sum();

        try {
            con.setAutoCommit(false); // Iniciamos transacción manual

            // ── 1. Insertar cabecera del pedido ──────────────────────
            String sqlPedido =
                "INSERT INTO pedidos (fecha, total, estado, nombre_cliente, metodo_pago) " +
                "VALUES (NOW(), ?, 'PAGADO', ?, ?)";

            int idPedido;
            try (PreparedStatement stmt = con.prepareStatement(
                    sqlPedido, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setFloat(1, total);
                stmt.setString(2, nombreCliente);
                stmt.setString(3, metodoPago);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (!rs.next()) throw new DataAccessException("No se generó id para el pedido");
                idPedido = rs.getInt(1);
            }

            // ── 2. Insertar líneas del pedido (batch) ────────────────
            String sqlLinea =
                "INSERT INTO pedido_detalle " +
                "(id_pedido, nombre_producto, cantidad, precio_unitario, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = con.prepareStatement(sqlLinea)) {
                for (PedidoRequest.LineaPedido item : items) {
                    stmt.setInt(1, idPedido);
                    stmt.setString(2, item.getNombre());
                    stmt.setInt(3, item.getCantidad());
                    stmt.setFloat(4, item.getPrecio());
                    stmt.setFloat(5, item.getTotal());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            con.commit(); // Todo OK → confirmamos

            String fechaStr = java.time.LocalDateTime.now().toString();
            return new PedidoResponse(
                idPedido, fechaStr, total, "PAGADO",
                nombreCliente, metodoPago,
                "Pedido #" + idPedido + " guardado correctamente"
            );

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { /* ignorar rollback fallido */ }
            throw new DataAccessException("Error al guardar el pedido", e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }
}