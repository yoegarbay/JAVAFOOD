package com.productos.repository;

import java.sql.Connection;
import java.util.List;
import com.productos.db.DB;
import com.productos.dto.ProductosDetalle;
import com.productos.dto.ProductosResumen;
import com.productos.entity.Productos;
import com.productos.mapper.ProductosMapper;

public class ProductosRepository extends BaseRepository<Productos> {

    public ProductosRepository(Connection con) {
        super(con, new ProductosMapper());
    }

    @Override public String getTable() { return "productos"; }

    @Override
    public String[] getColumnNames() {
        return new String[] { "id_producto", "nombre", "precio", "id_detalle"};
    }
    
    @Override public void setPrimaryKey(Productos p, int id) { p.setId_producto(id); }

    @Override
    public Object[] getInsertValues(Productos p) {
        return new Object[] { p.getNombre(), p.getPrecio(), p.getId_categoria()};
    }

    @Override
    public Object[] getUpdateValues(Productos p) {
        return new Object[] { p.getNombre(), p.getPrecio(), p.getId_categoria(), p.getId_producto() };
    }

    public List<ProductosResumen> findResumen() {
        String sql = "SELECT id_producto, nombre, id_detalle FROM productos ORDER BY nombre";
        return DB.queryMany(con, sql, rs ->
            new ProductosResumen(rs.getInt("id_producto"), rs.getString("nombre"), rs.getInt("id_detalle"))
        );
    }

    // ESTE MÉTODO ES EL QUE INSERTA EN LA TABLA PEDIDOS
    public void guardarPedido(String nombre, int cantidad, float precio, float total) {
        // Asegúrate de que los nombres coincidan con tu tabla 'pedidos'
        String sql = "INSERT INTO pedidos (producto_nombre, cantidad, precio_unitario, total) VALUES (?, ?, ?, ?)";
        DB.insert(con, sql, nombre, cantidad, precio, total);
    }
}