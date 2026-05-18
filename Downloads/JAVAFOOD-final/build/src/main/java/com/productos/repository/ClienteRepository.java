package com.productos.repository;

import java.sql.*;
import com.productos.dto.ClienteResponse;
import com.productos.exception.DataAccessException;

public class ClienteRepository {

    private final Connection con;
    public ClienteRepository(Connection con) { this.con = con; }

    public ClienteResponse register(String nombre, String email, String password) {
        try (PreparedStatement st = con.prepareStatement(
                "INSERT INTO clientes (nombre, email, password) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, nombre);
            st.setString(2, email);
            st.setString(3, password);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (!rs.next()) throw new DataAccessException("Error al crear cliente");
            return new ClienteResponse(rs.getInt(1), nombre, email);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DataAccessException("El email ya está registrado");
        } catch (SQLException e) {
            throw new DataAccessException("Error al registrar cliente", e);
        }
    }

    public ClienteResponse login(String email, String password) {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT id_cliente, nombre, email FROM clientes WHERE email=? AND password=?")) {
            st.setString(1, email);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) throw new DataAccessException("Email o contraseña incorrectos");
            return new ClienteResponse(rs.getInt("id_cliente"), rs.getString("nombre"), rs.getString("email"));
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException("Error al iniciar sesión", e);
        }
    }
}
