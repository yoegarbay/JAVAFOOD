package main.java.usuarios.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import main.java.usuarios.dto.UsuarioResumen;
import main.java.usuarios.entity.Usuario;
import main.java.usuarios.exceptions.DataAccessException;
import main.java.usuarios.repository.UsuarioRepository;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")

public class UsuarioController {
    private final DataSource ds;

    public UsuarioController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping
    public List<UsuarioResumen> index() {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            return repo.findAllResumen();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @GetMapping("/{id}")
    public Usuario show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @PutMapping("/{id}")
    public Usuario update(@RequestBody Usuario usuario, @PathVariable int id) {

        try (Connection con = ds.getConnection()) {

            UsuarioRepository repo = new UsuarioRepository(con);

            Usuario existente = repo.find(id);

            usuario.setId(id);
            usuario.setRol(existente.getRol());
            usuario.setContrasenya(existente.getContrasenya());

            repo.update(usuario);

            return usuario;

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @PutMapping("/{id}/rol")
    public void updateRol(@PathVariable int id, @RequestBody java.util.Map<String, String> body) {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            Usuario existente = repo.find(id);
            
            if (existente != null && body.containsKey("rol")) {
                existente.setRol(body.get("rol"));
                repo.update(existente);
            }
        } catch (SQLException e) {
            throw new main.java.usuarios.exceptions.DataAccessException(e);
        }
    }

    @PostMapping
    public Usuario store(@RequestBody Usuario usuario) {
        try (Connection con = ds.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(con);
            repo.insert(usuario);
            return usuario;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
