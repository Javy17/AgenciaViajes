package sv.unab.edu.Presentacion;

import com.toedter.calendar.JDateChooser;
import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Dominio.Empleado;
import sv.unab.edu.Dominio.Persona;
import sv.unab.edu.Infraestructura.logCliente;
import sv.unab.edu.Infraestructura.logEmpleado;
import sv.unab.edu.Presentacion.Util.Utilidades;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.ZoneId;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class formGestionEmpleado extends JDialog {

    private static final Logger LOG = Logger.getLogger("sv.edu.unab.agenciaviajes");

    public JPanel contentPane;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JFormattedTextField ftxtDUI;
    private JFormattedTextField ftxtNIT;
    private JTextField txtCorreo;
    private JTextArea txtaDireccion;
    private JDateChooser dcNacimiento;
    private JFormattedTextField ftxtTelefono;
    private JTextField txtSeguro;
    private JTextField txtAFP;

    private Empleado empleadoSeleccionado;
    private logEmpleado logEmpleado;

    public formGestionEmpleado(Empleado empleado/*, JDialog parent*/) {
        //super(parent, true);
        LOG.log(INFO, "[formGestionEmpleado][formGestionEmpleado] -> {0}", new Object[]{empleado});
        logEmpleado = new logEmpleado();
        empleadoSeleccionado = empleado;
        this.setContentPane(contentPane);
        this.getRootPane().setDefaultButton(btnGuardar);

        btnGuardar.addActionListener(e -> onGuardar());

        btnCancelar.addActionListener(e -> onCancel());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        Utilidades.duiFormato(ftxtDUI, this);
       // Utilidades.nitFormato(ftxtNIT, this);
        Utilidades.telefonoFormato(ftxtTelefono, this);
        llenarCampos();
    }

    public formGestionEmpleado() {
        this.setContentPane(contentPane);
        this.setModal(true);
        this.getRootPane().setDefaultButton(btnGuardar);

        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGuardar();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void llenarCampos() {
        if (empleadoSeleccionado != null) {
            txtNombre.setText(empleadoSeleccionado.getDatosPersonales().getNombre());
            txtApellidoPaterno.setText(empleadoSeleccionado.getDatosPersonales().getApellidoPaterno());
            txtApellidoMaterno.setText(empleadoSeleccionado.getDatosPersonales().getApellidoMaterno());
            ftxtDUI.setValue(empleadoSeleccionado.getDatosPersonales().getDui());
            //ftxtNIT.setValue(empleadoSeleccionado.getDatosPersonales().getNit());
            dcNacimiento.setDate(Date.valueOf(empleadoSeleccionado.getDatosPersonales().getFechaNacimiento()));
            ftxtTelefono.setValue(empleadoSeleccionado.getDatosPersonales().getTelefono());
            txtCorreo.setText(empleadoSeleccionado.getDatosPersonales().getEmail());
            txtaDireccion.setText(empleadoSeleccionado.getDatosPersonales().getDireccion());
            txtSeguro.setText(empleadoSeleccionado.getSeguro());
            txtAFP.setText(empleadoSeleccionado.getAfp());
        }
    }

    private void onGuardar() {
        // add your code here
        try {
            Empleado empleado = new Empleado();
            Persona persona = new Persona();
            persona.setNombre(txtNombre.getText());
            persona.setApellidoPaterno(txtApellidoPaterno.getText());
            persona.setApellidoMaterno(txtApellidoMaterno.getText().isEmpty() ? null : txtApellidoMaterno.getText());
            persona.setDui(ftxtDUI.getText());
            //persona.setNit(ftxtNIT.getText());
            persona.setFechaNacimiento(dcNacimiento.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            persona.setTelefono(ftxtTelefono.getText());
            persona.setEmail(txtCorreo.getText());
            persona.setDireccion(txtaDireccion.getText());
            if (empleadoSeleccionado != null) {
                empleado.setId(empleadoSeleccionado.getId());
                if (empleadoSeleccionado.getDatosPersonales() != null) {
                    persona.setId(empleadoSeleccionado.getDatosPersonales().getId());
                }
            }
            empleado.setDatosPersonales(persona);
            empleado.setSeguro(txtSeguro.getText());
            empleado.setAfp(txtAFP.getText());
            long currentInit = System.currentTimeMillis();
            if (empleadoSeleccionado != null) {
                 logEmpleado.actualizarEmpleado(empleado);
            } else {
                logEmpleado.registrarEmpleado(empleado);
            }
            long currentFin = System.currentTimeMillis();
            long time = currentFin - currentInit;
            LOG.log(INFO, "[formGestionEmpleado][onGuardar] -> Tiempo de ejecucion {0}", new Object[]{time});
            this.setVisible(false);
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        formGestionEmpleado dialog = new formGestionEmpleado();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        dcNacimiento = new JDateChooser();
        dcNacimiento.setDateFormatString("EEEE dd 'de' MMMMM 'de' yyyy");
    }
}