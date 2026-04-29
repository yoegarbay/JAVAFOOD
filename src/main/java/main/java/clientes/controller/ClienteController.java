package main.java.clientes.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import main.java.clientes.dto.ClienteResumen;
import main.java.clientes.entity.Cliente;
import main.java.clientes.exception.DataAccessException;
import main.java.clientes.repository.ClienteRepository;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")

public class ClienteController {
    private final DataSource ds;

    public ClienteController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping
    public List<ClienteResumen> index() {
        try (Connection con = ds.getConnection()) {
            ClienteRepository repo = new ClienteRepository(con);
            return repo.findAllResumen();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @GetMapping("/{id}")
    public Cliente show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            ClienteRepository repo = new ClienteRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            ClienteRepository repo = new ClienteRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @PutMapping("/{id}")
    public Cliente update(@RequestBody Cliente cliente, @PathVariable int id) {

        try (Connection con = ds.getConnection()) {

            ClienteRepository repo = new ClienteRepository(con);

            Cliente existente = repo.find(id);

            cliente.setId(id);
            cliente.setRol(existente.getRol());
            cliente.setContrasenya(existente.getContrasenya());

            repo.update(cliente);

            return cliente;

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @PutMapping("/{id}/rol")
    public void updateRol(@PathVariable int id, @RequestBody java.util.Map<String, String> body) {
        try (Connection con = ds.getConnection()) {
            ClienteRepository repo = new ClienteRepository(con);
            Cliente existente = repo.find(id);
            
            if (existente != null && body.containsKey("rol")) {
                existente.setRol(body.get("rol"));
                repo.update(existente);
            }
        } catch (SQLException e) {
            throw new main.java.clientes.exception.DataAccessException(e);
        }
    }

    @PostMapping
    public Cliente store(@RequestBody Cliente cliente) {
        try (Connection con = ds.getConnection()) {
            ClienteRepository repo = new ClienteRepository(con);
            repo.insert(cliente);
            return cliente;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
