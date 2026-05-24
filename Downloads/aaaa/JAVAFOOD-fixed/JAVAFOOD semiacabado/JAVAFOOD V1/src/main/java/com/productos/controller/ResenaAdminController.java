package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.productos.dto.ResenaAdminResponse;
import com.productos.exception.DataAccessException;
import com.productos.repository.ResenaRepository;

@RestController
@RequestMapping("/api/admin/resenas")
public class ResenaAdminController {

    private final DataSource ds;

    public ResenaAdminController(DataSource ds) {
        this.ds = ds;
    }

    /** GET /api/admin/resenas — Listar todas las reseñas */
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

    /** GET /api/admin/resenas/{id} — Ver detalle de una reseña */
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

    /** DELETE /api/admin/resenas/{id} — Eliminar reseña inapropiada */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            new ResenaRepository(con).delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // NOTA: El endpoint PUT (editar) fue eliminado intencionalmente.
    // Los administradores solo pueden VER y BORRAR reseñas, no modificarlas.
}