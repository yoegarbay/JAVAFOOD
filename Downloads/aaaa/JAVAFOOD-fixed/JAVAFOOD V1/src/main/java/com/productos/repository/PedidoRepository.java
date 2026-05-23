package com.productos.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.productos.dto.PedidoAdminResponse;
import com.productos.dto.PedidoEditRequest;
import com.productos.dto.PedidoRequest;
import com.productos.dto.PedidoResponse;
import com.productos.exception.DataAccessException;

public class PedidoRepository {

    private final Connection con;

    public PedidoRepository(Connection con) { this.con = con; }

    // ── CLIENTE: crear pedido desde pago.html ─────────────────────────────────
    public PedidoResponse guardarPedido(List<PedidoRequest.LineaPedido> items,
                                        String nombreCliente, String metodoPago,
                                        Integer idCliente) {
        if (items == null || items.isEmpty())
            throw new DataAccessException("El pedido está vacío");
        if (nombreCliente == null || nombreCliente.isBlank()) nombreCliente = "Cliente";
        metodoPago = "TARJETA".equalsIgnoreCase(metodoPago) ? "TARJETA" : "EFECTIVO";

        float total = (float) items.stream().mapToDouble(PedidoRequest.LineaPedido::getTotal).sum();

        try {
            con.setAutoCommit(false);

            // ── Validación de stock con SELECT FOR UPDATE ─────────────────────
            ProductosRepository prodRepo = new ProductosRepository(con);
            for (PedidoRequest.LineaPedido item : items) {
                if (item.getIdProducto() != null && item.getIdProducto() > 0) {
                    prodRepo.decrementarStock(item.getIdProducto(), item.getCantidad());
                }
            }

            // ── Insertar cabecera del pedido ──────────────────────────────────
            int idPedido;
            try (PreparedStatement st = con.prepareStatement(
                    "INSERT INTO pedidos (fecha, total, estado, nombre_cliente, metodo_pago, id_cliente) " +
                    "VALUES (NOW(), ?, 'PAGADO', ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                st.setFloat(1, total);
                st.setString(2, nombreCliente);
                st.setString(3, metodoPago);
                st.setObject(4, idCliente);
                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (!rs.next()) throw new DataAccessException("No se generó id");
                idPedido = rs.getInt(1);
            }

            // ── Insertar líneas del pedido ────────────────────────────────────
            try (PreparedStatement st = con.prepareStatement(
                    "INSERT INTO pedido_detalle (id_pedido, nombre_producto, cantidad, precio_unitario, subtotal) " +
                    "VALUES (?, ?, ?, ?, ?)")) {
                for (PedidoRequest.LineaPedido item : items) {
                    st.setInt(1, idPedido); st.setString(2, item.getNombre());
                    st.setInt(3, item.getCantidad()); st.setFloat(4, item.getPrecio());
                    st.setFloat(5, item.getTotal()); st.addBatch();
                }
                st.executeBatch();
            }

            con.commit();
            return new PedidoResponse(idPedido, java.time.LocalDateTime.now().toString(),
                total, "PAGADO", nombreCliente, metodoPago,
                "Pedido #" + idPedido + " guardado correctamente");

        } catch (DataAccessException dae) {
            try { con.rollback(); } catch (SQLException ex) { /* ignorar */ }
            throw dae;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { /* ignorar */ }
            throw new DataAccessException("Error al procesar el pedido", e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

    // ── CLIENTE: mis pedidos (filtrado por id_cliente) ────────────────────────
    public List<PedidoAdminResponse> findByCliente(int idCliente) {
        List<PedidoAdminResponse> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, fecha, total, estado, nombre_cliente, metodo_pago " +
                     "FROM pedidos WHERE id_cliente = ? ORDER BY fecha DESC";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idCliente);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_pedido");
                PedidoAdminResponse p = new PedidoAdminResponse(
                    id,
                    rs.getTimestamp("fecha").toLocalDateTime().toString(),
                    rs.getFloat("total"),
                    rs.getString("estado"),
                    new ArrayList<>(),
                    rs.getString("nombre_cliente"),
                    rs.getString("metodo_pago")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar pedidos del cliente #" + idCliente, e);
        }

        // Cargar líneas de cada pedido
        for (PedidoAdminResponse p : lista) {
            String sqlLineas = "SELECT id_linea, nombre_producto, cantidad, precio_unitario, subtotal " +
                               "FROM pedido_detalle WHERE id_pedido = ? ORDER BY id_linea";
            try (PreparedStatement st = con.prepareStatement(sqlLineas)) {
                st.setInt(1, p.getId_pedido());
                ResultSet rs = st.executeQuery();
                while (rs.next())
                    p.getLineas().add(new PedidoAdminResponse.LineaDetalle(
                        rs.getInt("id_linea"), rs.getString("nombre_producto"),
                        rs.getInt("cantidad"), rs.getFloat("precio_unitario"), rs.getFloat("subtotal")));
            } catch (SQLException e) {
                throw new DataAccessException("Error al cargar líneas del pedido #" + p.getId_pedido(), e);
            }
        }
        return lista;
    }

    // ── ADMIN: listar todos ───────────────────────────────────────────────────
    public List<PedidoAdminResponse> findAllPedidoAdminResponse() {
        List<PedidoAdminResponse> lista = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement(
                "SELECT id_pedido, fecha, total, estado FROM pedidos ORDER BY fecha DESC")) {
            ResultSet rs = st.executeQuery();
            while (rs.next())
                lista.add(new PedidoAdminResponse(rs.getInt("id_pedido"),
                    rs.getTimestamp("fecha").toLocalDateTime().toString(),
                    rs.getFloat("total"), rs.getString("estado"), null, null, null));
        } catch (SQLException e) { throw new DataAccessException("Error al listar pedidos", e); }
        return lista;
    }

    // ── ADMIN: detalle con líneas ─────────────────────────────────────────────
    public PedidoAdminResponse findById(int id) {
        PedidoAdminResponse pedido;
        try (PreparedStatement st = con.prepareStatement(
                "SELECT id_pedido, fecha, total, estado, nombre_cliente, metodo_pago " +
                "FROM pedidos WHERE id_pedido = ?")) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) throw new DataAccessException("Pedido #" + id + " no encontrado");
            pedido = new PedidoAdminResponse(rs.getInt("id_pedido"),
                rs.getTimestamp("fecha").toLocalDateTime().toString(),
                rs.getFloat("total"), rs.getString("estado"), new ArrayList<>(),
                rs.getString("nombre_cliente"), rs.getString("metodo_pago"));
        } catch (SQLException e) { throw new DataAccessException("Error al buscar pedido #" + id, e); }

        try (PreparedStatement st = con.prepareStatement(
                "SELECT id_linea, nombre_producto, cantidad, precio_unitario, subtotal " +
                "FROM pedido_detalle WHERE id_pedido = ? ORDER BY id_linea")) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next())
                pedido.getLineas().add(new PedidoAdminResponse.LineaDetalle(
                    rs.getInt("id_linea"), rs.getString("nombre_producto"),
                    rs.getInt("cantidad"), rs.getFloat("precio_unitario"), rs.getFloat("subtotal")));
        } catch (SQLException e) { throw new DataAccessException("Error al cargar líneas #" + id, e); }
        return pedido;
    }

    // ── ADMIN: actualizar pedido completo ─────────────────────────────────────
    public void updateFull(int id, PedidoEditRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty())
            throw new DataAccessException("El pedido debe tener al menos un producto");

        float nuevoTotal = (float) req.getItems().stream()
                .mapToDouble(PedidoEditRequest.LineaEdit::getTotal).sum();

        try {
            con.setAutoCommit(false);

            try (PreparedStatement st = con.prepareStatement(
                    "UPDATE pedidos SET estado=?, nombre_cliente=?, metodo_pago=?, total=? " +
                    "WHERE id_pedido=?")) {
                st.setString(1, req.getEstado().toUpperCase());
                st.setString(2, req.getNombreCliente() == null || req.getNombreCliente().isBlank()
                    ? "Cliente" : req.getNombreCliente());
                st.setString(3, "TARJETA".equalsIgnoreCase(req.getMetodoPago()) ? "TARJETA" : "EFECTIVO");
                st.setFloat(4, nuevoTotal);
                st.setInt(5, id);
                if (st.executeUpdate() == 0) throw new DataAccessException("Pedido #" + id + " no encontrado");
            }

            try (PreparedStatement st = con.prepareStatement(
                    "DELETE FROM pedido_detalle WHERE id_pedido=?")) {
                st.setInt(1, id); st.executeUpdate();
            }
            try (PreparedStatement st = con.prepareStatement(
                    "INSERT INTO pedido_detalle (id_pedido, nombre_producto, cantidad, precio_unitario, subtotal) " +
                    "VALUES (?, ?, ?, ?, ?)")) {
                for (PedidoEditRequest.LineaEdit item : req.getItems()) {
                    st.setInt(1, id); st.setString(2, item.getNombre());
                    st.setInt(3, item.getCantidad()); st.setFloat(4, item.getPrecio());
                    st.setFloat(5, item.getTotal()); st.addBatch();
                }
                st.executeBatch();
            }

            con.commit();
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { /* ignorar */ }
            throw new DataAccessException("Error al actualizar pedido #" + id, e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { /* ignorar */ }
        }
    }

    // ── ADMIN: solo estado ────────────────────────────────────────────────────
    public void updateEstado(int id, String estado) {
        try (PreparedStatement st = con.prepareStatement(
                "UPDATE pedidos SET estado=? WHERE id_pedido=?")) {
            st.setString(1, estado); st.setInt(2, id);
            if (st.executeUpdate() == 0) throw new DataAccessException("Pedido #" + id + " no encontrado");
        } catch (SQLException e) { throw new DataAccessException("Error al actualizar estado #" + id, e); }
    }

    // ── ADMIN: nombres de clientes distintos ──────────────────────────────────
    public List<String> findClientes() {
        List<String> lista = new ArrayList<>();
        try (PreparedStatement st = con.prepareStatement(
                "SELECT DISTINCT nombre_cliente FROM pedidos ORDER BY nombre_cliente")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) lista.add(rs.getString("nombre_cliente"));
        } catch (SQLException e) { throw new DataAccessException("Error al listar clientes", e); }
        return lista;
    }

    // ── ADMIN: eliminar ───────────────────────────────────────────────────────
    public void delete(int id) {
        try (PreparedStatement st = con.prepareStatement(
                "DELETE FROM pedidos WHERE id_pedido=?")) {
            st.setInt(1, id);
            if (st.executeUpdate() == 0) throw new DataAccessException("Pedido #" + id + " no encontrado");
        } catch (SQLException e) { throw new DataAccessException("Error al eliminar pedido #" + id, e); }
    }
}