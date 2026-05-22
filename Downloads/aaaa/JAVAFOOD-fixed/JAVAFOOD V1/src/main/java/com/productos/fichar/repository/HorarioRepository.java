package com.productos.fichar.repository;

import java.sql.Connection;
import java.util.List;
import com.productos.db.DB;
import com.productos.fichar.entity.Horario;
import com.productos.fichar.mapper.HorarioMapper;
import com.productos.repository.BaseRepository;

public class HorarioRepository extends BaseRepository<Horario> {

    public HorarioRepository(Connection con) { super(con, new HorarioMapper()); }

    @Override public String getTable()           { return "horarios"; }
    @Override public String[] getColumnNames()   { return new String[]{"id","empleado_id","anyo","mes","dia","turno_cod"}; }
    @Override public void setPrimaryKey(Horario h, int id) { h.setId(id); }
    @Override public Object[] getInsertValues(Horario h) {
        return new Object[]{h.getEmpleadoId(), h.getAnyo(), h.getMes(), h.getDia(), h.getTurnoCod()};
    }
    @Override public Object[] getUpdateValues(Horario h) {
        return new Object[]{h.getEmpleadoId(), h.getAnyo(), h.getMes(), h.getDia(), h.getTurnoCod(), h.getId()};
    }

    public List<Horario> findByEmpleadoMes(int empleadoId, int anyo, int mes) {
        return DB.queryMany(con,
            "SELECT * FROM horarios WHERE empleado_id=? AND anyo=? AND mes=? ORDER BY dia",
            mapper, empleadoId, anyo, mes);
    }

    public List<Horario> findByMes(int anyo, int mes) {
        return DB.queryMany(con,
            "SELECT * FROM horarios WHERE anyo=? AND mes=? ORDER BY empleado_id, dia",
            mapper, anyo, mes);
    }

    public void upsert(Horario h) {
        DB.update(con,
            "INSERT INTO horarios (empleado_id,anyo,mes,dia,turno_cod) VALUES (?,?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE turno_cod = VALUES(turno_cod)",
            h.getEmpleadoId(), h.getAnyo(), h.getMes(), h.getDia(), h.getTurnoCod());
    }
}
