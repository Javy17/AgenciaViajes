package sv.unab.edu.Negocios;

import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Dominio.Empleado;
import sv.unab.edu.Dominio.Persona;
import sv.unab.edu.Negocios.Util.Filtro;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.logging.Level.INFO;

public class dtsEmpleado {

    private static final Logger LOG = Logger.getLogger("sv.edu.unab.agenciaviajes");

    public Function<List<Filtro>, List<Empleado>> obtenerListadoEmpleados = (filtros) -> {
        LOG.log(INFO, "[dtsEmpleado][obtenerListadoEmpleados]");
        StringJoiner likeParameter = new StringJoiner("", "%", "%");
        List<Empleado> listado = null;
        StringJoiner sqlBuilder = new StringJoiner(" ");
        sqlBuilder.add("SELECT * FROM avr.empleado");
        if (filtros != null && !filtros.isEmpty()) {
            sqlBuilder.add("WHERE idpersona IN(SELECT id FROM avr.persona WHERE");
            List<Filtro> filtrosWithoutDate = filtros.stream().filter(f -> f.getTipo() != 'D').collect(Collectors.toList());
            filtrosWithoutDate.forEach(f -> {
                if (f.getOperador() != null) {
                    sqlBuilder.add(f.getOperador());
                }
                sqlBuilder.add(f.getNombre());
                if (f.getTipo() == 'S') {
                    sqlBuilder.add("ILIKE");
                    sqlBuilder.add("?");
                } else {
                    sqlBuilder.add("=");
                    sqlBuilder.add("?");
                }
            })
            ;
            filtros.stream().filter(f -> f.getTipo() == 'D').findFirst().ifPresent(f -> {
                if (f.getOperador() != null && filtrosWithoutDate.size() < 1) {
                    sqlBuilder.add(f.getOperador());
                } else if(filtrosWithoutDate.size() > 0){
                    sqlBuilder.add("AND");
                }
                sqlBuilder.add(f.getNombre());
                sqlBuilder.add("BETWEEN");
                sqlBuilder.add("? AND ?");
            });
            sqlBuilder.add(")");
        }
        try (Connection conn = new Conexion().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            if (filtros != null && !filtros.isEmpty()) {
                AtomicInteger idx = new AtomicInteger(1);
                filtros.stream().filter(f -> f.getTipo() != 'D').forEach(f -> {
                    try {
                        if (f.getTipo() == 'S') {
                            likeParameter.add(f.getValor());
                            pstmt.setString(idx.get(),likeParameter.toString());
                        }
                        else
                            pstmt.setLong(idx.get(), Long.valueOf(f.getValor()));
                        idx.incrementAndGet();
                    } catch (SQLException e) {
                        LOG.log(Level.SEVERE, "[dtsEmpleado][obtenerListadoEmpleados][SQLException] -> ", e);
                    } catch (Exception e){
                        LOG.log(Level.SEVERE, "[dtsEmpleado][obtenerListadoEmpleados][Excepcion] -> ", e);
                    }
                });
                filtros.stream().filter(f -> f.getTipo() == 'D').forEach(f -> {
                    try {
                        pstmt.setDate(idx.get(), Date.valueOf(f.getValor()));
                        idx.incrementAndGet();
                    } catch (SQLException e) {
                        LOG.log(Level.SEVERE, "[dtsEmpleado][obtenerListadoEmpleados][SQLException] -> ", e);
                    }
                });
            }
            LOG.log(Level.INFO, "[dtsEmpleado][obtenerListadoEmpleados][Query] -> {0}", pstmt);
            try (ResultSet rs = pstmt.executeQuery())
            {
                listado = new ArrayList<>();
                while (rs.next())
                {
                    Empleado empleado = new Empleado();
                    empleado.setId(rs.getLong("id"));
                    empleado.setSeguro(rs.getString("seguro"));
                    empleado.setAfp(rs.getString("AFP"));
                    empleado.setDatosPersonales(new dtsPersona().obtenerDatosPersonales.apply(new Persona(rs.getLong("idpersona"))));
                    listado.add(empleado);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[dtsEmpleado][obtenerListadoEmpleados][SQLException] -> ", e);
        }
        return listado;
    };

    public Consumer<Empleado> registrar = c -> {
        LOG.log(INFO, "[dtsEmpleado][registrar] -> {0}", new Object[]{c});
        StringJoiner sqlBuilder = new StringJoiner(" ");
        sqlBuilder.add("INSERT INTO avr.persona(nombre,")
                .add("apellidopaterno,")
                .add("apellidomaterno,")
                .add("dui,")
                //.add("nit,")
                .add("telefono,")
                .add("email,")
                .add("direccion,")
                .add("fechanacimiento)");
        sqlBuilder.add("VALUES (?,?,?,?,?,?,?,?)");
        sqlBuilder.add("RETURNING id");
        try (Connection conn = new Conexion().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            pstmt.setString(1, c.getDatosPersonales().getNombre());
            pstmt.setString(2, c.getDatosPersonales().getApellidoPaterno());
            pstmt.setString(3, c.getDatosPersonales().getApellidoMaterno());
            pstmt.setString(4, c.getDatosPersonales().getDui());
            //pstmt.setString(5, c.getDatosPersonales().getNit());
            pstmt.setString(5, c.getDatosPersonales().getTelefono());
            pstmt.setString(6, c.getDatosPersonales().getEmail());
            pstmt.setString(7, c.getDatosPersonales().getDireccion());
            pstmt.setDate(8, Date.valueOf(c.getDatosPersonales().getFechaNacimiento()));
            if (pstmt.execute())
            {
                try (ResultSet rs = pstmt.getResultSet())
                {
                    if (rs.next())
                    {
                        //long idPersona = rs.getLong(1);
                        try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO avr.empleado " +
                                "(idpersona,seguro,afp) VALUES (?,?,?)"))
                        {
                            pstmt1.setLong(1, rs.getLong(1));
                            pstmt1.setString(2, c.getSeguro());
                            pstmt1.setString(3, c.getAfp());
                            pstmt1.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[dtsEmpleado][registrar][SQLException] -> ", e);
        }
    };

    public Consumer<Empleado> actualizar = c -> {
        LOG.log(INFO, "[dtsEmpleado][actualizar] -> {0}", new Object[]{c});
        StringJoiner sqlBuilder = new StringJoiner(" ");
        sqlBuilder.add("UPDATE avr.persona SET")
                .add("nombre=?,")
                .add("apellidopaterno=?,")
                .add("apellidomaterno=?,")
                .add("dui=?,")
                .add("telefono=?,")
                .add("email=?,")
                .add("direccion=?,")
                .add("fechanacimiento=?");
        sqlBuilder.add("WHERE id=?");
        StringJoiner sql2 = new StringJoiner(" ");
                sql2.add("UPDATE avr.empleado SET");
                sql2.add("seguro=?,");
                sql2.add("afp=?");
                sql2.add("where id=?");
        try (Connection conn = new Conexion().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            pstmt.setString(1, c.getDatosPersonales().getNombre());
            pstmt.setString(2, c.getDatosPersonales().getApellidoPaterno());
            pstmt.setString(3, c.getDatosPersonales().getApellidoMaterno());
            pstmt.setString(4, c.getDatosPersonales().getDui());
            pstmt.setString(5, c.getDatosPersonales().getTelefono());
            pstmt.setString(6, c.getDatosPersonales().getEmail());
            pstmt.setString(7, c.getDatosPersonales().getDireccion());
            pstmt.setDate(8, Date.valueOf(c.getDatosPersonales().getFechaNacimiento()));
            pstmt.setLong(9, c.getDatosPersonales().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[dtsEmpleado][actualizar][SQLException] -> ", e);
        }
        try (Connection conex = new Conexion().getConexion();
             PreparedStatement pstmt1 = conex.prepareStatement(sql2.toString())) {
            pstmt1.setString(1, c.getSeguro());
            pstmt1.setString(2, c.getAfp());
            pstmt1.setLong(3, c.getDatosPersonales().getId());
            pstmt1.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[dtsEmpleado][actualizar][SQLException] -> ", e);
        }
    };

    public Consumer<Empleado> eliminar = c -> {
        LOG.log(INFO, "[dtsEmpleado][eliminar] -> {0}", new Object[]{c});
        try (Connection conn = new Conexion().getConexion();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM avr.empleado WHERE id = ?")) {
            pstmt.setLong(1, c.getId());
            if (pstmt.executeUpdate() > 0) {
                try (PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM avr.persona WHERE id = ?")) {
                    pstmt1.setLong(1, c.getDatosPersonales().getId());
                    JOptionPane.showMessageDialog(null, c.getDatosPersonales().getId());
                    pstmt1.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "[dtsEmpleado][eliminar][SQLException] -> ", e);
        }
    };

}