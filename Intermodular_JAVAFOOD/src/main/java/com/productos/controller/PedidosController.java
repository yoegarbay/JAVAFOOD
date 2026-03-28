package com.productos.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.productos.repository.ProductosRepository;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidosController {
    
    private final DataSource ds;

    public PedidosController(DataSource ds) {
        this.ds = ds;
    }

    @PostMapping(value = "/pagar", consumes = "application/json", produces = "application/json")
    public String procesarPedido(@RequestBody List<PedidoItem> items) {
        try (Connection con = ds.getConnection()) {
            ProductosRepository repo = new ProductosRepository(con);
            for (PedidoItem item : items) {
                repo.guardarPedido(
                    item.getNombre(), 
                    item.getCantidad(), 
                    item.getPrecio(), 
                    item.getTotal()
                );
            }
            return "{\"status\": \"OK\"}";
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"status\": \"ERROR\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }

    // CLASE INTERNA CORREGIDA
    public static class PedidoItem {
        private String nombre;
        private int cantidad;
        private float precio;
        private float total;

        public PedidoItem() {} // Constructor vacío necesario

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public float getPrecio() { return precio; }
        public void setPrecio(float precio) { this.precio = precio; }

        public float getTotal() { return total; }
        public void setTotal(float total) { this.total = total; }
    }
}