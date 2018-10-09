package sv.unab.edu.Main;

import sv.unab.edu.Dominio.*;
import sv.unab.edu.Presentacion.Util.Utilidades;
import sv.unab.edu.Presentacion.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.*;
import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.util.List;

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
        //frm.setContentPane(new FormPersona().panelRais);
        //frm.setContentPane(new formGestionEmpleado().contentPane);
        frm.setContentPane(new formEmpleado().pnlRoot);

        /*EntityManagerFactory emf = Persistence.createEntityManagerFactory("agencia_vuelo");
        EntityManager em = emf.createEntityManager();
        Query query = em.createNamedQuery("Persona.finTodas");
        List<Persona> res = query.getResultList();
        res.forEach(x -> System.out.print(x));*/

        //frm.setContentPane(new FormVehiculo1().panelRais);
        //frm.pack();
        //frm.setVisible(true);
        //frm.setLocationRelativeTo(null);
        //frm.setVisible(true);
        //frm.setPreferredSize(ajusteTamaño);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setPreferredSize(Utilidades.getSize());

        frm.setResizable(false);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }
}
