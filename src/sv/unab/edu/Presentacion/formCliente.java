
package sv.unab.edu.Presentacion;
import com.toedter.calendar.JDateChooser;
import org.apache.commons.lang.StringUtils;
import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Infraestructura.logCliente;
import sv.unab.edu.Negocios.Util.Filtro;
import sv.unab.edu.Presentacion.Util.ButtonColumn;
import sv.unab.edu.Presentacion.Util.DefaultTableModelImpl;
import sv.unab.edu.Presentacion.Util.JButtonCellRenderer;
import sv.unab.edu.Presentacion.Util.Utilidades;

//import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class formCliente extends JInternalFrame
{

    private static final Logger LOG = Logger.getLogger("sv.unab.edu.agenciaviajes");

    private JPanel pnlRoot;
    private JComboBox cboParametro;
    private JPanel pnlCamposBusquedaTXT;
    private JLabel lblParametro;
    private JTextField txtBusqueda;
    private JPanel pnlBusqueda;
    private JPanel pnlCamposFecha;
    private JDateChooser dcInicio;
    private JDateChooser dcFinal;
    private JPanel pnlDatos;
    private JTable tblClientes;
    private JButton btnAñadir;
    private JList lstFiltros;

    private logCliente logCliente;
    private List<Cliente> clientes;
    private List<Filtro> filtros;

    public formCliente()
    {
        LOG.log(INFO, "[formCliente][INIT]");
        this.setContentPane(pnlRoot);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(Utilidades.getSize());
        this.pack();
        //setLocationRelativeTo(null);
        txtBusqueda.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(KeyEvent.VK_ENTER == e.getKeyCode()){
                    String value = txtBusqueda.getText();
                    txtBusqueda.setText(null);
                    if(StringUtils.isNotBlank(value) && filtros.stream().noneMatch(f -> StringUtils.equalsIgnoreCase(f.getNombre(), cboParametro.getSelectedItem().toString().toLowerCase()))){
                        Filtro filtro = new Filtro();
                        filtro.setNombre(cboParametro.getSelectedItem().toString().toLowerCase());
                        filtro.setValor(value);
                        filtro.setTipo('S');
                        filtro.setOperador(!filtros.isEmpty() ? "AND" : null);
                        filtros.add(filtro);
                        actualizarListaFiltros();
                    } else {
                        JOptionPane.showMessageDialog(e.getComponent(), "El filtro ya esta agregado a la lista o esta vacio");
                    }
                }
            }
        });
        cboParametro.addItemListener(evt -> {
            LOG.log(Level.INFO, "[formCliente][cboParametroChangeItem] -> {0}", new Object[]{evt.getItem().toString()});
            if (StringUtils.containsIgnoreCase(evt.getItem().toString(), "Fecha")) {
                pnlCamposFecha.setVisible(true);
                pnlCamposBusquedaTXT.setVisible(false);
            } else if(StringUtils.containsIgnoreCase(evt.getItem().toString(), "Selecc")){
                pnlCamposFecha.setVisible(false);
                pnlCamposBusquedaTXT.setVisible(false);
                lblParametro.setText(null);
            } else {
                pnlCamposFecha.setVisible(false);
                pnlCamposBusquedaTXT.setVisible(true);
                lblParametro.setText(evt.getItem().toString());
            }
        });
        btnAñadir.addActionListener(evt ->
        {
            formClienteGestion frm = new formClienteGestion(null);
            frm.pack();
            frm.setVisible(true);
            clientes = logCliente.obtenerListado(null);
            cargarDatos(clientes);
        });
        logCliente = new logCliente();
        clientes = new ArrayList<>();
        filtros = new ArrayList<>();
        clientes = logCliente.obtenerListado(null);
        cargarDatos(clientes);
        /*btnAñadir.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                formClienteGestion frm = new formClienteGestion();
                frm.pack();
                frm.setVisible(true);
                clientes = logCliente.obtenerListado(null);
                cargarDatos(clientes);
            }
        });*/
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        dcInicio = new JDateChooser();
        dcFinal = new JDateChooser();
        dcInicio.setDateFormatString("EEEE dd 'de' MMMMM 'de' yyyy");
        dcFinal.setDateFormatString("EEEE dd 'de' MMMMM 'de' yyyy");
        dcInicio.setDate(new Date());
        dcFinal.setDate(new Date());
        dcInicio.addPropertyChangeListener(evt -> {
            if (StringUtils.equals("date", evt.getPropertyName()))
            {
                buscarFecha(dcInicio.getDate(), dcFinal.getDate());
            }
        });
        dcFinal.addPropertyChangeListener(evt -> {
            if (StringUtils.equals("date", evt.getPropertyName())) {
                if (dcInicio.getDate() != null && filtros.stream().noneMatch(f -> StringUtils.containsIgnoreCase(f.getNombre(), "fecha"))) {
                    buscarFecha(dcInicio.getDate(), dcFinal.getDate());
                } else {
                    JOptionPane.showMessageDialog((Component) evt.getSource(), "El filtro ya esta agregado a la lista o esta vacio");
                }
            }
        });
        tblClientes = new JTable();
        tblClientes.setFillsViewportHeight(true);
        ((DefaultTableCellRenderer) tblClientes.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.CENTER);
        lstFiltros = new JList<String>();
        lstFiltros.addListSelectionListener(e -> {
            try {
                int itemSelected = lstFiltros.getSelectedIndex();
                if(itemSelected > -1 && filtros != null && !filtros.isEmpty()) {
                    if (StringUtils.containsIgnoreCase(filtros.get(itemSelected).getNombre(), "fecha")) {
                        filtros.removeIf(f -> f.getTipo() == 'D');
                    } else {
                        filtros.remove(itemSelected);
                    }
                    actualizarListaFiltros();
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "[formCliente][createUIComponents][Excepcion] -> ", ex);
            }
        });
    }

    private void buscarFecha(Date inicio, Date fin) {
        if(inicio != null && fin != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String inicioStr = df.format(inicio);
            String finalStr = df.format(fin);
            if (filtros.stream().noneMatch(f -> StringUtils.containsIgnoreCase(f.getNombre(), "fecha"))) {
                Filtro filtro = new Filtro();
                filtro.setNombre("fechaNacimiento");
                filtro.setValor(inicioStr);
                filtro.setTipo('D');
                filtro.setOperador(!filtros.isEmpty() ? "AND" : null);
                filtros.add(filtro);
                filtro = new Filtro();
                filtro.setNombre("fechaNacimiento");
                filtro.setValor(finalStr);
                filtro.setTipo('D');
                filtro.setOperador(!filtros.isEmpty() ? "AND" : null);
                filtros.add(filtro);
                actualizarListaFiltros();
            } else {
                JOptionPane.showMessageDialog(this, "El filtro ya esta agregado a la lista o esta vacio");
            }
        }
    }

    private void cargarDatos(List<Cliente> clienteList) {
        Utilidades.reiniciarJTable(tblClientes);
        DefaultTableModel modelo = new DefaultTableModelImpl(Arrays.asList(4, 5));
        modelo.addColumn("DUI");
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Nacimiento");
        modelo.addColumn("Editar");
        modelo.addColumn("Eliminar");
        clienteList.stream().forEach(cl -> {
            modelo.addRow(new Object[]{
                    cl.getDatosPersonales().getDui(),
                    cl,
                    cl.getDatosPersonales().getTelefono(),
                    cl.getDatosPersonales().getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                    "Editar",
                    "Eliminar"
            });
        });
        tblClientes.setModel(modelo);
        tblClientes.setDefaultRenderer(Object.class, new JButtonCellRenderer());
        Utilidades.setAdjustColumnSize(tblClientes);
        Action editarAccion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable tbl = (JTable) e.getSource();
                if (tbl.getSelectedRow() != -1) {
                    Cliente cliente = (Cliente) tbl.getValueAt(tbl.getSelectedRow(), 1);
                    onEditar(cliente);
                }
            }
        };
        Action eliminarAccion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable tbl = (JTable) e.getSource();
                int fila = Integer.valueOf(e.getActionCommand());
                Cliente cliente = (Cliente) tbl.getValueAt(fila, 1);
                onEliminar(cliente);
            }
        };
        ButtonColumn buttonColumn = new ButtonColumn(tblClientes, editarAccion, 4);
        buttonColumn = new ButtonColumn(tblClientes, eliminarAccion, 5);
    }

    private void actualizarListaFiltros(){
        lstFiltros.removeAll();
        if(!filtros.isEmpty()){
            DefaultListModel<String> modeloLista = new DefaultListModel<>();
            filtros.stream().filter(f -> f.getTipo() != 'D').forEach(f -> {
                StringJoiner strFiltro = new StringJoiner(" ");
                strFiltro.add(f.getNombre().toUpperCase());
                strFiltro.add("->");
                strFiltro.add(f.getValor());
                modeloLista.addElement(strFiltro.toString());
            });
            filtros.stream().filter(f -> f.getTipo() == 'D').map(Filtro::getValor).reduce((f1, f2) -> {
                StringJoiner str = new StringJoiner(" ");
                str.add("Fecha Nacimiento");
                str.add("->");
                str.add(f1);
                str.add("a");
                str.add(f2);
                return str.toString();
            }).ifPresent(f -> modeloLista.addElement(f));
            lstFiltros.setModel(modeloLista);
            clientes = logCliente.obtenerListado(filtros);
            cargarDatos(clientes);
        }
    }

    private void onEditar(Cliente cliente) {
        LOG.log(INFO, "[formCliente][onEditar] -> {0}", new Object[]{cliente});
        formClienteGestion frm = new formClienteGestion(cliente);
        frm.pack();
        frm.setVisible(true);
        clientes = logCliente.obtenerListado(null);
        cargarDatos(clientes);
    }

    private void onEliminar(Cliente cliente) {
        LOG.log(INFO, "[formCliente][onEliminar] -> {0}", new Object[]{cliente});
        logCliente.eliminarCliente(cliente);
        clientes = logCliente.obtenerListado(null);
        cargarDatos(clientes);
    }

    /*private void actionPerformed(ActionEvent evt) {
        formClienteGestion frm = new formClienteGestion();
        frm.pack();
        frm.setVisible(true);
        clientes = logCliente.obtenerListado(null);
        cargarDatos(clientes);
    }*/
}
