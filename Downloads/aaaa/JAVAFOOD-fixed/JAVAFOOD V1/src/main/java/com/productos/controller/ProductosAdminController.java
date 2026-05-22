package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.productos.entity.Productos;
import com.productos.exception.DataAccessException;
import com.productos.repository.ProductosRepository;

@RestController
@RequestMapping("/api/admin/peliculas")
public class ProductosAdminController {
	private final DataSource ds;

    public ProductosAdminController(DataSource ds) {
    	this.ds = ds;
    }
    
    @GetMapping
    public List<Productos> index() throws SQLException {
    	try (Connection con = ds.getConnection()) {
    	    ProductosRepository repo = new ProductosRepository(con);
    	    return repo.findAll();
    	 } catch (SQLException e) {
    	        throw new DataAccessException(e);
    	 }
    }
    
    @GetMapping("/{id}")
    public Productos show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    public Productos store(@RequestBody Productos producto) {
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            repo.insert(producto);
            return producto;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PutMapping("/{id}")
    public Productos update(@PathVariable int id, @RequestBody Productos producto) {
    	System.out.println(producto);
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            producto.setId_producto(id);
            repo.update(producto);
            return producto;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
