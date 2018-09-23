package sv.unab.edu.Presentacion;

import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Dominio.Personas;
import sv.unab.edu.Infraestructura.PersonaBL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class FormCliente {
    private JTable tablaCliente;
    DefaultTableModel modelo = new DefaultTableModel();

    public FormCliente() {
        initComponents();
    }
    public void initComponents()
    {
        tablaCliente.setFillsViewportHeight(true);
        /*try
        {
            actualisarTabla(new PersonaBL().Buscar());

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }*/

    }
    private void actualisarTabla(List<Cliente> c)
    {
        c.stream().sorted(Comparator.comparing(Cliente::getId)).forEach(cli ->
                modelo.addRow(new Object[] {
                        cli.getId(),
                        cli.getIdPersona()
                }));
    }

}
