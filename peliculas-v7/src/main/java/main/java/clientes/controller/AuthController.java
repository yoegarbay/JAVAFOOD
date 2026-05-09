package main.java.clientes.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import main.java.clientes.dto.auth.LoginRequest;
import main.java.clientes.dto.auth.RegisterRequest;
import main.java.clientes.entity.Cliente;
import main.java.clientes.repository.ClienteRepository;
import main.java.clientes.exception.DataAccessException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final DataSource ds;

    public AuthController(DataSource ds) {
        this.ds = ds;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {

        try (Connection con = ds.getConnection()) {

            ClienteRepository repo = new ClienteRepository(con);

            Cliente cliente = new Cliente(
                null,
                req.nombre(),
                req.apellidos(),
                req.direccion(),
                req.telefono(),
                req.email(),
                req.contrasenya(),
                "CLIENTE"   //Poner automáticamente cliente, cuando 
                //alguien se registra es cliente salvo que el admin 
                //le otorgue otro error.
            );

            repo.insert(cliente);

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
    
    @PostMapping("/login")
    public Cliente login(@RequestBody LoginRequest req, HttpSession session) {

        try (Connection con = ds.getConnection()) {

            ClienteRepository repo = new ClienteRepository(con);

            Cliente usuario = repo.findByEmailAndPassword(
                req.email(),
                req.contrasenya()
            );

            if (usuario == null) {
                throw new RuntimeException("Email o contraseña incorrectos");
            }

           
            session.setAttribute("userId", usuario.getId());
            session.setAttribute("role", usuario.getRol());

            return usuario;

        } catch (SQLException e) {
            throw new DataAccessException("Error en login de base de datos", e);
        }
    }
}