package com.productos.fichar.repository;

import java.sql.Connection;
import java.util.List;
import com.productos.db.DB;
import com.productos.fichar.entity.Fichaje;
import com.productos.fichar.mapper.FichajeMapper;
import com.productos.repository.BaseRepository;

public class FichajeRepository extends BaseRepository<Fichaje> {

    public FichajeRepository(Connection con) { super(con, new FichajeMapper()); }

    @Override public String getTable()           { return "fichajes"; }
    @Override public String[] getColumnNames()   { return new String[]{"id","empleado_id","tipo","fecha","hora","horas_calc"}; }
    @Override public void setPrimaryKey(Fichaje f, int id) { f.setId(id); }
    @Override public Object[] getInsertValues(Fichaje f) {
        return new Object[]{f.getEmpleadoId(), f.getTipo(), f.getFecha(), f.getHora(), f.getHorasCalc()};
    }
    @Override public Object[] getUpdateValues(Fichaje f) {
        return new Object[]{f.getEmpleadoId(), f.getTipo(), f.getFecha(), f.getHora(), f.getHorasCalc(), f.getId()};
    }

    public List<Fichaje> findByEmpleado(int empleadoId) {
        return DB.queryMany(con, "SELECT * FROM fichajes WHERE empleado_id = ? ORDER BY fecha DESC, hora DESC", mapper, empleadoId);
    }

    public Fichaje findLastEntrada(int empleadoId, String fecha) {
        return DB.queryOne(con,
            "SELECT * FROM fichajes WHERE empleado_id=? AND fecha=? AND tipo='entrada' ORDER BY hora DESC LIMIT 1",
            mapper, empleadoId, fecha);
    }
}
