package sv.unab.edu.Infraestructura;

import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Dominio.Personas;
import sv.unab.edu.Negocios.PersonaDAL;

import java.sql.SQLException;
import java.util.ArrayList;

public class PersonaBL
{
    public int GuardarPersona(Personas pPersona) throws SQLException
    {
        return new PersonaDAL().GuardarPersona(pPersona);
    }
    public ArrayList<Personas> BuscarP(String pValor) throws SQLException
    {
        return new PersonaDAL().BuscarP(pValor);
    }
    public ArrayList<Personas> Buscar() throws SQLException
    {
        return new PersonaDAL().Buscar();
    }
    public int Eliminar(Long pId) throws SQLException
    {
        return new PersonaDAL().Eliminar(pId);
    }
    public int EditarPersona(Personas pPersona) throws SQLException
    {
        return new PersonaDAL().EditarPersona(pPersona);
    }
    public ArrayList<Cliente> BuscarC() throws SQLException
    {
        return new PersonaDAL().BuscarC();
    }
    public ArrayList<Personas> cargarPersona(Long cod) throws SQLException
    {
        return new PersonaDAL().cargarPersona(cod);
    }
}
