package edu.clemson.lph.jitter.ui;

import java.awt.Component;

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
    DefaultCellEditor combo = null;
    private DefaultCellEditor lastSelected;
	private String[] aStdCols = new String[SourceCSVFile.getStandardColumns().length + 1];

	public ConfigTableCellEditor() {		
		aStdCols[0] = "";
		for( int i = 1; i <= SourceCSVFile.getStandardColumns().length; i++ ) {
			aStdCols[i] = SourceCSVFile.getStandardColumns()[i-1];
		}
		JComboBox<String> box = new JComboBox<String>(aStdCols);
		combo = new DefaultCellEditor(box);
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
            return combo.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
        lastSelected = other;
        return other.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

}

