package com.productos.fichar.repository;

import java.sql.Connection;
import com.productos.fichar.entity.TurnoTipo;
import com.productos.fichar.mapper.TurnoTipoMapper;
import com.productos.repository.BaseRepository;

public class TurnoTipoRepository extends BaseRepository<TurnoTipo> {

    public TurnoTipoRepository(Connection con) { super(con, new TurnoTipoMapper()); }

    @Override public String getTable()           { return "turnos_tipo"; }
    @Override public String[] getColumnNames()   { return new String[]{"id","codigo","nombre","emoji","horas"}; }
    @Override public void setPrimaryKey(TurnoTipo t, int id) { t.setId(id); }
    @Override public Object[] getInsertValues(TurnoTipo t) {
        return new Object[]{t.getCodigo(), t.getNombre(), t.getEmoji(), t.getHoras()};
    }
    @Override public Object[] getUpdateValues(TurnoTipo t) {
        return new Object[]{t.getCodigo(), t.getNombre(), t.getEmoji(), t.getHoras(), t.getId()};
    }
}
