package com.productos.fichar.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.productos.fichar.entity.TurnoTipo;
import com.productos.mapper.RowMapper;

public class TurnoTipoMapper implements RowMapper<TurnoTipo> {
    @Override
    public TurnoTipo map(ResultSet rs) throws SQLException {
        return new TurnoTipo(rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"),
                             rs.getString("emoji"), rs.getString("horas"));
    }
}
