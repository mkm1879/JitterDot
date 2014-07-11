package edu.clemson.lph.jitter.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import edu.clemson.lph.jitter.files.SourceCSVFile;
/*
Copyright 2014 Michael K Martin

This file is part of JitterDot.

JitterDot is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JitterDot is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the Lesser GNU General Public License
along with JitterDot.  If not, see <http://www.gnu.org/licenses/>.
*/
@SuppressWarnings("serial")
public class ConfigTableCellEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {
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
		box.setMaximumRowCount(SourceCSVFile.getStandardColumns().length + 1);
		combo = new DefaultCellEditor(box);
		box.addItemListener( this ); 
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


	@Override
	public void itemStateChanged(ItemEvent arg0) {
		String sThisValue = (String)box.getSelectedItem();
		if( (sLastValue != null && sThisValue == null) || 
			(sLastValue == null && sThisValue != null) || 
			( sLastValue != null && !sLastValue.equals(sThisValue) )) {
			combo.stopCellEditing();
			table.getModel().setValueAt(box.getSelectedItem(), iLastRow, iLastColumn);
			sLastValue = sThisValue;
		}
		// This is a huge Kluge to force this control to give up focus so next click activates somewhere else.
		table.selectAll();
	}

}

