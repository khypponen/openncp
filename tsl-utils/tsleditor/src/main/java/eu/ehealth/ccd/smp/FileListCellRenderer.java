/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ehealth.ccd.smp;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * JList Renderer for rendering the files in JList with their names
 * 
 * @author joao.cunha
 * 
 */
public class FileListCellRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        if (value instanceof File) {
            JLabel fileNameLabel = (JLabel)component;
            File file = (File) value;
            fileNameLabel.setText(file.getName());
            component = fileNameLabel;
//            if (isSelected) {
//                setBackground(list.getSelectionBackground());
//                setForeground(list.getSelectionForeground());
//            } else {
//                setBackground(list.getBackground());
//                setForeground(list.getForeground());
//            }
//            setEnabled(list.isEnabled());
//            setFont(list.getFont());
//            setOpaque(true);
        }
        return component;
    }
}
