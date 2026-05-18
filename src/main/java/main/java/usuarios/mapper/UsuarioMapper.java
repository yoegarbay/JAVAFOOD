package main.java.usuarios.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.usuarios.entity.Usuario;

public class UsuarioMapper implements RowMapper<Usuario> {
    @Override
    public Usuario map(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("apellidos"),
            rs.getString("direccion"),
            rs.getInt("telefono"),
            rs.getString("email"),
            rs.getString("contrasenya"),
            rs.getString("rol")
        );
    }
}
