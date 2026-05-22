package com.productos.dto;

public class ResenaEditRequest {

    private int    puntuacion;  // 1–5
    private String comentario;

    public int    getPuntuacion()       { return puntuacion; }
    public void   setPuntuacion(int v)  { this.puntuacion = v; }
    public String getComentario()       { return comentario; }
    public void   setComentario(String v) { this.comentario = v; }
}
