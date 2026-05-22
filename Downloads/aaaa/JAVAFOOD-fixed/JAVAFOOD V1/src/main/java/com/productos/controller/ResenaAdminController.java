package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.productos.dto.ResenaAdminResponse;
import com.productos.dto.ResenaEditRequest;
import com.productos.dto.ResenaRequest;
import com.productos.exception.DataAccessException;
import com.productos.repository.ResenaRepository;

@RestController
@RequestMapping("/api/admin/resenas")
public class ResenaAdminController {

    private final DataSource ds;

    public ResenaAdminController(DataSource ds) {
        this.ds = ds;
    }

    // GET /api/admin/resenas  → lista todas las reseñas
    @GetMapping
    public List<ResenaAdminResponse> index() {
        try (Connection con = ds.getConnection()) {
            return new ResenaRepository(con).findAll();
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // GET /api/admin/resenas/{id}  → detalle de una reseña
    @GetMapping("/{id}")
    public ResenaAdminResponse show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            return new ResenaRepository(con).findById(id);
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // PUT /api/admin/resenas/{id}  → editar puntuación y comentario
    @PutMapping("/{id}")
    public ResenaAdminResponse update(@PathVariable int id,
                                      @RequestBody ResenaEditRequest req) {
        try (Connection con = ds.getConnection()) {
            ResenaRepository repo = new ResenaRepository(con);
            repo.update(id, req.getPuntuacion(), req.getComentario());
            return repo.findById(id);
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // DELETE /api/admin/resenas/{id}  → eliminar reseña
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            new ResenaRepository(con).delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Reseña #" + id + " eliminada"));
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // POST /api/admin/resenas  → crear reseña sin validación de pedido
    @PostMapping
    public ResenaAdminResponse create(@RequestBody ResenaRequest req) {
        if (req.getIdCliente() == null || req.getIdProducto() == null)
            throw new DataAccessException("idCliente e idProducto son obligatorios");
        try (Connection con = ds.getConnection()) {
            return new ResenaRepository(con).adminGuardar(
                req.getIdCliente(), req.getIdProducto(),
                req.getPuntuacion(), req.getComentario()
            );
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
