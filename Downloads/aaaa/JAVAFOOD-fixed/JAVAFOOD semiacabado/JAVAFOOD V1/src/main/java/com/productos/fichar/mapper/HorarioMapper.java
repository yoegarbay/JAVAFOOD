package com.productos.fichar.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.productos.fichar.entity.Horario;
import com.productos.mapper.RowMapper;

public class HorarioMapper implements RowMapper<Horario> {
    @Override
    public Horario map(ResultSet rs) throws SQLException {
        return new Horario(rs.getInt("id"), rs.getInt("empleado_id"), rs.getInt("anyo"),
                           rs.getInt("mes"), rs.getInt("dia"), rs.getString("turno_cod"));
    }
}
