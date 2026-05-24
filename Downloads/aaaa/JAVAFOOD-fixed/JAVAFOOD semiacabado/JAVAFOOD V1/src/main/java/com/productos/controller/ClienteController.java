package com.productos.controller;

import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.dto.ClienteRequest;
import com.productos.dto.ClienteResponse;
import com.productos.exception.DataAccessException;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final DataSource ds;
    public ClienteController(DataSource ds) { this.ds = ds; }

    // ── LOGIN ─────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ClienteResponse login(@RequestBody ClienteRequest req) {
        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                 "SELECT u.id, u.nom AS nombre, u.email, u.tipo, COALESCE(c.puntos, 0) AS puntos " +
                 "FROM usuario u " +
                 "LEFT JOIN cliente c ON c.id = u.id " +
                 "WHERE u.email = ? AND u.contrasena = ?")) {
            st.setString(1, req.getEmail().trim().toLowerCase());
            st.setString(2, req.getPassword());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) throw new DataAccessException("Email o contraseña incorrectos");
            return new ClienteResponse(
                rs.getInt("id"), rs.getString("nombre"),
                rs.getString("email"), rs.getString("tipo"), rs.getInt("puntos"));
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException("Error al iniciar sesión", e); }
    }

    // ── REGISTRO ──────────────────────────────────────────────────────────────
    @PostMapping("/registro")
    public ClienteResponse registro(@RequestBody ClienteRequest req) {
        if (req.getNombre() == null || req.getNombre().isBlank())
            throw new DataAccessException("El nombre es obligatorio");
        if (req.getEmail() == null || !req.getEmail().contains("@"))
            throw new DataAccessException("Email inválido");
        if (req.getPassword() == null || req.getPassword().length() < 4)
            throw new DataAccessException("La contraseña debe tener al menos 4 caracteres");

        try (Connection con = ds.getConnection()) {
            con.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement st = con.prepareStatement(
                        "INSERT INTO usuario (nom, apellidos, direccion, telefono, email, contrasena, tipo) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 'CLIENTE')", Statement.RETURN_GENERATED_KEYS)) {
                    st.setString(1, req.getNombre().trim());
                    st.setString(2, req.getApellidos() != null ? req.getApellidos().trim() : "");
                    st.setString(3, req.getDireccion() != null ? req.getDireccion().trim() : "");
                    st.setString(4, req.getTelefono()  != null ? req.getTelefono().trim()  : "");
                    st.setString(5, req.getEmail().trim().toLowerCase());
                    st.setString(6, req.getPassword());
                    st.executeUpdate();
                    ResultSet rs = st.getGeneratedKeys();
                    if (!rs.next()) throw new DataAccessException("Error al crear cuenta");
                    id = rs.getInt(1);
                }
                try (PreparedStatement st = con.prepareStatement(
                        "INSERT INTO cliente (id, puntos) VALUES (?, 0)")) {
                    st.setInt(1, id);
                    st.executeUpdate();
                }
                con.commit();
                return new ClienteResponse(id, req.getNombre().trim(),
                    req.getEmail().trim().toLowerCase(), "CLIENTE", 0);
            } catch (Exception e) { con.rollback(); throw e; }
            finally { con.setAutoCommit(true); }
        } catch (DataAccessException e) { throw e;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DataAccessException("El email ya está registrado");
        } catch (SQLException e) { throw new DataAccessException("Error al registrar", e); }
    }

    // ── GET PUNTOS ────────────────────────────────────────────────────────────
    @GetMapping("/{id}/puntos")
    public Map<String, Integer> getPuntos(@PathVariable int id) {
        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                 "SELECT puntos FROM cliente WHERE id = ?")) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) throw new DataAccessException("Cliente #" + id + " no encontrado");
            return Map.of("puntos", rs.getInt("puntos"));
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException("Error al obtener puntos", e); }
    }

    // ── UPDATE PUNTOS (delta positivo o negativo) ─────────────────────────────
    @PutMapping("/{id}/puntos")
    public Map<String, Integer> updatePuntos(@PathVariable int id,
                                              @RequestBody Map<String, Integer> body) {
        int delta = body.getOrDefault("delta", 0);
        try (Connection con = ds.getConnection()) {
            // Evitar puntos negativos
            try (PreparedStatement st = con.prepareStatement(
                    "UPDATE cliente SET puntos = GREATEST(0, puntos + ?) WHERE id = ?")) {
                st.setInt(1, delta);
                st.setInt(2, id);
                if (st.executeUpdate() == 0) throw new DataAccessException("Cliente #" + id + " no encontrado");
            }
            // Devolver el nuevo saldo
            try (PreparedStatement st = con.prepareStatement(
                    "SELECT puntos FROM cliente WHERE id = ?")) {
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                rs.next();
                return Map.of("puntos", rs.getInt("puntos"));
            }
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException("Error al actualizar puntos", e); }
    }

    // ── LISTA ─────────────────────────────────────────────────────────────────
    @GetMapping
    public List<ClienteResponse> index() {
        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                 "SELECT u.id, u.nom AS nombre, u.email, u.tipo, COALESCE(c.puntos,0) AS puntos " +
                 "FROM usuario u LEFT JOIN cliente c ON c.id = u.id ORDER BY u.nom")) {
            ResultSet rs = st.executeQuery();
            List<ClienteResponse> lista = new ArrayList<>();
            while (rs.next())
                lista.add(new ClienteResponse(rs.getInt("id"), rs.getString("nombre"),
                    rs.getString("email"), rs.getString("tipo"), rs.getInt("puntos")));
            return lista;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}