package com.productos.fichar.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.exception.DataAccessException;
import com.productos.fichar.entity.Horario;
import com.productos.fichar.entity.TurnoTipo;
import com.productos.fichar.repository.HorarioRepository;
import com.productos.fichar.repository.TurnoTipoRepository;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    private final DataSource ds;
    public HorarioController(DataSource ds) { this.ds = ds; }

    @GetMapping("/turnos")
    public List<TurnoTipo> turnos() {
        try (Connection con = ds.getConnection()) {
            return new TurnoTipoRepository(con).findAll();
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @GetMapping("/empleado/{id}")
    public List<Horario> porEmpleado(@PathVariable int id, @RequestParam int anyo, @RequestParam int mes) {
        try (Connection con = ds.getConnection()) {
            return new HorarioRepository(con).findByEmpleadoMes(id, anyo, mes);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @GetMapping
    public List<Horario> porMes(@RequestParam int anyo, @RequestParam int mes) {
        try (Connection con = ds.getConnection()) {
            return new HorarioRepository(con).findByMes(anyo, mes);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @PostMapping
    public Horario upsert(@RequestBody Horario horario) {
        try (Connection con = ds.getConnection()) {
            new HorarioRepository(con).upsert(horario); return horario;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            new HorarioRepository(con).delete(id);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}
