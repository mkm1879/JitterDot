package edu.clemson.lph.jitter.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;

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
public class ConfigTableCellRenderer implements TableCellRenderer {
	private JLabel header = new JLabel();
	private JLabel other = new JLabel();
	private ArrayList<JComboBox<String>> aCombos = new ArrayList<JComboBox<String>>();
	private String[] aStdCols = new String[SourceCSVFile.getStandardColumns().length + 1];

	public ConfigTableCellRenderer() {		
		aStdCols[0] = "";
		for( int i = 1; i <= SourceCSVFile.getStandardColumns().length; i++ ) {
			aStdCols[i] = SourceCSVFile.getStandardColumns()[i-1];
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
		if( row == 0 ) {
			JComboBox<String> combo;
			if( column >= aCombos.size() ) {
				for( int i = aCombos.size(); i <= column + 1; i++ ) {
					JComboBox<String> box = new JComboBox<String>(aStdCols);
					aCombos.add(box);
				}
			}
			combo = aCombos.get(column);	
			combo.setSelectedItem(value);
			return combo;
		}
		else if (row == 1 ) {
			header.setText((String)value);
			header.setOpaque(true);
			header.setBackground(Color.LIGHT_GRAY);
			header.setForeground(Color.BLACK);
			header.setBorder(new BevelBorder(BevelBorder.RAISED));
			return header;
		}
		else {
			other.setText((String)value);
			return other;
		}
	}

}
