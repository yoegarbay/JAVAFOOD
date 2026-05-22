package com.productos.fichar.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.productos.fichar.entity.Empleado;
import com.productos.mapper.RowMapper;

public class EmpleadoMapper implements RowMapper<Empleado> {
    @Override
    public Empleado map(ResultSet rs) throws SQLException {
        return new Empleado(rs.getInt("id"), rs.getString("nombre"), rs.getString("iniciales"),
                            rs.getString("color"), rs.getBoolean("activo"));
    }
}
