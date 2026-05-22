package com.productos.fichar.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.productos.fichar.entity.Fichaje;
import com.productos.mapper.RowMapper;

public class FichajeMapper implements RowMapper<Fichaje> {
    @Override
    public Fichaje map(ResultSet rs) throws SQLException {
        Double horas = null;
        double raw = rs.getDouble("horas_calc");
        if (!rs.wasNull()) horas = raw;
        return new Fichaje(rs.getInt("id"), rs.getInt("empleado_id"),
                           rs.getString("tipo"), rs.getString("fecha"),
                           rs.getString("hora"), horas);
    }
}
