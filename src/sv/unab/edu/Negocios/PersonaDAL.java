package sv.unab.edu.Negocios;

//import com.sun.java.util.jar.pack.Package;

import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Dominio.Personas;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class PersonaDAL
{
    Connection connection;

    {
        try
        {

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/agencia_vuelo", "postgres", "trance4ever");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/agencia_vuelo","postgres", "trance4ever");
    PreparedStatement preparedStatement = null;
    //PreparedStatement MaxId = null;
    PreparedStatement insertCliente = null;


    public int GuardarPersona(Personas pPersona) throws SQLException {
        //Connection connection = null;
        //PreparedStatement preparedStatement = null;

        Integer resultado = 0;
        try {

            Class.forName("org.postgresql.Driver");
            //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/agencia_vuelo", "postgres", "trance4ever");
            preparedStatement = connection.prepareStatement("INSERT INTO avr.persona(nombre,apellidoPaterno,apellidoMaterno,dui,telefono,email,direccion,fechaNacimiento) VALUES(?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, pPersona.getNombre());
            preparedStatement.setString(2, pPersona.getApellidoPaterno());
            preparedStatement.setString(3, pPersona.getApellidoMaterno());
            preparedStatement.setString(4, pPersona.getDui());
           ////// //preparedStatement.setString(5, pPersona.getNit());
            preparedStatement.setString(5, pPersona.getTelefono());
            preparedStatement.setString(6, pPersona.getEmail());
            preparedStatement.setString(7, pPersona.getDireccion());
            preparedStatement.setString(8, pPersona.getFechaNacimiento());

            resultado = preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("select max(Id) as Id from avr.persona");
            Long id = null;
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                id = rs.getLong("Id");
            }
            //JOptionPane.showMessageDialog(null,"Id " +  id);
            insertCliente = connection.prepareStatement("INSERT INTO avr.clientes(idpersona)VALUES(?)");
            insertCliente.setLong(1, id);
            resultado = insertCliente.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error " + e);

        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (insertCliente != null) {
                insertCliente.close();
            }
        }
        return resultado;
    }

    public  ArrayList<Personas> lP = new ArrayList<>();
    public void  BuscarMI() throws SQLException
    {
        preparedStatement = connection.prepareStatement("select max(Id) from avr.persona");
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Personas p = new Personas();
            p.setId(rs.getLong("Id"));
            lP.add(p);
        }
        //return  lP;
    }
    //String pValor
    public ArrayList<Personas> BuscarP(String pValor) throws SQLException {
        // Connection con = null;
        //PreparedStatement ps = null;

        ArrayList<Personas> listaP = new ArrayList<>();
        //preparedStatement = connection.prepareStatement("select * from avr.persona where nombre Ilike '%"+ pValor +"%' or apellidopaterno Ilike '%"+ pValor +"%' or apellidomaterno Ilike '%"+ pValor +"%' or dui Ilike '%"+ pValor +"%' or telefono Ilike '%"+ pValor +"%' or email Ilike '%"+ pValor +"%' or direccion Ilike '%"+ pValor +"%' or fechanacimiento '%"+ pValor +"%'");
        preparedStatement = connection.prepareStatement("select * from avr.persona where nombre Ilike '%" + pValor + "%' or apellidopaterno Ilike '%" + pValor + "%' or apellidomaterno Ilike '%" + pValor + "%' or dui Ilike '%" + pValor + "%' or telefono Ilike '%" + pValor + "%' or email Ilike '%" + pValor + "%' or direccion Ilike '%" + pValor + "%' or fechanacimiento Ilike '%" + pValor + "%' ");

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Personas p = new Personas();
            p.setId(rs.getLong("Id"));
            p.setNombre(rs.getString("nombre"));
            p.setApellidoPaterno(rs.getString("apellidopaterno"));
            p.setApellidoMaterno(rs.getString("apellidomaterno"));
            p.setDui(rs.getString("dui"));
            p.setTelefono(rs.getString("telefono"));
            p.setEmail(rs.getString("email"));
            p.setDireccion(rs.getString("direccion"));
            p.setFechaNacimiento(rs.getString("fechanacimiento"));
            listaP.add(p);
        }
        connection.close();
        preparedStatement.close();

        //JOptionPane.showMessageDialog(null, listaP);
        return listaP;
    }
    public ArrayList<Personas> Buscar() throws SQLException {
        // Connection con = null;
        //PreparedStatement ps = null;

        ArrayList<Personas> listaP = new ArrayList<>();
       preparedStatement = connection.prepareStatement("select * from avr.persona");
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next())
        {
            Personas per = new Personas();
            per.setId(rs.getLong("Id"));
            per.setNombre(rs.getString("nombre"));
            per.setApellidoPaterno(rs.getString("apellidopaterno"));
            per.setApellidoMaterno(rs.getString("apellidomaterno"));
            per.setDui(rs.getString("dui"));
            per.setTelefono(rs.getString("telefono"));
            per.setEmail(rs.getString("email"));
            per.setDireccion(rs.getString("direccion"));
            per.setFechaNacimiento(rs.getString("fechanacimiento"));
            listaP.add(per);
        }
        connection.close();
        preparedStatement.close();

        //JOptionPane.showMessageDialog(null, listaP);
        return listaP;
    }
    public ArrayList<Personas> cargarPersona(Long cod) throws SQLException {
        //Connection Conex = connexion.cone();
        PreparedStatement Ps;
        ArrayList<Personas> ListP = new ArrayList<>();
        Ps =connection.prepareStatement(
                "select * from avr.persona where id=" + cod);
        ResultSet rs = Ps.executeQuery();
        while (rs.next()) {
            Personas per = new Personas();
            per.setNombre(rs.getString("Nombre"));
            per.setApellidoPaterno(rs.getString("ApellidoPaterno"));
            per.setApellidoMaterno(rs.getString("ApellidoMaterno"));
            per.setDui(rs.getString("Dui"));
            per.setTelefono(rs.getString("Telefono"));
            per.setEmail(rs.getString("Email"));
            per.setDireccion(rs.getString("Direccion"));
            per.setFechaNacimiento(rs.getString("FechaNacimiento"));
            ListP.add(per);
        }
        connection.close();
        Ps.close();
        return ListP;
    }
    public ArrayList<Cliente> BuscarC() throws SQLException {
        // Connection con = null;
        //PreparedStatement ps = null;

        ArrayList<Cliente> listaC = new ArrayList<>();
        preparedStatement = connection.prepareStatement("select * from avr.clientes");
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next())
        {
            Cliente c = new Cliente();
            c.setId(rs.getLong("Id"));
            c.setIdPersona(rs.getLong("idpersona"));
            listaC.add(c);
        }
        connection.close();
        preparedStatement.close();

        //JOptionPane.showMessageDialog(null, listaP);
        return listaC;
    }
    //elimnar
    public int Eliminar(Long pId) throws SQLException
    {
        Integer resultado = 0;
        try
        {
            Class.forName("org.postgresql.Driver");
            preparedStatement = connection.prepareStatement("delete FROM avr.clientes where idpersona = ?");
            preparedStatement.setLong(1, pId);
            resultado = preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("delete FROM avr.persona where Id = ?");
            preparedStatement.setLong(1, pId);
            resultado = preparedStatement.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error " + e);

        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        return resultado;
    }
    //EDITAR
    public int EditarPersona(Personas pPersona) throws SQLException
    {
        Integer resultado = 0;
        try
        {
            Class.forName("org.postgresql.Driver");
            preparedStatement = connection.prepareStatement("UPDATE  avr.persona set nombre=?, apellidoPaterno=?, apellidoMaterno=?, dui=?,telefono=?,email=?,direccion=?,fechaNacimiento=? where Id=?");
            preparedStatement.setString(1, pPersona.getNombre());
            preparedStatement.setString(2, pPersona.getApellidoPaterno());
            preparedStatement.setString(3, pPersona.getApellidoMaterno());
            preparedStatement.setString(4, pPersona.getDui());
            //preparedStatement.setString(5, pPersona.getNit());
            preparedStatement.setString(5, pPersona.getTelefono());
            preparedStatement.setString(6, pPersona.getEmail());
            preparedStatement.setString(7, pPersona.getDireccion());
            preparedStatement.setString(8, pPersona.getFechaNacimiento());
            preparedStatement.setLong(9, pPersona.getId());

            resultado = preparedStatement.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error " + e);

        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        return resultado;
    }
}
