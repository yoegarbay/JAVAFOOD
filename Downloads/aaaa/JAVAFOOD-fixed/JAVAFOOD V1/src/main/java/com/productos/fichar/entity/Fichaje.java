package com.productos.fichar.entity;

public class Fichaje {
    private Integer id;
    private Integer empleadoId;
    private String tipo;
    private String fecha;
    private String hora;
    private Double horasCalc;

    public Fichaje() {}
    public Fichaje(Integer id, Integer empleadoId, String tipo, String fecha, String hora, Double horasCalc) {
        this.id = id; this.empleadoId = empleadoId; this.tipo = tipo;
        this.fecha = fecha; this.hora = hora; this.horasCalc = horasCalc;
    }

    public Integer getId()                   { return id; }
    public void setId(Integer v)             { this.id = v; }
    public Integer getEmpleadoId()           { return empleadoId; }
    public void setEmpleadoId(Integer v)     { this.empleadoId = v; }
    public String getTipo()                  { return tipo; }
    public void setTipo(String v)            { this.tipo = v; }
    public String getFecha()                 { return fecha; }
    public void setFecha(String v)           { this.fecha = v; }
    public String getHora()                  { return hora; }
    public void setHora(String v)            { this.hora = v; }
    public Double getHorasCalc()             { return horasCalc; }
    public void setHorasCalc(Double v)       { this.horasCalc = v; }
}
