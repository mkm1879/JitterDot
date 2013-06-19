package edu.clemson.lph.jitter.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import edu.clemson.lph.jitter.geometry.StateBounds;
import edu.clemson.lph.jitter.logger.Loggers;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class ConfigFrame extends JFrame {

	private JPanel contentPane;
	private JTable tblDataFileLayout;
	private DataFileLayoutModel model;
	private JCheckBox ckDetailedLogging;
	private JCheckBox ckNaadsm;
	private JCheckBox ckInterspreadPlus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    try {
			        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    } catch (Exception e) {
			        Loggers.error( "Error setting look and feel\n" + e.getMessage() );
			    }
				try {
					ConfigFrame frame = new ConfigFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConfigFrame() {
		setTitle("JitterDot Configuration Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 650);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open Data File");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel pSettings = new JPanel();
		contentPane.add(pSettings, BorderLayout.NORTH);
		pSettings.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JPanel pStates = new JPanel();
		pStates.setBorder(new TitledBorder(null, "State(s) Included", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pSettings.add(pStates);
		pStates.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		String[] aStates = StateBounds.getStateList();
		JList<String> list = new JList<String>(aStates);
		list.setToolTipText("Use <CTRL> Click to Select Multiple States");
		JScrollPane statesPane = new JScrollPane(list);
		statesPane.setPreferredSize(new Dimension(300,100));
		statesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pStates.add(statesPane);
		

		ckDetailedLogging = new JCheckBox("Detailed Logging");
		pSettings.add(ckDetailedLogging);
		
		JPanel pOutput = new JPanel();
		pOutput.setBorder(new TitledBorder(null, "Output Type(s)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pSettings.add(pOutput);
		
		ckNaadsm = new JCheckBox("NAADSM");
		pOutput.add(ckNaadsm);
		
		ckInterspreadPlus = new JCheckBox("Interspread Plus");
		pOutput.add(ckInterspreadPlus);
		
		JPanel pDataFileLayout = new JPanel();
		pDataFileLayout.setBorder(new TitledBorder(null, "Data File Layout", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(pDataFileLayout, BorderLayout.CENTER);
		pDataFileLayout.setLayout(new BorderLayout(0, 0));
		
		JScrollPane spDateFileLayout = new JScrollPane();
		pDataFileLayout.add(spDateFileLayout, BorderLayout.CENTER);
		
		tblDataFileLayout = new JTable();
		// Hide the default header and use my row of combo boxes in row one instead
		tblDataFileLayout.setTableHeader(null);
		spDateFileLayout.setViewportView(tblDataFileLayout);
		setDataFile( new File( "Test.csv"));
	}
	
	public void setDataFile( File fData ) {
		try {
			model = new DataFileLayoutModel( fData );
			tblDataFileLayout.setModel(model);
			// Override the first rows cells to select mapping as combobox, all others still text boxes
			ConfigTableCellEditor cellEdit = new ConfigTableCellEditor();
			// Why does this not work
//			tblDataFileLayout.setCellEditor(cellEdit);
			// When this does?
			for( int i = 0; i < model.getColumnCount(); i++ ) {
				TableColumn col = tblDataFileLayout.getColumnModel().getColumn(i);
				col.setCellEditor(cellEdit);
			}
			// Override the first row cells to display selected mapping as combobox with item selected
			// Override second row to immitate row header look displaying first row of data file (header)
			// All others ordinary labels.
			ConfigTableCellRenderer rend = new ConfigTableCellRenderer();
			for( int i = 0; i < model.getColumnCount(); i++ ) {
				TableColumn col = tblDataFileLayout.getColumnModel().getColumn(i);
				col.setCellRenderer(rend);
			}
			tblDataFileLayout.setRowHeight(0,25);
			tblDataFileLayout.setRowHeight(1,20);

		} catch (IOException e) {
			Loggers.error(e);
		}
	}
	
	// Getters and Setters for settings on form
	
	public void setDetailedLoggingRequested( boolean b ) {
		ckDetailedLogging.setSelected(b);
	}
	
	public boolean getDetailedLoggingRequested() {
		boolean bRet = ckDetailedLogging.isSelected();
		return bRet;
	}
	
	public void setNaadsmRequested( boolean b) {
		ckNaadsm.setSelected(b);
	}
	
	public boolean getNaadsmRequested() {
		boolean bRet = ckNaadsm.isSelected();
		return bRet;
	}
	
	public void setInterspreadPlusRequested( boolean b) {
		ckInterspreadPlus.setSelected(b);
	}
	
	public boolean getInterspreadPlusRequested() {
		boolean bRet = ckInterspreadPlus.isSelected();
		return bRet;
	}
	
	public void setMappedColumn( String sStandardColumn, String sInputColumn ) {
		for( int iCol = 0; iCol < tblDataFileLayout.getColumnCount(); iCol++ ) {
			if( tblDataFileLayout.getValueAt(1, iCol).equals( sInputColumn ) ) {
				tblDataFileLayout.setValueAt(sStandardColumn, 0, iCol);
				break;
			}
		}
	}
	
	public String getMappedColumn( String sStandardColumn ) {
		String sRet = null;
		for( int iCol = 0; iCol < tblDataFileLayout.getColumnCount(); iCol++ ) {
			if( tblDataFileLayout.getValueAt(0, iCol).equals( sStandardColumn ) ) {
				sRet = (String)tblDataFileLayout.getValueAt(1, iCol);
				break;
			}
		}
		return sRet;
	}
}
