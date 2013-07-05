package edu.clemson.lph.jitter.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import edu.clemson.lph.jitter.files.SourceCSVFile;

@SuppressWarnings("serial")
public class ConfigTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    DefaultCellEditor other = new DefaultCellEditor(new JTextField());
    private DefaultCellEditor lastSelected;
	private DefaultCellEditor combo;
	private int iLastColumn = -1;
	private int iLastRow = -1;
	private JTable table = null;
	private String sLastValue = null;

	private String[] aStdCols = new String[SourceCSVFile.getStandardColumns().length + 1];
	private JComboBox<String> box;

	public ConfigTableCellEditor() {		
		aStdCols[0] = "";
		for( int i = 1; i <= SourceCSVFile.getStandardColumns().length; i++ ) {
			aStdCols[i] = SourceCSVFile.getStandardColumns()[i-1];
		}
		box = new JComboBox<String>(aStdCols);
		combo = new DefaultCellEditor(box);
		box.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sThisValue = (String)box.getSelectedItem();
				if( sThisValue != null && !sThisValue.equals(sLastValue)) {
					combo.stopCellEditing();
					table.getModel().setValueAt(box.getSelectedItem(), iLastRow, iLastColumn);
					sLastValue = sThisValue;
				}
				// This is a huge Kluge to force this control to give up focus so next click activates somewhere else.
				table.selectAll();
			}
		});

	}


	@Override
	public Object getCellEditorValue() {
		return lastSelected.getCellEditorValue();
	}

    @Override
    public Component getTableCellEditorComponent(JTable table,
            Object value, boolean isSelected, int row, int column) {
        if(row == 0) {
            lastSelected = combo;
            iLastRow = row;
            iLastColumn = column;
            this.table = table;
            return combo.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
        lastSelected = other;
        return other.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

}
