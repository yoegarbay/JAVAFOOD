package com.productos.repository;

import java.sql.Connection;
import java.util.List;

import com.productos.db.DB;
import com.productos.dto.UsuarioResumen;
import com.productos.entity.Usuario;
import com.productos.mapper.UsuarioMapper;

public class UsuarioRepository extends BaseRepository<Usuario> {

    public UsuarioRepository(Connection con) {
        super(con, new UsuarioMapper());
    }

    @Override
    public String getTable() { return "usuario"; }

    @Override
    public String[] getColumnNames() {
        return new String[] {
            "id", "nom", "apellidos", "direccion",
            "telefono", "email", "contrasena", "tipo"
        };
    }

    @Override
    public void setPrimaryKey(Usuario u, int id) { u.setId(id); }

    @Override
    public Object[] getInsertValues(Usuario u) {
        return new Object[] {
            u.getNom(), u.getApellidos(), u.getDireccion(),
            u.getTelefono(), u.getEmail(), u.getContrasena(), u.getTipo()
        };
    }

    @Override
    public Object[] getUpdateValues(Usuario u) {
        return new Object[] {
            u.getNom(), u.getApellidos(), u.getDireccion(),
            u.getTelefono(), u.getEmail(), u.getContrasena(), u.getTipo(),
            u.getId()
        };
    }

    // ─── Insertar fila en cliente cuando el usuario es de tipo CLIENTE ────────
    public void insertCliente(int idUsuario) {
        String sql = "INSERT INTO cliente (id, puntos) VALUES (?, 0)";
        DB.update(con, sql, idUsuario);
    }

    // ─── Resumen para webAdmin ────────────────────────────────────────────────
    public List<UsuarioResumen> findAllResumen() {
        String sql = """
            SELECT
                u.id,
                u.nom,
                u.apellidos,
                u.direccion,
                u.telefono,
                u.email,
                u.tipo,
                c.puntos
            FROM usuario u
            LEFT JOIN cliente c ON c.id = u.id
            ORDER BY u.tipo DESC, u.nom, u.apellidos
        """;
        return DB.queryMany(con, sql, rs ->
            new UsuarioResumen(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("apellidos"),
                rs.getString("direccion"),
                rs.getInt("telefono"),
                rs.getString("email"),
                rs.getString("tipo"),
                rs.getObject("puntos") != null ? rs.getInt("puntos") : 0
            )
        );
    }

    // ─── Login por email + contraseña ─────────────────────────────────────────
    public Usuario findByEmailAndPassword(String email, String contrasena) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND contrasena = ?";
        return DB.queryOne(con, sql, new UsuarioMapper(), email, contrasena);
    }
}
