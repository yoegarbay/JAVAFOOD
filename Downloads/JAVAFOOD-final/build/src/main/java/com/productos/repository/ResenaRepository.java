package com.productos.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.productos.dto.ResenaRequest;
import com.productos.dto.ResenaResponse;
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
}
