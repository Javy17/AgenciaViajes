
package sv.unab.edu.Presentacion;
import sv.unab.edu.Presentacion.*;
import javax.swing.*;
import java.awt.*;

public class Principal extends JFrame
{

    private Principal()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JDesktopPane desktopPane = new JDesktopPane();
        JMenuBar barra = new JMenuBar();
        JMenu formularios = new JMenu("Formularios");
        JMenu salir = new JMenu("Salir");
        barra.add(formularios);
        barra.add(salir);
        JMenuItem clie = new JMenuItem("Cliente");
        JMenuItem emp = new JMenuItem("Empleado");
        JMenuItem sal = new JMenuItem("Cerrar menú");
        formularios.add(clie);
        formularios.add(emp);
        salir.add(sal);
        setJMenuBar(barra);
        desktopPane.setBackground(Color.darkGray);
        barra.setBackground(Color.LIGHT_GRAY);
        setContentPane(desktopPane);
        clie.addActionListener(e -> {
            //desktopPane.removeAll();
            formCliente f = new formCliente();
            f.show();
            this.setSize(f.getWidth() + 16, f.getHeight() + 60);
            desktopPane.add(f);
        });
        emp.addActionListener(e ->
        {
            //desktopPane.removeAll();
            formEmpleado f = new formEmpleado();
            this.pack();
            this.setSize(f.getWidth() + 16, f.getHeight() + 60);
            f.show();
            desktopPane.add(f);

        });
        sal.addActionListener(e -> this.dispose());
    }

    public static void main(String[] args) {
        Principal f = new Principal();
        f.pack();
        f.setVisible(true);
        Dimension df = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dr = new Dimension(df.width / 4, df.height / 4);
        f.setSize(dr);

    }
}

