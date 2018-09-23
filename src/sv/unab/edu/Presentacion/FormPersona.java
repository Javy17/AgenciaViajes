package sv.unab.edu.Presentacion;

import com.sun.org.apache.xpath.internal.functions.FuncFalse;
import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Dominio.Personas;
import sv.unab.edu.Infraestructura.PersonaBL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FormPersona {

    public JPanel panelRais;
    private JTextField txtNombre;
    private JTextField txtApellidoP;
    private JTextField txtApellidoM;
    private JFormattedTextField txtDUI;
    private JFormattedTextField txtFecha;
    private JFormattedTextField txtTelefono;
    private JTextField txtEmail;
    private JTextArea txtDireccion;
    private JPanel panelBotones;
    private JButton btnNuevo;
    private JButton btnGuardar;
    private JTextField txtFiltro;
    private JButton btnBuscar;
    private JLabel lblId;
    private JButton btnEliminar;
    private JButton btnEditar;
    private JTable tablaPersona;
    private JTextField txtNIT;

    private List<Personas> personaListado;
    DefaultTableModel modelo = new DefaultTableModel();
    Long cod = null;
    AtomicLong aton = new AtomicLong();

    public void Habilitar()
    {
        txtNombre.enable(true);
        txtApellidoP.enable(true);
        txtApellidoM.enable(true);
        txtDUI.enable(true);
       // txtNIT.enable(true);
        txtFecha.enable(true);
        txtTelefono.enable(true);
        txtEmail.enable(true);
        txtDireccion.enable(true);

        txtNombre.requestFocus();
    }
    public void DesHabilitar()
    {
        lblId.enable(false);
        txtNombre.enable(false);
        txtApellidoP.enable(false);
        txtApellidoM.enable(false);
        txtDUI.enable(false);
        //txtNIT.enable(false);
        txtFecha.enable(false);
        txtTelefono.enable(false);
        txtEmail.enable(false);
        txtDireccion.enable(false);
    }
    public void Limpiar()
    {
        lblId.setText("Id");
        txtNombre.setText("");
        txtApellidoP.setText("");
        txtApellidoM.setText("");
        txtDUI.setText("");
        //txtNIT.enable(false);
        txtFecha.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
    }
    public FormPersona()
    {
        initComponents();
        DesHabilitar();
        try
        {
            actualisarTabla(new PersonaBL().BuscarC() , new PersonaBL().Buscar());

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                /*FormCliente fc = new FormCliente();
                fc.setVisble(true);*/
            }
        });
        tablaPersona.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int y = tablaPersona.getSelectedRow();
                cod = Long.valueOf(tablaPersona.getValueAt(y,0).toString());
                try
                {
                    new PersonaBL().BuscarC().stream().filter(x -> x.getId().equals(cod)).forEach(p->{
                        aton.set(p.getIdPersona());
                        try
                        {
                            new PersonaBL().cargarPersona(p.getIdPersona()).forEach(pe -> {
                            txtNombre.setText(pe.getNombre());
                                txtApellidoP.setText(pe.getApellidoPaterno());
                                txtApellidoM.setText(pe.getApellidoMaterno());
                                txtDUI.setText(pe.getDui());
                                txtTelefono.setText(pe.getTelefono());
                                txtEmail.setText(pe.getEmail());
                                txtDireccion.setText(pe.getDireccion());
                                txtFecha.setText(pe.getFechaNacimiento());
                            });
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    });
                }
                catch (Exception exx)
                {
                    exx.printStackTrace();
                }
            }
        });
    }
    public void initComponents()
    {
        tablaPersona.setFillsViewportHeight(true);
        campo(modelo, tablaPersona);

        if (personaListado == null) {
            personaListado = new ArrayList<>();
        }
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Habilitar();
            }
        });
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!txtNombre.getText().isEmpty() || !txtApellidoP.getText().isEmpty() || !txtApellidoM.getText().isEmpty() || !txtDUI.getText().isEmpty() || !txtFecha.getText().isEmpty() || !txtDireccion.getText().isEmpty()) {
                    Personas per = new Personas();
                    per.setNombre(txtNombre.getText());
                    per.setApellidoPaterno(txtApellidoP.getText());
                    per.setApellidoMaterno(txtApellidoM.getText());
                    per.setDui(txtDUI.getText());
                    per.setTelefono(txtTelefono.getText());
                    per.setEmail(txtEmail.getText());
                    per.setDireccion(txtDireccion.getText());
                    per.setFechaNacimiento(txtFecha.getText());
                    try {
                        int N = new PersonaBL().GuardarPersona(per);
                        if (N > 0) {
                            JOptionPane.showMessageDialog(null, "Persona registrada correctamente", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
                            Limpiar();
                            DesHabilitar();
                            try
                            {
                                actualisarTabla(new PersonaBL().BuscarC() , new PersonaBL().Buscar());

                            }
                            catch (SQLException x)
                            {
                                x.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No se ha podido registrar a la Persona", "Error", JOptionPane.ERROR_MESSAGE);
                            txtNombre.requestFocus();
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Debes llenar todos los datos de la Persona", "Error", JOptionPane.ERROR_MESSAGE);
                    txtNombre.requestFocus();
                }
            }
        });
        //metodo buscar
        /*btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            if(!txtFiltro.getText().isEmpty())
            {
                try
                {
                    //new PersonaBL().BuscarP(txtFiltro.getText());

                    ArrayList<Personas> lista =  new PersonaBL().BuscarP(txtFiltro.getText());
                    //JOptionPane.showMessageDialog(null, lista);
                    for (Personas item : lista)
                    {
                        txtNombre.setText(item.getNombre());
                        txtApellidoP.setText(item.getApellidoPaterno());
                        txtApellidoM.setText(item.getApellidoMaterno());
                        txtDUI.setText(item.getDui());
                        txtTelefono.setText(item.getTelefono());
                        txtEmail.setText(item.getEmail());
                        txtDireccion.setText(item.getDireccion());
                        txtFecha.setText(item.getFechaNacimiento());
                    }
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
                }
                else
                {
                 JOptionPane.showMessageDialog(null, "Debes digitar los datos primero", "Error", JOptionPane.ERROR_MESSAGE);
                    txtFiltro.requestFocus();
                }
            }
        });*/
        //metodo filtrar solo al escribir
        txtFiltro.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                //new PersonaBL().BuscarP(txtFiltro.getText());

                ArrayList<Personas> lista = null;
                try
                {
                    //lista = new PersonaBL().BuscarP(txtFiltro.getText());
                    actualisarTabla(new PersonaBL().BuscarC() , new PersonaBL().BuscarP(txtFiltro.getText()));
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
                //JOptionPane.showMessageDialog(null, lista);
                    /*for (Personas item : lista) {
                        lblId.setText(item.getId().toString());
                        txtNombre.setText(item.getNombre());
                        txtApellidoP.setText(item.getApellidoPaterno());
                        txtApellidoM.setText(item.getApellidoMaterno());
                        txtDUI.setText(item.getDui());
                        txtTelefono.setText(item.getTelefono());
                        txtEmail.setText(item.getEmail());
                        txtDireccion.setText(item.getDireccion());
                        txtFecha.setText(item.getFechaNacimiento());
                    }*/
                    //JOptionPane.showMessageDialog(null, !txtFiltro.getText().toString().isEmpty());
                    if (!txtFiltro.getText().toString().isEmpty()) {

                    } else {
                        Limpiar();
                    }
                /*List<Personas> personasFiltrados = personaListado.stream().filter(x -> {
                    boolean  encontrado = x.getNombre().toUpperCase().contains(txtFiltro.getText()) || x.getApellidoPaterno().toUpperCase().contains(txtFiltro.getText())
                            || x.getApellidoMaterno().toUpperCase().contains(txtFiltro.getText()) || x.getDui().contains(txtFiltro.getText()) || x.getTelefono().contains(txtFiltro.getText())
                    || x.getEmail().toUpperCase().contains(txtFiltro.getText()) || x.getDireccion().toUpperCase().contains(txtFiltro.getText()) || x.getFechaNacimiento().contains(txtFiltro.getText());

                    return encontrado;
                }).collect(Collectors.toList());*/
                //JOptionPane.showMessageDialog(null, personasFiltrados);
                //actualisarTabla(personasFiltrados);
                //actualisarTabla(personas);
            }
        });
        //metodo eliminar persona
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //if(!lblId.getText().equals("Id"))
                //{
                    if (JOptionPane.showConfirmDialog(null, "Se eliminará el registro, ¿desea continuar?", "Eliminar Registro", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        //Integer Id = Integer.parseInt(lblId.getText());
                        try {
                            Integer N = new PersonaBL().Eliminar(cod);
                            if (N > 0) {
                                JOptionPane.showMessageDialog(null, "La persona se a eliminado correctamente", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
                                Limpiar();
                                DesHabilitar();
                                txtFiltro.setText(null);
                                try
                                {
                                    actualisarTabla(new PersonaBL().BuscarC() , new PersonaBL().Buscar());

                                }
                                catch (SQLException x)
                                {
                                    x.printStackTrace();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "No se a podido eliminar la persona", "Error", JOptionPane.ERROR_MESSAGE);
                                txtNombre.requestFocus();
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                /*}
                else
                {
                    JOptionPane.showMessageDialog(null, "Debes buscar primero los datos de la Persona para realizar la accion", "Error", JOptionPane.ERROR_MESSAGE);
                    txtNombre.requestFocus();
                }*/
            }
        });
        //editar
        btnEditar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int y = tablaPersona.getSelectedRow();
                cod = Long.valueOf(tablaPersona.getValueAt(y, 0).toString());
                //if (!lblId.getText().equals("Id"))
                //{
                    if (btnEditar.getText().equals("Editar"))
                    {
                        Habilitar();
                        btnEditar.setText("Actualizar");
                    }
                    else if (btnEditar.getText().equals("Actualizar"))
                    {

                        try {
                            new PersonaBL().Buscar().stream().filter(n -> n.getId().equals(aton.get())).forEach(p ->
                            {
                                    //Personas p = new Personas();
                                    //p.setId(cod);
                                    p.setNombre(txtNombre.getText());
                            p.setApellidoPaterno(txtApellidoP.getText());
                            p.setApellidoMaterno(txtApellidoM.getText());
                            p.setDui(txtDUI.getText());
                            p.setTelefono(txtTelefono.getText());
                            p.setEmail(txtEmail.getText());
                            p.setDireccion(txtDireccion.getText());
                            p.setFechaNacimiento(txtFecha.getText());


                                int N = 0;
                                try
                                {
                                    N = new PersonaBL().EditarPersona(p);
                                }
                                catch (SQLException e1)
                                {
                                    e1.printStackTrace();
                                }
                                if (N > 0) {
                                JOptionPane.showMessageDialog(null, "Los datos de la Persona actualizados correctamente", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
                                Limpiar();
                                DesHabilitar();
                                btnEditar.setText("Editar");
                                txtFiltro.setText(null);
                                try
                                {
                                    actualisarTabla(new PersonaBL().BuscarC() , new PersonaBL().Buscar());

                                }
                                catch (SQLException x)
                                {
                                    x.printStackTrace();
                                }
                            } else
                                {
                                JOptionPane.showMessageDialog(null, "No se ha podido actualizar los datos de la Persona", "Error", JOptionPane.ERROR_MESSAGE);
                                txtNombre.requestFocus();
                                }

                            });
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                    }
                //}
                /*else
                {
                    JOptionPane.showMessageDialog(null, "Debes buscar primero los datos de la Persona para realizar la accion", "Error", JOptionPane.ERROR_MESSAGE);
                    txtNombre.requestFocus();
                }*/
            }
        });

    }
    private void actualisarTabla(List<Cliente> c, List<Personas> listado)
    {
        reiniciarJTable(tablaPersona);

        /*for (int i = 0; i <= personaListado.size() - 1; i++) {
            Long cod = personaListado.get(i).getId();
            String nombre = personaListado.get(i).getNombre();
            String apellidoP = personaListado.get(i).getApellidoPaterno();
            String apellidoM = personaListado.get(i).getApellidoMaterno();
            String dui = personaListado.get(i).getDui();
            String telefono = personaListado.get(i).getTelefono();
            String email = personaListado.get(i).getEmail();
            String direccion = personaListado.get(i).getDireccion();
            String fechaN = personaListado.get(i).getFechaNacimiento();*/
        c.stream().sorted(Comparator.comparing(Cliente::getId)).forEach(r ->
            listado.stream().filter(h -> r.getIdPersona().equals(h.getId())).forEach(p ->

                    modelo.addRow(new Object[] {

                            r.getId(),
                            p.getNombre(),
                            p.getApellidoPaterno(),
                            p.getApellidoMaterno(),
                            p.getDui(),
                            p.getTelefono(),
                            p.getEmail(),
                            p.getDireccion(),
                            p.getFechaNacimiento()

                    })));

            /*modelo.addRow(new Object[]{
                    cod,nombre,apellidoP,apellidoM,dui,telefono,email,direccion,fechaN
            });*/
        //}
        tablaPersona.setModel(modelo);
    }
    private void reiniciarJTable(JTable jTable) {
        DefaultTableModel modelo = (DefaultTableModel) jTable.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }

    }
    public void campo(DefaultTableModel mo, JTable tab) {
        mo.addColumn("Id");
        mo.addColumn("Nombre");
        mo.addColumn("ApellidoPaterno");
        mo.addColumn("ApellidoMaterno");
        mo.addColumn("Dui");
        mo.addColumn("Telefono");
        mo.addColumn("Email");
        mo.addColumn("Direccion");
        mo.addColumn("FechaNacimiento");
        tab.setModel(mo);
    }
}
