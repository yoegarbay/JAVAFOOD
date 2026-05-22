package com.productos.fichar.entity;

public class Horario {
    private Integer id;
    private Integer empleadoId;
    private Integer anyo;
    private Integer mes;
    private Integer dia;
    private String turnoCod;

    public Horario() {}
    public Horario(Integer id, Integer empleadoId, Integer anyo, Integer mes, Integer dia, String turnoCod) {
        this.id = id; this.empleadoId = empleadoId; this.anyo = anyo;
        this.mes = mes; this.dia = dia; this.turnoCod = turnoCod;
    }

    public Integer getId()                   { return id; }
    public void setId(Integer v)             { this.id = v; }
    public Integer getEmpleadoId()           { return empleadoId; }
    public void setEmpleadoId(Integer v)     { this.empleadoId = v; }
    public Integer getAnyo()                 { return anyo; }
    public void setAnyo(Integer v)           { this.anyo = v; }
    public Integer getMes()                  { return mes; }
    public void setMes(Integer v)            { this.mes = v; }
    public Integer getDia()                  { return dia; }
    public void setDia(Integer v)            { this.dia = v; }
    public String getTurnoCod()              { return turnoCod; }
    public void setTurnoCod(String v)        { this.turnoCod = v; }
}
