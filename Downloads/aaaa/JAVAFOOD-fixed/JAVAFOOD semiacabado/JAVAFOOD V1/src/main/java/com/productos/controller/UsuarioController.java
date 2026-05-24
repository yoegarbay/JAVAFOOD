package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.productos.dto.UsuarioResumen;
import com.productos.entity.Usuario;
import com.productos.exception.DataAccessException;
import com.productos.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final DataSource ds;

    public UsuarioController(DataSource ds) {
        this.ds = ds;
    }

    // GET /api/usuarios → lista resumida para webAdmin
    @GetMapping
    public List<UsuarioResumen> index() {
        try (Connection con = ds.getConnection()) {
            return new UsuarioRepository(con).findAllResumen();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // GET /api/usuarios/{id} → detalle de un usuario
    @GetMapping("/{id}")
    public Usuario show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            return new UsuarioRepository(con).find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // POST /api/usuarios → crear usuario (desde webAdmin)
    @PostMapping
    public Usuario store(@RequestBody Usuario usuario) {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            int id = repo.insert(usuario);
            // Si es CLIENTE, insertar también en tabla cliente con puntos = 0
            if ("CLIENTE".equalsIgnoreCase(usuario.getTipo())) {
                repo.insertCliente(id);
            }
            usuario.setId(id);
            return usuario;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // PUT /api/usuarios/{id} → actualizar datos (no cambia contraseña ni tipo)
    @PutMapping("/{id}")
    public Usuario update(@PathVariable int id, @RequestBody Usuario usuario) {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            Usuario existente = repo.find(id);
            usuario.setId(id);
            // Conservar contraseña y tipo del registro original
            usuario.setContrasena(existente.getContrasena());
            usuario.setTipo(existente.getTipo());
            repo.update(usuario);
            return usuario;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // PUT /api/usuarios/{id}/rol → cambiar solo el rol
    @PutMapping("/{id}/rol")
    public void updateRol(@PathVariable int id, @RequestBody Map<String, String> body) {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            Usuario existente = repo.find(id);
            if (existente != null && body.containsKey("rol")) {
                existente.setTipo(body.get("rol"));
                repo.update(existente);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            new UsuarioRepository(con).delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
