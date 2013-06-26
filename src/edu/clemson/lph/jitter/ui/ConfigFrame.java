package edu.clemson.lph.jitter.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.FlowLayout;
import java.beans.Beans;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.geometry.StateBounds;
import edu.clemson.lph.jitter.logger.Loggers;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ConfigFrame extends JFrame {
	private ConfigController control;

	private JPanel contentPane;
	private JTable tblDataFileLayout;
	private DataFileLayoutModel model;
	private JCheckBox ckDetailedLogging;
	private JCheckBox ckNaadsm;
	private JCheckBox ckInterspreadPlus;
	private JList<String> lbStates;
	private JTextArea lblWarnings;
	private JSpinner spinMinK;
	private JSpinner spinMinGroup;

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
	
	JTable getTable() { return tblDataFileLayout; }
	DataFileLayoutModel getModel() { return model; }
	String getBaseTitle() { return "JitterDot Configuration Editor"; }

	/**
	 * Create the frame.
	 */
	public ConfigFrame() {
		control = new ConfigController( this );
		setTitle(getBaseTitle());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 650);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open Data File");
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File fDir = new File(".");
				JFileChooser fDialog = new JFileChooser(fDir);
				fDialog.setFileFilter(new FileNameExtensionFilter("Comma Separated Values File", "csv"));
				int iRet = fDialog.showOpenDialog(ConfigFrame.this);
				if( iRet == JFileChooser.APPROVE_OPTION) {
					File fNew = fDialog.getSelectedFile();
					control.setDataFile( fNew );
				}
			}
		});
		
		JMenuItem mntmOpenConfigFile = new JMenuItem("Open Config File");
		mnFile.add(mntmOpenConfigFile);
		mntmOpenConfigFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File fDir = new File(".");
				JFileChooser fDialog = new JFileChooser(fDir);
				fDialog.setFileFilter(new FileNameExtensionFilter("JitterDot Config File", "config"));
				int iRet = fDialog.showOpenDialog(ConfigFrame.this);
				if( iRet == JFileChooser.APPROVE_OPTION) {
					File fNew = fDialog.getSelectedFile();
					String sNewPath = fNew.getPath();
					control.loadConfig( sNewPath );
				}
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save Configuration");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				control.saveConfig();
			}
		});
		
		JMenuItem mntmSaveAs = new JMenuItem("Save Configuration As");
		mnFile.add(mntmSaveAs);
		mntmSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File fDir = new File(".");
				JFileChooser fDialog = new JFileChooser(fDir);
				fDialog.setFileFilter(new FileNameExtensionFilter("JitterDot Config File", "config"));
				int iRet = fDialog.showSaveDialog(ConfigFrame.this);
				if( iRet == JFileChooser.APPROVE_OPTION) {
					File fNew = fDialog.getSelectedFile();
					String sNewPath = fNew.getPath();
					control.saveConfigAs( sNewPath );
				}
			}
		});
		
		JMenuItem mntmRun = new JMenuItem("Run Deidentification");
		mnFile.add(mntmRun);
		mntmRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				control.runJitter();
			}
		});
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConfigFrame.this.setVisible(false);
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel pSettings = new JPanel();
		contentPane.add(pSettings, BorderLayout.NORTH);
		pSettings.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel pStates = new JPanel();
		pStates.setBorder(new TitledBorder(null, "State(s) Included", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pSettings.add(pStates);
		String[] aStates = StateBounds.getStateList();
		if( !Beans.isDesignTime() ) 
			lbStates = new JList<String>(aStates);
		else
			lbStates = new JList<String>();
		pStates.setLayout(new BorderLayout(0, 0));
		lbStates.setToolTipText("Use <CTRL> Click to Select Multiple States");
		JScrollPane statesPane = new JScrollPane(lbStates);
		statesPane.setPreferredSize(new Dimension(200, 150));
		statesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pStates.add(statesPane);
		
		JPanel pDetails = new JPanel();
		pDetails.setBorder(new EmptyBorder(10, 0, 0, 0));
		pSettings.add(pDetails);
		pDetails.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel pTop = new JPanel();
		pDetails.add(pTop);
		

		ckDetailedLogging = new JCheckBox("Detailed Logging");
		pTop.add(ckDetailedLogging);
		
		JPanel pOutput = new JPanel();
		pTop.add(pOutput);
		pOutput.setBorder(new TitledBorder(null, "Output Type(s)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		ckNaadsm = new JCheckBox("NAADSM");
		ckNaadsm.addActionListener(control);
		pOutput.add(ckNaadsm);
		
		ckInterspreadPlus = new JCheckBox("Interspread Plus");
		ckInterspreadPlus.addActionListener(control);
		pOutput.add(ckInterspreadPlus);
		
		JPanel pJitter = new JPanel();
		pDetails.add(pJitter);
		pJitter.setLayout(null);
		
		JLabel lblKminimumAnonymity = new JLabel("K (Minimum Anonymity):");
		lblKminimumAnonymity.setBounds(23, 7, 115, 14);
		pJitter.add(lblKminimumAnonymity);
		
		spinMinK = new JSpinner();
		spinMinK.setBounds(143, 5, 38, 18);
		spinMinK.setModel(new SpinnerNumberModel(new Integer(5), new Integer(1), null, new Integer(1)));
		pJitter.add(spinMinK);
		
		JLabel lblMinimumGroup = new JLabel("Minimum Group:");
		lblMinimumGroup.setBounds(201, 7, 76, 14);
		pJitter.add(lblMinimumGroup);
		
		spinMinGroup = new JSpinner();
		spinMinGroup.setBounds(281, 5, 38, 18);
		spinMinGroup.setModel(new SpinnerNumberModel(new Integer(5), new Integer(1), null, new Integer(1)));
		pJitter.add(spinMinGroup);
		
		JPanel pBottom = new JPanel();
		pBottom.setBorder(new EmptyBorder(0, 30, 0, 0));
		pDetails.add(pBottom);
		pBottom.setLayout(new BorderLayout(0, 0));
		
		lblWarnings = new JTextArea("No Output Requested\nColumn Mapping Incomplete");
		lblWarnings.setBackground(SystemColor.inactiveCaptionBorder);
		lblWarnings.setLineWrap(true);
		lblWarnings.setWrapStyleWord(true);
		lblWarnings.setForeground(Color.RED);
		lblWarnings.setFont(new Font("Tahoma", Font.BOLD, 14));
		pBottom.add(lblWarnings, BorderLayout.NORTH);
		
		JPanel pDataFileLayout = new JPanel();
		pDataFileLayout.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Data File Layout (Column Mapping)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(pDataFileLayout, BorderLayout.CENTER);
		pDataFileLayout.setLayout(new BorderLayout(0, 0));
		
		JScrollPane spDateFileLayout = new JScrollPane();
		pDataFileLayout.add(spDateFileLayout, BorderLayout.CENTER);
		
		tblDataFileLayout = new JTable();
		tblDataFileLayout.setModel( new DataFileLayoutModel() );
		// Hide the default header and use my row of combo boxes in row one instead
		tblDataFileLayout.setTableHeader(null);
		spDateFileLayout.setViewportView(tblDataFileLayout);
		control.loadConfig();
//		control.setDataFile( new File( "Test.csv"));
	}
	
	void setLayoutTableModelData( File fData ) {
		try {
			model = new DataFileLayoutModel( fData );
			tblDataFileLayout.setModel(model);
			// Override the first rows cells to select mapping as combobox, all others still text boxes
			ConfigTableCellEditor cellEdit = new ConfigTableCellEditor();
			// Why does this not work?
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
			model.addTableModelListener(control);

		} catch (IOException e) {
			Loggers.error(e);
		}
	}
	
	// Getters and Setters for settings on form
	
	public List<String> getStates() {
		return lbStates.getSelectedValuesList();
	}
	
	public void setStates( List<String> states ) {
		String[] aStates = StateBounds.getStateList();
		int[] iStates = new int[states.size()];
		int i = 0;
		for( String state : states ) {
			int index = Arrays.binarySearch(aStates, state);
			iStates[i++] = index;
		}
		lbStates.setSelectedIndices( iStates );
	}
	
	public void setDetailedLoggingRequested( boolean b ) {
		ckDetailedLogging.setSelected(b);
	}
	
	public boolean isDetailedLoggingRequested() {
		boolean bRet = ckDetailedLogging.isSelected();
		return bRet;
	}
	
	public void setNaadsmRequested( boolean b) {
		ckNaadsm.setSelected(b);
	}
	
	public void setMinK( Integer iK ) {
		spinMinK.setValue(iK);
	}
	
	public Integer getMinK() {
		return (Integer)spinMinK.getValue();
	}
	
	public void setMinGroup( Integer iGroup ) {
		spinMinGroup.setValue(iGroup);
	}
	
	public Integer getMinGroup() {
		return (Integer)spinMinGroup.getValue();
	}
	
	public boolean isNAADSMRequested() {
		boolean bRet = ckNaadsm.isSelected();
		return bRet;
	}
	
	public void setInterspreadPlusRequested( boolean b) {
		ckInterspreadPlus.setSelected(b);
	}
	
	public boolean isInterspreadRequested() {
		boolean bRet = ckInterspreadPlus.isSelected();
		return bRet;
	}
	
	/**
	 * Set the data file used to map columns in source data to output columns.
	 * @param sDataFile String relative or absolute path to data file available for read access.
	 */
	public void setDataFile( String sDataFile ) throws FileNotFoundException, IOException {
		File fFile = new File( sDataFile );
		if( !fFile.exists() ) 
			throw new FileNotFoundException( "File '" + sDataFile + "' not found" );			
		if( !fFile.canRead() ) 
			throw new IOException( "File '" + sDataFile + "' cannot be read" );
		control.setDataFile( fFile );
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
	
	public void setWarnings( String sWarnings ) {
		if( sWarnings != null && sWarnings.trim().length() > 0 ) {
			lblWarnings.setText(sWarnings);
			lblWarnings.setVisible(true);
		}
		else {
			lblWarnings.setVisible(false);
		}
	}
}
