package main.java.clientes.entity;

public class Cliente {

	private Integer id;
	private String nombre;
	private String apellidos;
	private String direccion;
	private int telefono;
	private String email;
	private String contrasenya;
	private String rol = "CLIENTE";


	public Cliente(Integer id, String nombre, String apellidos, String direccion, int telefono, String email, String contrasenya, String rol) {
		this.setId(id);
		this.setNombre(nombre);
		this.setApellidos(apellidos);
		this.setDireccion(direccion);
		this.setTelefono(telefono);
		this.setEmail(email);
		this.setContrasenya(contrasenya);
		this.setRol(rol);
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public int getTelefono() {
		return telefono;
	}

	public String getEmail() {
		return email;
	}
	
	public String getContrasenya() {
		return contrasenya;
	}
	
	public String getRol() {
		return rol;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}
	
	public void setRol(String rol) {
		if (rol == null || rol.isBlank()) {
		    this.rol = "CLIENTE";
		} else {
		    this.rol = rol;
		}
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", direccion="
				+ direccion + ", telefono=" + telefono + ", email=" + email + ", contraseña=" + contrasenya
				+ ", rol " + rol + "]";
	}

}
