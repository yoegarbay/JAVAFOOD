package com.productos.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.productos.dto.ResenaRequest;
import com.productos.dto.ResenaResponse;
import com.productos.dto.ResenaAdminResponse;
import com.productos.exception.DataAccessException;

public class ResenaRepository {

    private final Connection con;

    public ResenaRepository(Connection con) {
        this.con = con;
    }

    // ── Listar reseñas de un producto + promedio ──────────────────────────────
    public ResenaResponse findByProducto(int idProducto) {
        List<ResenaResponse.Item> lista = new ArrayList<>();

        String sqlLista = """
            SELECT r.id_resena, c.nombre AS nombre_cliente,
                   r.puntuacion, r.comentario, r.fecha
            FROM resenas r
            JOIN clientes c ON r.id_cliente = c.id_cliente
            WHERE r.id_producto = ?
            ORDER BY r.fecha DESC
            """;

        try (PreparedStatement st = con.prepareStatement(sqlLista)) {
            st.setInt(1, idProducto);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(new ResenaResponse.Item(
                    rs.getInt("id_resena"),
                    rs.getString("nombre_cliente"),
                    rs.getInt("puntuacion"),
                    rs.getString("comentario"),
                    rs.getTimestamp("fecha").toLocalDateTime().toString()
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar reseñas del producto " + idProducto, e);
        }

        // Promedio
        double promedio = 0.0;
        int    total    = lista.size();

        if (total > 0) {
            String sqlProm = "SELECT AVG(puntuacion) FROM resenas WHERE id_producto = ?";
            try (PreparedStatement st = con.prepareStatement(sqlProm)) {
                st.setInt(1, idProducto);
                ResultSet rs = st.executeQuery();
                if (rs.next()) promedio = rs.getDouble(1);
            } catch (SQLException e) {
                throw new DataAccessException("Error al calcular promedio", e);
            }
        }

        return new ResenaResponse(
            Math.round(promedio * 10.0) / 10.0,   // 1 decimal
            total,
            lista
        );
    }

    // ── Comprueba si el cliente puede reseñar ─────────────────────────────────
    // Condición: tener al menos un pedido con ese producto Y no haberlo reseñado ya
    public boolean puedeResenar(int idCliente, int idProducto) {
        if (yaReseno(idCliente, idProducto)) return false;
        return haPedido(idCliente, idProducto);
    }

    public boolean yaReseno(int idCliente, int idProducto) {
        String sql = "SELECT COUNT(*) FROM resenas WHERE id_cliente = ? AND id_producto = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idCliente);
            st.setInt(2, idProducto);
            ResultSet rs = st.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al comprobar reseña existente", e);
        }
    }

    // Busca un pedido del cliente que contenga el producto (por nombre o por id_cliente en pedidos)
    private boolean haPedido(int idCliente, int idProducto) {
        String sql = """
            SELECT COUNT(*)
            FROM pedidos p
            JOIN pedido_detalle pd ON p.id_pedido = pd.id_pedido
            JOIN productos      pr ON pr.nombre   = pd.nombre_producto
            WHERE p.id_cliente = ? AND pr.id_producto = ?
            """;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, idCliente);
            st.setInt(2, idProducto);
            ResultSet rs = st.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al verificar pedido del cliente", e);
        }
    }

    // ── Guardar reseña ────────────────────────────────────────────────────────
    public ResenaResponse.Item guardar(ResenaRequest req) {
        int idCliente  = req.getIdCliente();
        int idProducto = req.getIdProducto();
        int puntuacion = req.getPuntuacion();

        if (puntuacion < 1 || puntuacion > 5)
            throw new DataAccessException("La puntuación debe estar entre 1 y 5");
        if (!haPedido(idCliente, idProducto))
            throw new DataAccessException("Debes haber pedido este producto para poder reseñarlo");
        if (yaReseno(idCliente, idProducto))
            throw new DataAccessException("Ya has reseñado este producto");

        String sql = """
            INSERT INTO resenas (id_producto, id_cliente, puntuacion, comentario, fecha)
            VALUES (?, ?, ?, ?, NOW())
            """;
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, idProducto);
            st.setInt(2, idCliente);
            st.setInt(3, puntuacion);
            st.setString(4, req.getComentario() != null ? req.getComentario().trim() : null);
            st.executeUpdate();

            ResultSet keys = st.getGeneratedKeys();
            if (!keys.next()) throw new DataAccessException("No se generó id de reseña");
            int idResena = keys.getInt(1);

            // Recuperar nombre del cliente para devolverlo en la respuesta
            String nombre = getNombreCliente(idCliente);
            String fecha  = java.time.LocalDateTime.now().toString();

            return new ResenaResponse.Item(idResena, nombre, puntuacion,
                req.getComentario(), fecha);

        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry"))
                throw new DataAccessException("Ya has reseñado este producto");
            throw new DataAccessException("Error al guardar la reseña", e);
        }
    }

    private String getNombreCliente(int idCliente) {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT nombre FROM clientes WHERE id_cliente = ?")) {
            st.setInt(1, idCliente);
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getString(1) : "Cliente";
        } catch (SQLException e) {
            return "Cliente";
        }
    }

    // ── ADMIN: listar todas las reseñas ──────────────────────────────────────
    public List<ResenaAdminResponse> findAll() {
        List<ResenaAdminResponse> lista = new ArrayList<>();
        String sql = """
            SELECT r.id_resena, r.id_producto, p.nombre AS nombre_producto,
                   r.id_cliente, c.nombre AS nombre_cliente,
                   r.puntuacion, r.comentario, r.fecha
            FROM resenas r
            JOIN productos p ON p.id_producto = r.id_producto
            JOIN clientes  c ON c.id_cliente  = r.id_cliente
            ORDER BY r.fecha DESC
            """;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(new ResenaAdminResponse(
                    rs.getInt("id_resena"),
                    rs.getInt("id_producto"),
                    rs.getString("nombre_producto"),
                    rs.getInt("id_cliente"),
                    rs.getString("nombre_cliente"),
                    rs.getInt("puntuacion"),
                    rs.getString("comentario"),
                    rs.getTimestamp("fecha").toLocalDateTime().toString()
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar reseñas", e);
        }
        return lista;
    }

    // ── ADMIN: obtener reseña por id ─────────────────────────────────────────
    public ResenaAdminResponse findById(int id) {
        String sql = """
            SELECT r.id_resena, r.id_producto, p.nombre AS nombre_producto,
                   r.id_cliente, c.nombre AS nombre_cliente,
                   r.puntuacion, r.comentario, r.fecha
            FROM resenas r
            JOIN productos p ON p.id_producto = r.id_producto
            JOIN clientes  c ON c.id_cliente  = r.id_cliente
            WHERE r.id_resena = ?
            """;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) throw new DataAccessException("Reseña #" + id + " no encontrada");
            return new ResenaAdminResponse(
                rs.getInt("id_resena"),
                rs.getInt("id_producto"),
                rs.getString("nombre_producto"),
                rs.getInt("id_cliente"),
                rs.getString("nombre_cliente"),
                rs.getInt("puntuacion"),
                rs.getString("comentario"),
                rs.getTimestamp("fecha").toLocalDateTime().toString()
            );
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException("Error al obtener reseña #" + id, e);
        }
    }

    // ── ADMIN: actualizar reseña (puntuación + comentario) ───────────────────
    public void update(int id, int puntuacion, String comentario) {
        if (puntuacion < 1 || puntuacion > 5)
            throw new DataAccessException("La puntuación debe estar entre 1 y 5");
        String sql = "UPDATE resenas SET puntuacion = ?, comentario = ? WHERE id_resena = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, puntuacion);
            st.setString(2, comentario != null ? comentario.trim() : null);
            st.setInt(3, id);
            if (st.executeUpdate() == 0)
                throw new DataAccessException("Reseña #" + id + " no encontrada");
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar reseña #" + id, e);
        }
    }

    // ── ADMIN: eliminar reseña ────────────────────────────────────────────────
    public void delete(int id) {
        String sql = "DELETE FROM resenas WHERE id_resena = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            if (st.executeUpdate() == 0)
                throw new DataAccessException("Reseña #" + id + " no encontrada");
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar reseña #" + id, e);
        }
    }

    // ── ADMIN: crear reseña sin validación de pedido ─────────────────────────
    public ResenaAdminResponse adminGuardar(int idCliente, int idProducto,
                                            int puntuacion, String comentario) {
        if (puntuacion < 1 || puntuacion > 5)
            throw new DataAccessException("La puntuación debe estar entre 1 y 5");
        if (yaReseno(idCliente, idProducto))
            throw new DataAccessException("Este cliente ya ha reseñado este producto");

        String sql = """
            INSERT INTO resenas (id_producto, id_cliente, puntuacion, comentario, fecha)
            VALUES (?, ?, ?, ?, NOW())
            """;
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, idProducto);
            st.setInt(2, idCliente);
            st.setInt(3, puntuacion);
            st.setString(4, comentario != null ? comentario.trim() : null);
            st.executeUpdate();
            ResultSet keys = st.getGeneratedKeys();
            if (!keys.next()) throw new DataAccessException("No se generó id de reseña");
            return findById(keys.getInt(1));
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry"))
                throw new DataAccessException("Este cliente ya ha reseñado este producto");
            throw new DataAccessException("Error al crear la reseña", e);
        }
    }
}
