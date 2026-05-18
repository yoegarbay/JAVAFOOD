package main.java.usuarios.repository;

import java.sql.Connection;
import java.util.List;

import main.java.usuarios.db.DB;
import main.java.usuarios.dto.UsuarioResumen;
import main.java.usuarios.entity.Usuario;
import main.java.usuarios.mapper.UsuarioMapper;

public class UsuarioRepository extends BaseRepository<Usuario> {

    public UsuarioRepository(Connection con) {
        super(con, new UsuarioMapper());
    }

    @Override
    public String getTable() {
        return "clientes";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {
            "id",
            "nombre",
            "apellidos",
            "direccion",
            "telefono",
            "email",
            "contrasenya",
            "rol"
        };
    }

    @Override
    public void setPrimaryKey(Usuario u, int id) {
        u.setId(id);
    }

    //Insert
    @Override
    public Object[] getInsertValues(Usuario u) {
    	
    	String rol = u.getRol();

        if (rol == null || rol.isBlank()) {
            rol = "CLIENTE";
        }
    	
        return new Object[] {
            u.getNombre(),
            u.getApellidos(),
            u.getDireccion(),
            u.getTelefono(),
            u.getEmail(),
            u.getContrasenya(),
            u.getRol()
        };
    }

    // update
    @Override
    public Object[] getUpdateValues(Usuario u) {
        return new Object[] {
            u.getNombre(),
            u.getApellidos(),
            u.getDireccion(),
            u.getTelefono(),
            u.getEmail(),
            u.getContrasenya(),
            u.getRol(),
            u.getId()
        };
    }

    // lista admin
    public List<UsuarioResumen> findAllResumen() {

        String sql = """
            SELECT id, nombre, apellidos, direccion, telefono, email, rol
            FROM clientes
            ORDER BY rol DESC, nombre, apellidos
        """;

        return DB.queryMany(con, sql, rs ->
            new UsuarioResumen(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("direccion"),
                rs.getInt("telefono"),
                rs.getString("email"),
                rs.getString("rol")
            )
        );
    }
    
    //LOGINS
    public Usuario findByEmailAndPassword(String email, String pass) {

        String sql = "SELECT * FROM clientes WHERE email = ? AND contrasenya = ?";

        return DB.queryOne(con, sql, new UsuarioMapper(), email, pass);
    }
}
