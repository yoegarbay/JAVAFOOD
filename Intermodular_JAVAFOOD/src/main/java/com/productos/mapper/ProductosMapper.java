package com.productos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.productos.entity.Productos;

public class ProductosMapper implements RowMapper<Productos> {
	@Override
    public Productos map(ResultSet rs) throws SQLException {
        return new Productos(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getInt("precio"),
                rs.getInt("id_categoria")
        );
    }
}
