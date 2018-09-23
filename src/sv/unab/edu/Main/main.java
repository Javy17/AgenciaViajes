package sv.unab.edu.Main;

import sv.unab.edu.Presentacion.FormPersona;

import javax.swing.*;
import java.awt.*;

public class main
{
    public static void main(String[] args)
    {
        // Obteniendo resoluciones de pantalla
        Dimension resolucionPantalla =  Toolkit.getDefaultToolkit().getScreenSize();
        // Ajustando tamaño del formulario
        Dimension ajusteTamaño =  new  Dimension (resolucionPantalla.width / 2 , resolucionPantalla.height -  100 );
        JFrame frm = new JFrame("Estudiante de Program 4");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setContentPane(new FormPersona().panelRais);
        //frm.setContentPane(new FormVehiculo1().panelRais);
        //frm.pack();
        //frm.setVisible(true);
        //frm.setLocationRelativeTo(null);
        //frm.setVisible(true);
        //frm.setPreferredSize(ajusteTamaño);
        frm.setResizable(false);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }
}
