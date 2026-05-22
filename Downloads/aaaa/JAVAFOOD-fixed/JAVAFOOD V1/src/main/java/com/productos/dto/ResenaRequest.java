package com.productos.dto;

public class ResenaRequest {

    private Integer idCliente;
    private Integer idProducto;
    private int     puntuacion;   // 1–5
    private String  comentario;

    public Integer getIdCliente()          { return idCliente; }
    public void    setIdCliente(Integer v) { this.idCliente = v; }

    public Integer getIdProducto()          { return idProducto; }
    public void    setIdProducto(Integer v) { this.idProducto = v; }

    public int  getPuntuacion()       { return puntuacion; }
    public void setPuntuacion(int v)  { this.puntuacion = v; }

    public String getComentario()         { return comentario; }
    public void   setComentario(String v) { this.comentario = v; }
}
