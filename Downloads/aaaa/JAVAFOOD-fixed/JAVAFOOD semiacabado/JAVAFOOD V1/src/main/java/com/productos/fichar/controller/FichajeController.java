package com.productos.fichar.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.db.DB;
import com.productos.exception.DataAccessException;
import com.productos.fichar.dto.FichajeConNombre;
import com.productos.fichar.entity.Fichaje;
import com.productos.fichar.repository.FichajeRepository;

@RestController
@RequestMapping("/api/fichajes")
public class FichajeController {

    private final DataSource ds;
    public FichajeController(DataSource ds) { this.ds = ds; }

    @GetMapping
    public List<FichajeConNombre> index() {
        try (Connection con = ds.getConnection()) {
            String sql = """
                SELECT f.id, e.nombre, e.iniciales, e.color,
                       f.tipo, f.fecha, f.hora, f.horas_calc
                FROM fichajes f
                JOIN empleados e ON f.empleado_id = e.id
                ORDER BY f.fecha DESC, f.hora DESC
            """;
            return DB.queryMany(con, sql, rs -> new FichajeConNombre(
                rs.getInt("id"), rs.getString("nombre"), rs.getString("iniciales"),
                rs.getString("color"), rs.getString("tipo"), rs.getString("fecha"),
                rs.getString("hora"),
                rs.getObject("horas_calc") != null ? rs.getDouble("horas_calc") : null
            ));
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @GetMapping("/empleado/{id}")
    public List<Fichaje> porEmpleado(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            return new FichajeRepository(con).findByEmpleado(id);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @PostMapping
    public Fichaje store(@RequestBody Fichaje fichaje) {
        try (Connection con = ds.getConnection()) {
            FichajeRepository repo = new FichajeRepository(con);
            if ("salida".equals(fichaje.getTipo())) {
                Fichaje entrada = repo.findLastEntrada(fichaje.getEmpleadoId(), fichaje.getFecha());
                if (entrada != null) {
                    try {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalTime tE = LocalTime.parse(entrada.getHora(), fmt);
                        LocalTime tS = LocalTime.parse(fichaje.getHora(), fmt);
                        long min = ChronoUnit.MINUTES.between(tE, tS);
                        if (min < 0) min += 24 * 60;
                        fichaje.setHorasCalc(Math.round(min / 60.0 * 100) / 100.0);
                    } catch (Exception ignored) {}
                }
            }
            repo.insert(fichaje);
            return fichaje;
        } catch (SQLException e) { throw new DataAccessException(e); }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            new FichajeRepository(con).delete(id);
        } catch (SQLException e) { throw new DataAccessException(e); }
    }
}
