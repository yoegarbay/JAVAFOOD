package com.productos.fichar.entity;

public class Empleado {
    private Integer id;
    private String  nombre;
    private String  iniciales;
    private String  color;
    private Boolean activo;
    private String  pin;

    public Empleado() {}
    public Empleado(Integer id, String nombre, String iniciales, String color, Boolean activo, String pin) {
        this.id = id; this.nombre = nombre; this.iniciales = iniciales;
        this.color = color; this.activo = activo; this.pin = pin;
    }

    public Integer getId()               { return id; }
    public void    setId(Integer id)     { this.id = id; }
    public String  getNombre()           { return nombre; }
    public void    setNombre(String v)   { this.nombre = v; }
    public String  getIniciales()        { return iniciales; }
    public void    setIniciales(String v){ this.iniciales = v; }
    public String  getColor()            { return color; }
    public void    setColor(String v)    { this.color = v; }
    public Boolean getActivo()           { return activo; }
    public void    setActivo(Boolean v)  { this.activo = v; }
    public String  getPin()              { return pin; }
    public void    setPin(String v)      { this.pin = v; }
}