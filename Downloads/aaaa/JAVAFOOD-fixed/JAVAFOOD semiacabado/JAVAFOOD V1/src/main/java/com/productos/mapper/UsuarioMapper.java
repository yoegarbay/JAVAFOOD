package com.productos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.productos.entity.Usuario;

public class UsuarioMapper implements com.productos.mapper.RowMapper<Usuario> {

    @Override
    public Usuario map(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("apellidos"),
            rs.getString("direccion"),
            rs.getInt("telefono"),
            rs.getString("email"),
            rs.getString("contrasena"),
            rs.getString("tipo")
        );
    }
}
