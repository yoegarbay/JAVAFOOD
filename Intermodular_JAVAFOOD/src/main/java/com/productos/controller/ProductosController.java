package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.dto.ProductosResumen;
import com.productos.exception.DataAccessException;
import com.productos.repository.ProductosRepository;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") 
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
}
