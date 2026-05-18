package main.java.usuarios.entity;

public class Cliente extends Usuario {

    private Integer puntos;

    public Cliente(Integer id, String nombre, String apellidos, String direccion,
                   int telefono, String email, String contrasenya, String rol,
                   Integer puntos) {

        super(id, nombre, apellidos, direccion, telefono, email, contrasenya, rol);
        this.puntos = puntos;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nombre=" + getNombre() +
                ", apellidos=" + getApellidos() +
                ", direccion=" + getDireccion() +
                ", telefono=" + getTelefono() +
                ", email=" + getEmail() +
                ", rol=" + getRol() +
                ", puntos=" + puntos +
                '}';
    }
}