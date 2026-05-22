package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.productos.dto.ProductosDetalle;
import com.productos.dto.ProductosResumen;
import com.productos.exception.DataAccessException;
import com.productos.repository.ProductosRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {
	private final DataSource ds;
    public ProductosController(DataSource ds) {
        this.ds = ds;
    }
    
    @GetMapping
    public List<ProductosResumen> index() {
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            return repo.findResumen();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @GetMapping("/categoria/{categoria}")
    public List<ProductosResumen> indexByCategoria(@PathVariable String categoria) {
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            return repo.findResumenByCategoria(categoria);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @GetMapping("/{id}")
    public ProductosDetalle show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            return repo.findDetalle(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
