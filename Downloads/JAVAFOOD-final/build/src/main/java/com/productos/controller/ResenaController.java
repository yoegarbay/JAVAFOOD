package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.productos.dto.ResenaRequest;
import com.productos.dto.ResenaResponse;
import com.productos.exception.DataAccessException;
import com.productos.repository.ResenaRepository;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final DataSource ds;

    public ResenaController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping("/producto/{id}")
    public ResenaResponse getByProducto(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            return new ResenaRepository(con).findByProducto(id);
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @GetMapping("/puede/{idProducto}")
    public Map<String, Boolean> puede(@PathVariable int idProducto,
                                      @RequestParam int idCliente) {
        try (Connection con = ds.getConnection()) {
            ResenaRepository repo = new ResenaRepository(con);
            boolean yaReseno = repo.yaReseno(idCliente, idProducto);
            boolean puede    = !yaReseno && repo.puedeResenar(idCliente, idProducto);
            return Map.of("puede", puede, "yaReseno", yaReseno);
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    public ResenaResponse.Item crear(@RequestBody ResenaRequest req) {
        System.out.println(">>> POST /api/resenas recibido");
        System.out.println(">>> idCliente="  + req.getIdCliente());
        System.out.println(">>> idProducto=" + req.getIdProducto());
        System.out.println(">>> puntuacion=" + req.getPuntuacion());
        System.out.println(">>> comentario=" + req.getComentario());

        if (req.getIdCliente() == null || req.getIdProducto() == null)
            throw new DataAccessException("idCliente e idProducto son obligatorios");

        try (Connection con = ds.getConnection()) {
            return new ResenaRepository(con).guardar(req);
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}