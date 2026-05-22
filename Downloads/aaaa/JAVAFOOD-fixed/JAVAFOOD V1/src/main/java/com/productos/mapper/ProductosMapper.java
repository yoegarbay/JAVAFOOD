package com.productos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.productos.entity.Productos;

public class ProductosMapper implements RowMapper<Productos> {
    @Override
    public Productos map(ResultSet rs) throws SQLException {
        int stock = 0;
        try { stock = rs.getInt("stock"); } catch (SQLException ignored) {}
        return new Productos(
            rs.getInt("id_producto"),
            rs.getString("nombre"),
            rs.getFloat("precio"),
            rs.getInt("id_detalle"),
            stock
        );
    }
}
