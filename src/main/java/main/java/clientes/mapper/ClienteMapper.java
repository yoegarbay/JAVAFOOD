package main.java.clientes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.clientes.entity.Cliente;

public class ClienteMapper implements RowMapper<Cliente> {
    @Override
    public Cliente map(ResultSet rs) throws SQLException {
        return new Cliente(
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
