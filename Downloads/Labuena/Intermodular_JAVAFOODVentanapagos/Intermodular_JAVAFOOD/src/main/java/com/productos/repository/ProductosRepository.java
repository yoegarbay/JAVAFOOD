package com.productos.repository;

import java.sql.Connection;
import java.util.List;

import com.productos.db.DB;
import com.productos.dto.ProductosDetalle;
import com.productos.dto.ProductosResumen;
import com.productos.entity.Productos;
import com.productos.mapper.ProductosMapper;
import com.productos.mapper.RowMapper;

public class ProductosRepository extends BaseRepository<Productos> {

	public ProductosRepository(Connection con) {
		super(con, new ProductosMapper());
	}

	public ProductosRepository(Connection con, RowMapper<Productos> mapper) {
		super(con, mapper);
	}

	@Override
	public String getTable() {
		return "productos";
	}

	@Override
	public String getPrimaryKeyName() {
		return "id_producto";
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id_producto", "nombre", "precio", "id_detalle" };
	}

	@Override
	public void setPrimaryKey(Productos p, int id_producto) {
		p.setId_producto(id_producto);
	}

	@Override
	public Object[] getInsertValues(Productos p) {
		return new Object[] { p.getNombre(), p.getPrecio(), p.getId_detalle() };
	}

	@Override
	public Object[] getUpdateValues(Productos p) {
		return new Object[] { p.getNombre(), p.getPrecio(), p.getId_detalle(), p.getId_producto() };
	}

	public List<ProductosResumen> findResumen() {
		String sql = """
			SELECT id_producto, nombre, precio
			FROM productos
			ORDER BY nombre
		""";
		return DB.queryMany(con, sql, rs ->
			new ProductosResumen(
				rs.getInt("id_producto"),
				rs.getString("nombre"),
				rs.getFloat("precio")
			)
		);
	}

	/**
	 * Filtra productos por nombre de categoría.
	 * La tabla productos no tiene columna id_categoria directa; los productos
	 * se vinculan a categorias a través de detalle_categoria.
	 * Para Bebidas (id_categoria=2) hay detalles (1,2,3).
	 * Para Complementos y Salsas no hay detalle_categoria, así que buscamos
	 * también por nombre directo en categorias usando id_categoria del producto
	 * si existe, o devolvemos los hardcodeados como fallback en el HTML.
	 *
	 * Estrategia real: los productos de Bebidas tienen id_detalle IN (1,2,3)
	 * que apuntan a id_categoria=2 (Bebidas).
	 * Los de Complementos y Salsas no tienen detalle -> usamos LEFT JOIN
	 * y filtramos: si hay detalle usamos c.nombre, si no hay (NULL) usamos
	 * la categoría hardcodeada por id_detalle IS NULL + parámetro nombre.
	 *
	 * Solución limpia: hacer LEFT JOIN y devolver todos los que SÍ tienen
	 * la categoría pedida, más los que tienen id_detalle NULL cuando se pide
	 * Complementos o Salsas (que en la BD actual no tienen detalle asignado).
	 *
	 * En la práctica la BD del alumno tiene TODOS los productos en Bebidas
	 * (id_detalle 1-3 → id_categoria=2). Para que Complementos y Salsas
	 * funcionen necesitamos añadir sus filas en detalle_categoria e id_detalle
	 * en productos. Mientras tanto devolvemos lista vacía sólo para esas dos
	 * categorías y el HTML usa el fallback JS.
	 *
	 * → Añadimos los detalle_categoria y actualizamos productos en el SQL.
	 */
	public List<ProductosResumen> findResumenByCategoria(String categoria) {
		String sql = """
			SELECT p.id_producto, p.nombre, p.precio
			FROM productos p
			JOIN detalle_categoria dc ON p.id_detalle = dc.id_detalle
			JOIN categorias c ON dc.id_categoria = c.id_categoria
			WHERE c.nombre = ?
			ORDER BY p.nombre
		""";
		return DB.queryMany(con, sql, rs ->
			new ProductosResumen(
				rs.getInt("id_producto"),
				rs.getString("nombre"),
				rs.getFloat("precio")
			),
			categoria
		);
	}

	public ProductosDetalle findDetalle(int id_producto) {
		String sql = """
			SELECT p.id_producto,
			       p.nombre,
			       p.precio,
			       p.id_detalle,
			       c.nombre AS categoria
			FROM productos p
			JOIN detalle_categoria dc ON p.id_detalle = dc.id_detalle
			JOIN categorias c ON dc.id_categoria = c.id_categoria
			WHERE p.id_producto = ?
		""";
		return DB.queryOne(con, sql, rs ->
			new ProductosDetalle(
				rs.getInt("id_producto"),
				rs.getString("nombre"),
				rs.getFloat("precio"),
				rs.getInt("id_detalle"),
				rs.getString("categoria")
			),
			id_producto
		);
	}
}
