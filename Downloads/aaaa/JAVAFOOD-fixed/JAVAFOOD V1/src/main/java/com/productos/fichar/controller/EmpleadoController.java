package com.productos.fichar.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.exception.DataAccessException;
import com.productos.fichar.entity.Empleado;
import com.productos.fichar.repository.EmpleadoRepository;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final DataSource ds;
    public EmpleadoController(DataSource ds) { this.ds = ds; }

    @GetMapping
    public List<Empleado> index() {
        try (Connection con = ds.getConnection()) {
            return new EmpleadoRepository(con).findActivos();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @GetMapping("/todos")
    public List<Empleado> todos() {
        try (Connection con = ds.getConnection()) {
            return new EmpleadoRepository(con).findAll();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @GetMapping("/{id}")
    public Empleado show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            return new EmpleadoRepository(con).find(id);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @PostMapping
    public Empleado store(@RequestBody Empleado e) {
        try (Connection con = ds.getConnection()) {
            new EmpleadoRepository(con).insert(e); return e;
        } catch (SQLException ex) { throw new DataAccessException(ex); }
    }

    @PutMapping("/{id}")
    public Empleado update(@PathVariable int id, @RequestBody Empleado e) {
        try (Connection con = ds.getConnection()) {
            e.setId(id); new EmpleadoRepository(con).update(e); return e;
        } catch (SQLException ex) { throw new DataAccessException(ex); }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            EmpleadoRepository repo = new EmpleadoRepository(con);
            Empleado e = repo.find(id);
            if (e != null) { e.setActivo(false); repo.update(e); }
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}
