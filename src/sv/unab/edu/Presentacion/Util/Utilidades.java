package sv.unab.edu.Presentacion.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;

public class Utilidades
{
    public static Dimension getSize() {
        return new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height - 200);
    }

    public static void reiniciarJTable(JTable tbl) {
        DefaultTableModel modelo = (DefaultTableModel) tbl.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }

    public static void reiniciarJList(JList lst){
        DefaultListModel modelo = (DefaultListModel) lst.getModel();
        while (modelo.getSize() > 0){
            modelo.remove(0);
        }
    }

    public static void setAdjustColumnSize(JTable tbl) {
        for (int column = 0; column < tbl.getColumnCount(); column++) {
            TableColumn tableColumn = tbl.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int preferredHeight = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();
            int maxHeight = 300;

            for (int row = 0; row < tbl.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tbl.getCellRenderer(row, column);
                Component c = tbl.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tbl.getIntercellSpacing().width;
                int height = c.getPreferredSize().height + tbl.getIntercellSpacing().height;
                preferredWidth = Math.max(preferredWidth, width);
                preferredHeight = Math.max(preferredHeight, height);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
                if (preferredHeight >= maxHeight) {
                    preferredHeight = maxHeight;
                    break;
                }
            }
            tableColumn.setPreferredWidth(preferredWidth);
            tbl.setRowHeight(preferredHeight);
        }
    }

    public static void duiFormato(JFormattedTextField txt, Component ventana) {
        try {
            txt.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("########-#")));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void nitFormato(JFormattedTextField txt, Component ventana) {
        try {
            txt.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("####-######-###-#")));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void telefonoFormato(JFormattedTextField txt, Component ventana) {
        try {
            txt.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("####-####")));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Sistema de Compra y Ventas - Validaciones", JOptionPane.ERROR_MESSAGE);
        }
    }
}
