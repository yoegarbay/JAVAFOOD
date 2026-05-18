package com.productos.controller;

import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.dto.ClienteRequest;
import com.productos.dto.ClienteResponse;
import com.productos.exception.DataAccessException;
import com.productos.repository.ClienteRepository;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final DataSource ds;
    public ClienteController(DataSource ds) { this.ds = ds; }

    // GET /api/clientes → lista todos los clientes (nombre + id)
    @GetMapping
    public List<ClienteResponse> index() {
        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(
                 "SELECT id_cliente, nombre, email FROM clientes ORDER BY nombre")) {
            ResultSet rs = st.executeQuery();
            List<ClienteResponse> lista = new ArrayList<>();
            while (rs.next())
                lista.add(new ClienteResponse(
                    rs.getInt("id_cliente"), rs.getString("nombre"), rs.getString("email")));
            return lista;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @PostMapping("/registro")
    public ClienteResponse registro(@RequestBody ClienteRequest req) {
        if (req.getNombre() == null || req.getNombre().isBlank())
            throw new DataAccessException("El nombre es obligatorio");
        if (req.getEmail() == null || !req.getEmail().contains("@"))
            throw new DataAccessException("Email inválido");
        if (req.getPassword() == null || req.getPassword().length() < 4)
            throw new DataAccessException("La contraseña debe tener al menos 4 caracteres");
        try (Connection con = ds.getConnection()) {
            return new ClienteRepository(con).register(
                req.getNombre().trim(), req.getEmail().trim().toLowerCase(), req.getPassword());
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @PostMapping("/login")
    public ClienteResponse login(@RequestBody ClienteRequest req) {
        try (Connection con = ds.getConnection()) {
            return new ClienteRepository(con).login(
                req.getEmail().trim().toLowerCase(), req.getPassword());
        } catch (DataAccessException e) { throw e;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}