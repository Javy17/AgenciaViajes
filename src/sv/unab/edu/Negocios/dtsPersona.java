package sv.unab.edu.Negocios;

import sv.unab.edu.Dominio.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dtsPersona {

    private static final Logger LOG = Logger.getLogger("sv.edu.unab.agenciaviajes");

    public Function<Persona,Persona> obtenerDatosPersonales = (Persona pPersona) -> {
        LOG.log(Level.INFO, "[PersonaCN][obtenerDatosPersonales] -> {0}", new Object[]{pPersona.getId()});
        Persona persona = null;
        try (Connection conn = new Conexion().getConexion();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM avr.persona WHERE id = ?")) {
            pstmt.setLong(1, pPersona.getId());
            LOG.log(Level.INFO, "[PersonaCN][obteenerDatosPersonales][Query] -> {0}", pstmt);
            if (pstmt.execute()) {
                persona = new Persona();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while(rs.next()){
                        persona.setId(rs.getLong("id"));
                        persona.setNombre(rs.getString("nombre"));
                        persona.setApellidoPaterno(rs.getString("apellidopaterno"));
                        persona.setApellidoMaterno(rs.getString("apellidomaterno"));
                        persona.setDui(rs.getString("dui"));
                        //persona.setNit(rs.getString("nit"));
                        persona.setTelefono(rs.getString("telefono"));
                        persona.setEmail(rs.getString("email"));
                        persona.setDireccion(rs.getString("direccion"));
                        persona.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                    }
                }
            }
        } catch (SQLException sqlex) {
            LOG.log(Level.SEVERE, "[PersonaCN][obtenerDatosPersonales][SQLException] -> ", sqlex);
        }
        return persona;
    };

//    public void

}
