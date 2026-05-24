package com.productos.fichar.repository;

import java.sql.Connection;
import java.util.List;
import com.productos.db.DB;
import com.productos.fichar.entity.Empleado;
import com.productos.fichar.mapper.EmpleadoMapper;
import com.productos.repository.BaseRepository;

public class EmpleadoRepository extends BaseRepository<Empleado> {

    public EmpleadoRepository(Connection con) { super(con, new EmpleadoMapper()); }

    @Override public String  getTable()           { return "empleados"; }
    @Override public String  getPrimaryKeyName()  { return "id"; }
    @Override public String[] getColumnNames()    { return new String[]{"id","nombre","iniciales","color","activo","pin"}; }
    @Override public void    setPrimaryKey(Empleado e, int id) { e.setId(id); }

    @Override public Object[] getInsertValues(Empleado e) {
        return new Object[]{
            e.getNombre(), e.getIniciales(), e.getColor(),
            e.getActivo() != null ? e.getActivo() : true,
            e.getPin()
        };
    }

    @Override public Object[] getUpdateValues(Empleado e) {
        return new Object[]{
            e.getNombre(), e.getIniciales(), e.getColor(),
            e.getActivo(), e.getPin(), e.getId()
        };
    }

    public List<Empleado> findActivos() {
        return DB.queryMany(con, "SELECT * FROM empleados WHERE activo = 1 ORDER BY nombre", mapper);
    }

    public List<Empleado> findAll() {
        return DB.queryMany(con, "SELECT * FROM empleados ORDER BY nombre", mapper);
    }

    public Empleado find(int id) {
        return DB.queryOne(con, "SELECT * FROM empleados WHERE id = ?", mapper, id);
    }
}