package sv.unab.edu.Presentacion;

//import com.toedter.calendar.JDateChooser;

import com.toedter.calendar.JDateChooser;

import org.apache.commons.lang.StringUtils;
import sv.unab.edu.Dominio.Empleado;
import sv.unab.edu.Infraestructura.logEmpleado;
import sv.unab.edu.Negocios.Util.Filtro;
import sv.unab.edu.Presentacion.Util.ButtonColumn;
import sv.unab.edu.Presentacion.Util.DefaultTableModelImpl;
import sv.unab.edu.Presentacion.Util.JButtonCellRenderer;
import sv.unab.edu.Presentacion.Util.Utilidades;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;

import static java.util.logging.Level.INFO;

public class formEmpleado extends JInternalFrame
{

    private static final Logger LOG = Logger.getLogger("sv.unab.edu.agenciaviajes");

    public JPanel pnlRoot;
    private JComboBox cboParametro;
    private JPanel pnlCamposBusquedaTXT;
    private JLabel lblParametro;
    private JTextField txtBusqueda;
    private JPanel pnlBusqueda;
    private JPanel pnlCamposFecha;
    private JDateChooser dcInicio;
    private JDateChooser dcFinal;
    private JPanel pnlDatos;
    private JTable tblEmpleados;
    private JButton btnAñadir;
    private JList lstFiltros;

    private logEmpleado logEmpleado;
    private List<Empleado> empleados;
    private List<Filtro> filtros;

    public formEmpleado()
    {
        LOG.log(INFO, "[formEmpleado][INIT]");
        this.setContentPane(pnlRoot);
        this.setDefaultCloseOperation(JInternalFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(Utilidades.getSize());
        this.pack();
        //this.setLocationRelativeTo(null);


        cboParametro.addItemListener(evt ->
        {
            LOG.log(Level.INFO, "[formEmpleado][cboParametroChangeItem] -> {0}", new Object[]{evt.getItem().toString()});
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
            formGestionEmpleado frm = new formGestionEmpleado(null);
            frm.pack();
            frm.setVisible(true);
            empleados = logEmpleado.obtenerListado(null);
            cargarDatos(empleados);
        });
        logEmpleado = new logEmpleado();
        empleados = new ArrayList<>();
        filtros = new ArrayList<>();
        empleados = logEmpleado.obtenerListado(null);
        cargarDatos(empleados);

        txtBusqueda.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(KeyEvent.VK_ENTER == e.getKeyCode())
                {
                    String value = txtBusqueda.getText();
                    txtBusqueda.setText(null);
                    if(StringUtils.isNotBlank(value) && filtros.stream().noneMatch(f -> StringUtils.equalsIgnoreCase(f.getNombre(), cboParametro.getSelectedItem().toString().toLowerCase())))
                    {
                        Filtro filtro = new Filtro();
                        filtro.setNombre(cboParametro.getSelectedItem().toString().toLowerCase());
                        filtro.setValor(value);
                        filtro.setTipo('S');
                        filtro.setOperador(!filtros.isEmpty() ? "AND" : null);
                        filtros.add(filtro);
                        actualizarListaFiltros();
                    }
                    else
                        {
                        JOptionPane.showMessageDialog(e.getComponent(), "El filtro ya esta agregado a la lista o esta vacio");
                    }
                }
            }
        });
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
            if (StringUtils.equals("date", evt.getPropertyName())) {
                buscarFecha(dcInicio.getDate(), dcFinal.getDate());
            }
        });
        dcFinal.addPropertyChangeListener(evt -> {
            if (org.apache.commons.lang.StringUtils.equals("date", evt.getPropertyName())) {
                if (dcInicio.getDate() != null && filtros.stream().noneMatch(f -> StringUtils.containsIgnoreCase(f.getNombre(), "fecha"))) {
                    buscarFecha(dcInicio.getDate(), dcFinal.getDate());
                } else {
                    JOptionPane.showMessageDialog((Component) evt.getSource(), "El filtro ya esta agregado a la lista o esta vacio");
                }
            }
        });
        tblEmpleados = new JTable();
        tblEmpleados.setFillsViewportHeight(true);
        ((DefaultTableCellRenderer) tblEmpleados.getDefaultRenderer(Object.class)).setHorizontalAlignment(JLabel.CENTER);
        lstFiltros = new JList<String>();
        lstFiltros.addListSelectionListener(e ->
        {
            try
            {
                int itemSelected = lstFiltros.getSelectedIndex();
                if(itemSelected > -1 && filtros != null && !filtros.isEmpty())
                {
                    if (StringUtils.containsIgnoreCase(filtros.get(itemSelected).getNombre(), "fecha"))
                    {
                        filtros.removeIf(f -> f.getTipo() == 'D');
                    }
                    else
                        {
                        filtros.remove(itemSelected);
                    }
                    actualizarListaFiltros();
                }
            }
            catch (Exception ex)
            {
                LOG.log(Level.SEVERE, "[formEmpleado][createUIComponents][Excepcion] -> ", ex);
            }
        });
    }

    private void buscarFecha(Date inicio, Date fin)
    {
        if(inicio != null && fin != null)
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String inicioStr = df.format(inicio);
            String finalStr = df.format(fin);
            if (filtros.stream().noneMatch(f -> StringUtils.containsIgnoreCase(f.getNombre(), "fecha")))
            {
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
            }
            else
                {
                JOptionPane.showMessageDialog(this, "El filtro ya esta agregado a la lista o esta vacio");
            }
        }
    }

    private void cargarDatos(List<Empleado> empleadoList)
    {
        Utilidades.reiniciarJTable(tblEmpleados);
        DefaultTableModel modelo = new DefaultTableModelImpl(Arrays.asList(6, 7));
        modelo.addColumn("DUI");
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Nacimiento");
        modelo.addColumn("Seguro");
        modelo.addColumn("AFP");
        modelo.addColumn("Editar");
        modelo.addColumn("Eliminar");
        empleadoList.stream().forEach(cl ->
        {
            modelo.addRow(new Object[]
                    {
                    cl.getDatosPersonales().getDui(),
                    cl,
                    cl.getDatosPersonales().getTelefono(),
                    cl.getDatosPersonales().getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                    cl.getSeguro(),
                    cl.getAfp(),
                    "Editar",
                    "Eliminar"
            });
        });
        tblEmpleados.setModel(modelo);
        tblEmpleados.setDefaultRenderer(Object.class, new JButtonCellRenderer());
        Utilidades.setAdjustColumnSize(tblEmpleados);
        //Edicion
        Action editarAccion = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JTable tbl = (JTable) e.getSource();
                if (tbl.getSelectedRow() != -1)
                {
                    Empleado empleado = (Empleado) tbl.getValueAt(tbl.getSelectedRow(), 1);

                    onEditar(empleado);
                }
            }
        };
        Action eliminarAccion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable tbl = (JTable) e.getSource();
                int fila = Integer.valueOf(e.getActionCommand());
                Empleado empleado = (Empleado) tbl.getValueAt(fila, 1);
                onEliminar(empleado);
            }
        };
        ButtonColumn buttonColumn = new ButtonColumn(tblEmpleados, editarAccion, 6);
        buttonColumn = new ButtonColumn(tblEmpleados, eliminarAccion, 7);
    }

    //metodo actualizar
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
            empleados = logEmpleado.obtenerListado(filtros);
            cargarDatos(empleados);
        }
    }

    private void onEditar(Empleado empleado) {
        LOG.log(INFO, "[formEmpleado][onEditar] -> {0}", new Object[]{empleado});
        formGestionEmpleado frm = new formGestionEmpleado(empleado);
        frm.pack();
        frm.setVisible(true);
        empleados = logEmpleado.obtenerListado(null);
        cargarDatos(empleados);
    }

    private void onEliminar(Empleado empleado) {
        LOG.log(INFO, "[formEmpleado][onEliminar] -> {0}", new Object[]{empleado});
        logEmpleado.eliminarEmpleado(empleado);
        empleados = logEmpleado.obtenerListado(null);
        cargarDatos(empleados);
    }

}