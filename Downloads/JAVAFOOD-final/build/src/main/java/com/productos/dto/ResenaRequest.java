package com.productos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResenaRequest {

    @JsonProperty("idCliente")
    private Integer idCliente;

    @JsonProperty("idProducto")
    private Integer idProducto;

    @JsonProperty("puntuacion")
    private int puntuacion;

    @JsonProperty("comentario")
    private String comentario;

    public Integer getIdCliente()           { return idCliente; }
    public void    setIdCliente(Integer v)  { this.idCliente = v; }

    public Integer getIdProducto()          { return idProducto; }
    public void    setIdProducto(Integer v) { this.idProducto = v; }

    public int  getPuntuacion()             { return puntuacion; }
    public void setPuntuacion(int v)        { this.puntuacion = v; }

    public String getComentario()           { return comentario; }
    public void   setComentario(String v)   { this.comentario = v; }
}