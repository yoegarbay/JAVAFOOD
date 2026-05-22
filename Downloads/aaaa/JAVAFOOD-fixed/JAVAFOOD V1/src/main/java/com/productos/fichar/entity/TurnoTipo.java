package com.productos.fichar.entity;

public class TurnoTipo {
    private Integer id;
    private String codigo;
    private String nombre;
    private String emoji;
    private String horas;

    public TurnoTipo() {}
    public TurnoTipo(Integer id, String codigo, String nombre, String emoji, String horas) {
        this.id = id; this.codigo = codigo; this.nombre = nombre; this.emoji = emoji; this.horas = horas;
    }

    public Integer getId()              { return id; }
    public void setId(Integer v)        { this.id = v; }
    public String getCodigo()           { return codigo; }
    public void setCodigo(String v)     { this.codigo = v; }
    public String getNombre()           { return nombre; }
    public void setNombre(String v)     { this.nombre = v; }
    public String getEmoji()            { return emoji; }
    public void setEmoji(String v)      { this.emoji = v; }
    public String getHoras()            { return horas; }
    public void setHoras(String v)      { this.horas = v; }
}
