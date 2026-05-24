package com.productos.controller;

import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.entity.Productos;
import com.productos.exception.DataAccessException;

/**
 * Gestión CRUD de promociones.
 * Usa siempre la categoría "Promociones" de la BD — nunca muestra
 * ensaladas, bebidas ni ningún otro producto de otra categoría.
 */
@RestController
@RequestMapping("/api/admin/promociones")
public class PromocionesAdminController {

    private final DataSource ds;
    public PromocionesAdminController(DataSource ds) { this.ds = ds; }

    // ── GET id_detalle de la categoría Promociones ────────────────────────────
    private int getIdDetallePromociones(Connection con) throws SQLException {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT dc.id_detalle FROM detalle_categoria dc " +
                "JOIN categorias c ON dc.id_categoria = c.id_categoria " +
                "WHERE c.nombre = 'Promociones' LIMIT 1")) {
            ResultSet rs = st.executeQuery();
            if (!rs.next()) throw new DataAccessException("Categoría 'Promociones' no encontrada en BD");
            return rs.getInt("id_detalle");
        }
    }

    // ── LIST ─────────────────────────────────────────────────────────────────
    @GetMapping
    public List<Productos> index() {
        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                 "SELECT p.id_producto, p.nombre, p.precio, IFNULL(p.stock, 999) AS stock, p.id_detalle " +
                 "FROM productos p " +
                 "JOIN detalle_categoria dc ON p.id_detalle = dc.id_detalle " +
                 "JOIN categorias c ON dc.id_categoria = c.id_categoria " +
                 "WHERE c.nombre = 'Promociones' ORDER BY p.nombre")) {
            ResultSet rs = st.executeQuery();
            List<Productos> lista = new ArrayList<>();
            while (rs.next())
                lista.add(new Productos(
                    rs.getInt("id_producto"), rs.getString("nombre"),
                    rs.getFloat("precio"), rs.getInt("id_detalle"),
                    rs.getInt("stock")));
            return lista;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // ── CREATE ────────────────────────────────────────────────────────────────
    @PostMapping
    public Map<String, Object> store(@RequestBody Map<String, Object> body) {
        String nombre = (String) body.getOrDefault("nombre", "");
        float  precio = ((Number) body.getOrDefault("precio", body.getOrDefault("puntos", 0))).floatValue();

        if (nombre.isBlank()) throw new DataAccessException("El nombre es obligatorio");

        try (Connection con = ds.getConnection()) {
            int idDetalle = getIdDetallePromociones(con);
            try (PreparedStatement st = con.prepareStatement(
                    "INSERT INTO productos (nombre, precio, stock, id_detalle) VALUES (?, ?, 999, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, nombre.trim());
                st.setFloat(2, precio);
                st.setInt(3, idDetalle);
                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                rs.next();
                return Map.of("id_producto", rs.getInt(1), "nombre", nombre, "precio", precio);
            }
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable int id,
                                       @RequestBody Map<String, Object> body) {
        String nombre = (String) body.getOrDefault("nombre", "");
        float  precio = ((Number) body.getOrDefault("precio", body.getOrDefault("puntos", 0))).floatValue();

        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                 "UPDATE productos SET nombre = ?, precio = ? WHERE id_producto = ?")) {
            st.setString(1, nombre.trim());
            st.setFloat(2, precio);
            st.setInt(3, id);
            if (st.executeUpdate() == 0) throw new DataAccessException("Promoción #" + id + " no encontrada");
            return Map.of("id_producto", id, "nombre", nombre, "precio", precio);
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                 "DELETE FROM productos WHERE id_producto = ?")) {
            st.setInt(1, id);
            if (st.executeUpdate() == 0) throw new DataAccessException("Promoción #" + id + " no encontrada");
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}