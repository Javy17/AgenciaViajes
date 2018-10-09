/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.unab.edu.Presentacion.Util;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author dakrpastiursSennin
 */
public class DefaultTableModelImpl extends DefaultTableModel {

    protected List<Integer> colsButton;

    public DefaultTableModelImpl(List<Integer> colsButton) {
        this.colsButton = colsButton;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int vColIndex) {
        return colsButton.contains(vColIndex);
    }
}