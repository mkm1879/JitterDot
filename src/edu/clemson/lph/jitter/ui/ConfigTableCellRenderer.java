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
