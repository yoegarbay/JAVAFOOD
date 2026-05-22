package com.productos.repository;

import java.sql.*;
import java.util.List;

import com.productos.db.DB;
import com.productos.dto.ProductosDetalle;
import com.productos.dto.ProductosResumen;
import com.productos.entity.Productos;
import com.productos.exception.DataAccessException;
import com.productos.mapper.ProductosMapper;
import com.productos.mapper.RowMapper;

public class ProductosRepository extends BaseRepository<Productos> {

    public ProductosRepository(Connection con) {
        super(con, new ProductosMapper());
    }

    public ProductosRepository(Connection con, RowMapper<Productos> mapper) {
        super(con, mapper);
    }

    @Override public String getTable()          { return "productos"; }
    @Override public String getPrimaryKeyName() { return "id_producto"; }
    @Override public String[] getColumnNames()  {
        return new String[]{"id_producto","nombre","precio","id_detalle","stock"};
    }
    @Override public void setPrimaryKey(Productos p, int id) { p.setId_producto(id); }
    @Override public Object[] getInsertValues(Productos p) {
        return new Object[]{p.getNombre(), p.getPrecio(), p.getId_detalle(), p.getStock()};
    }
    @Override public Object[] getUpdateValues(Productos p) {
        return new Object[]{p.getNombre(), p.getPrecio(), p.getId_detalle(), p.getStock(), p.getId_producto()};
    }

    public List<ProductosResumen> findResumen() {
        String sql = "SELECT id_producto, nombre, precio, IFNULL(stock, 15) AS stock FROM productos ORDER BY nombre";
        return DB.queryMany(con, sql,
            rs -> new ProductosResumen(rs.getInt("id_producto"), rs.getString("nombre"),
                                       rs.getFloat("precio"), rs.getInt("stock")));
    }

    public List<ProductosResumen> findResumenByCategoria(String categoria) {
        String sql = """
            SELECT p.id_producto, p.nombre, p.precio, IFNULL(p.stock, 15) AS stock
            FROM productos p
            JOIN detalle_categoria dc ON p.id_detalle = dc.id_detalle
            JOIN categorias c ON dc.id_categoria = c.id_categoria
            WHERE c.nombre = ?
            ORDER BY p.nombre
            """;
        return DB.queryMany(con, sql,
            rs -> new ProductosResumen(rs.getInt("id_producto"), rs.getString("nombre"),
                                       rs.getFloat("precio"), rs.getInt("stock")),
            categoria);
    }

    public ProductosDetalle findDetalle(int id_producto) {
        String sql = """
            SELECT p.id_producto, p.nombre, p.precio, p.id_detalle, IFNULL(p.stock, 15) AS stock, c.nombre AS categoria
            FROM productos p
            JOIN detalle_categoria dc ON p.id_detalle = dc.id_detalle
            JOIN categorias c ON dc.id_categoria = c.id_categoria
            WHERE p.id_producto = ?
            """;
        return DB.queryOne(con, sql,
            rs -> new ProductosDetalle(rs.getInt("id_producto"), rs.getString("nombre"),
                                       rs.getFloat("precio"), rs.getInt("id_detalle"),
                                       rs.getString("categoria")),
            id_producto);
    }

    /**
     * Decrementa el stock de un producto CON SELECT FOR UPDATE (debe llamarse
     * dentro de una transacción ya abierta).
     * @throws DataAccessException si el stock disponible es insuficiente.
     */
    public void decrementarStock(int id_producto, int cantidad) {
        // 1) Leer stock con SELECT FOR UPDATE (dentro de transacción ya abierta)
        String sqlLock = "SELECT stock FROM productos WHERE id_producto = ? FOR UPDATE";
        int stockActual;
        try (PreparedStatement st = con.prepareStatement(sqlLock)) {
            st.setInt(1, id_producto);
            ResultSet rs = st.executeQuery();
            if (!rs.next())
                throw new DataAccessException("Producto #" + id_producto + " no encontrado");
            stockActual = rs.getInt("stock");
        } catch (SQLException e) {
            throw new DataAccessException("Error comprobando stock", e);
        }

        // 2) Validar — fuera del catch para evitar tipos incompatibles
        if (stockActual < cantidad)
            throw new DataAccessException("Stock insuficiente para el producto #" + id_producto
                    + ": disponible=" + stockActual + ", pedido=" + cantidad);

        // 3) Decrementar
        DB.update(con, "UPDATE productos SET stock = stock - ? WHERE id_producto = ?",
                  cantidad, id_producto);
    }
}
