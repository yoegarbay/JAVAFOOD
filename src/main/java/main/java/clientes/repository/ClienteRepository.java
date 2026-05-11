package main.java.clientes.repository;

import java.sql.Connection;
import java.util.List;

import main.java.clientes.db.DB;
import main.java.clientes.dto.ClienteResumen;
import main.java.clientes.entity.Cliente;
import main.java.clientes.mapper.ClienteMapper;

public class ClienteRepository extends BaseRepository<Cliente> {

    public ClienteRepository(Connection con) {
        super(con, new ClienteMapper());
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
    public void setPrimaryKey(Cliente c, int id) {
        c.setId(id);
    }

    //Insert
    @Override
    public Object[] getInsertValues(Cliente c) {
    	
    	String rol = c.getRol();

        if (rol == null || rol.isBlank()) {
            rol = "CLIENTE";
        }
    	
        return new Object[] {
            c.getNombre(),
            c.getApellidos(),
            c.getDireccion(),
            c.getTelefono(),
            c.getEmail(),
            c.getContrasenya(),
            c.getRol()
        };
    }

    // update
    @Override
    public Object[] getUpdateValues(Cliente c) {
        return new Object[] {
            c.getNombre(),
            c.getApellidos(),
            c.getDireccion(),
            c.getTelefono(),
            c.getEmail(),
            c.getContrasenya(),
            c.getRol(),
            c.getId()
        };
    }

    // lista admin
    public List<ClienteResumen> findAllResumen() {

        String sql = """
            SELECT id, nombre, apellidos, direccion, telefono, email, rol
            FROM clientes
            ORDER BY rol DESC, nombre, apellidos
        """;

        return DB.queryMany(con, sql, rs ->
            new ClienteResumen(
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
    public Cliente findByEmailAndPassword(String email, String pass) {

        String sql = "SELECT * FROM clientes WHERE email = ? AND contrasenya = ?";

        return DB.queryOne(con, sql, new ClienteMapper(), email, pass);
    }
}
