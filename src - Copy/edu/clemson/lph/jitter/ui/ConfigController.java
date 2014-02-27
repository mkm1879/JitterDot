package edu.clemson.lph.jitter.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.clemson.lph.dialogs.MessageDialog;
import edu.clemson.lph.jitter.JitterThread;
import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.logger.Loggers;

public class ConfigController implements TableModelListener, ActionListener, FocusListener {
	private ConfigFrame frame;
	private String sDataFile;
	private DataFileLayoutModel model;

	public ConfigController() {
		frame = (ConfigFrame)null;
		model = new DataFileLayoutModel();
		model.addTableModelListener(this);

	}
	
	public ConfigController(ConfigFrame frame) {
		this.frame = frame;
		model = new DataFileLayoutModel();
		model.addTableModelListener(this);
	}
	
	public void loadConfig() {
		frame.setStates( ConfigFile.getStates() );
		frame.setDetailedLoggingRequested( ConfigFile.isDetailedLoggingRequested() );
		frame.setNaadsmRequested( ConfigFile.isNAADSMRequested() );
		frame.setInterspreadPlusRequested( ConfigFile.isInterspreadRequested() );
		frame.setMinK( ConfigFile.getMinK());
		frame.setMinGroup(ConfigFile.getMinGroup());
		frame.setUTMZone(ConfigFile.getUTMZone());
		// This is tortured logic.  Send the logic to Gitmo.
		DataFileLayoutModel model = (DataFileLayoutModel)frame.getTable().getModel();
		if( model.getDataFile() != null ) {
			// For every column key that SourceCSVFile knows about
			// Not just ones in Config File
			for( String sKey : SourceCSVFile.getStandardColumns() ) {
				// Find the mapping for that key.  Default is the key itself.
				String sMap = ConfigFile.mapColumn(sKey);
				// For every column in the data file
				for( int i = 0; i < model.getColumnCount(); i++ ) {
					// Read its header.  Row 1 rather than 0 because our model puts the map in 0
					String sHeader = (String)model.getValueAt(1,  i);
					// Assuming there is a value in the header and it matches the mapped value
					if( sHeader != null && sHeader.toLowerCase().equals(sMap.toLowerCase())) {
						// Fill in the drop down combo box with the standard column name for output.
						model.setValueAt(sKey, 0, i);
					}
				}
			}
		}
		// make sure the frame title reflects the current settings
		setFrameTitle();
	}
	
	private void setFrameTitle() {
		DataFileLayoutModel model = (DataFileLayoutModel)frame.getTable().getModel();
		File fConfig = ConfigFile.getConfigFile();
		String sConfig = fConfig == null?"":fConfig.getName();
		File fData = model.getDataFile();
		String sData = fData == null?"":fData.getName();
		frame.setTitle( frame.getBaseTitle() + " -- Config File: '" + sConfig + "' -- Data File: '" + sData +"'" );
	}
	
	public void loadConfig( String sNewFilePath ) {
		ConfigFile.setConfigFilePath(sNewFilePath);
		loadConfig();
	}
	
	
	public void setDataFile( File fData ) {
		this.sDataFile = fData.getPath();
		try {
			model.setDataFile(fData);
			frame.setLayoutTableModel(model);
			clearOldMappings();
			loadConfig();
		} catch (IOException e) {
			Loggers.error(e);
		}
	}
	
	private void clearOldMappings() {
		List<String> aFields = ConfigFile.listMapKeys();
		for( String sKey : ConfigFile.listMapKeys() ) {
			// Find the mapping for that key.  Default is the key itself.
			String sMap = ConfigFile.mapColumn(sKey);
			// For every column in the data file
			for( int i = 0; i < model.getColumnCount(); i++ ) {
				// Read its header.  Row 1 rather than 0 because our model puts the map in 0
				String sHeader = (String)model.getValueAt(1,  i);
				// Assuming there is a value in the header and it matches the mapped value
				if( sHeader != null && sHeader.equals(sMap)) {
					aFields.remove(sKey);
				}
			}
		}
		ConfigFile.clearFieldMappings(aFields);
	}

	
	public void saveConfigAs( String sFilePath ) {
		updateConfigData();
		ConfigFile.saveConfigAs(sFilePath);
		setFrameTitle();
	}
	
	public void saveConfig() {
		updateConfigData();
		ConfigFile.saveConfig();
	}
	
	private void updateConfigData() {
		ConfigFile.setStates(frame.getStates());
		ConfigFile.setDetailedLoggingRequested( frame.isDetailedLoggingRequested() );
		ConfigFile.setNAADSMRequested( frame.isNAADSMRequested() );
		ConfigFile.setInterspreadRequested( frame.isInterspreadRequested() );
		ConfigFile.setMinK( frame.getMinK());
		ConfigFile.setMinGroup(frame.getMinGroup());
		ConfigFile.setUTMZone(frame.getUTMZone());
		for( int i = 0; i < frame.getTable().getModel().getColumnCount(); i++ ) {
			String sHeader = (String)frame.getTable().getModel().getValueAt(1,  i);
			String sKey = (String)frame.getTable().getModel().getValueAt(0,  i);
			if( sHeader != null && sHeader.trim().length() > 0 && sKey != null && sKey.trim().length() > 0 )
				ConfigFile.setFieldMap(sKey, sHeader);
		}		
	}
	
	/**
	 * This method starts up a secondary thread to handle the actual jitter process
	 * and display some semblance of progress in a dialog.
	 * Run on currently specified file.
	 */
	public void runJitter() {
		runJitter( sDataFile );
	}

	/**
	 * Run jittering on specified data file.
	 * @param sDataFile
	 */
	public void runJitter( String sDataFile ) {
		Loggers.info("Running Jitter");
		if( !ConfigFile.validateDataFile( sDataFile ) ) {
			MessageDialog.messageLater(frame, "JitterDot: Error", "Config file " + ConfigFile.getConfigFile().getName() +
					                         " is missing key mappings or does not match data file " + sDataFile );
			return;
		}
		if( !frame.isInterspreadRequested() && !frame.isNAADSMRequested() ) {
			MessageDialog.messageLater(frame, "JitterDot: Error", "No Output Requested");
			return;
		}
		saveConfig();
		JitterThread thread = new JitterThread( frame, sDataFile );
		frame.setEditEnabled(false);
		thread.runJitter();
		
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int iColumn = e.getColumn();
		int iRow = e.getFirstRow();
		int iType = e.getType();
		if( iType == TableModelEvent.UPDATE && iRow == 0 ) {
			String sValue = (String)frame.getTable().getValueAt(iRow, iColumn);	
			for( int i = 0; i < frame.getTable().getColumnCount(); i++ ) {
				if( i != iColumn ) {
					String sOther = (String)frame.getTable().getValueAt(0, i);
					if( sValue != null && sValue.trim().length() > 0 && sValue.equals(sOther)) {
						MessageDialog.messageLater(frame, "JitterDot: Duplicate Map Warning", "Warning: Column " + sValue + " is already mapped.");
						return;
					}
				}
			}
			checkCompleteness();
			// Why the blink do I have to reapply these settings every time the data change?
			frame.getTable().setRowHeight(0,25);  // Should replace magic numbers but I know for now these are fixed.
			frame.getTable().setRowHeight(1,20);
		}
	}
	
	private void checkCompleteness() {
		boolean bRunEnabled = true;
		List<String> aValues = new ArrayList<String>();
		for( int i = 0; i < frame.getTable().getColumnCount(); i++ ) {
			String sValue = (String)frame.getTable().getValueAt(0, i);
			if( sValue != null && sValue.trim().length() > 0 ) {
				aValues.add(sValue);
			}
		}
		boolean bFoundAll = true;
		for( String sCol : SourceCSVFile.getEssentialColumns() ) {
			if( !aValues.contains(sCol) ) {
				bFoundAll = false;
				break;
			}
		}
		String sWarnings = new String();
		if( !frame.isInterspreadRequested() && !frame.isNAADSMRequested() ) {
			sWarnings += "No Output Requested\n";
			bRunEnabled = false;
		}
		if( !bFoundAll ) {
			sWarnings += "Column Mapping Incomplete";
			bRunEnabled = false;
		}
		if( !isCoordinateMapValid(aValues) ) {
			sWarnings += "Coordinate Mapping Incomplete";
			bRunEnabled = false;
		}
		frame.setWarnings( sWarnings );		
		frame.setRunEnabled( bRunEnabled );
	}
	
	private boolean isCoordinateMapValid( List<String> aValues ) {
		boolean bRet = false;
		if( aValues.contains("Latitude") && aValues.contains("Longitude") )
			bRet = true;
		else if( aValues.contains("Northing") && aValues.contains("Easting") ) {
			 String sZone =  frame.getUTMZone();
			 if( sZone != null && Character.isDigit(sZone.charAt(0)) && 
					 (sZone.toUpperCase().endsWith("N") || sZone.toUpperCase().endsWith("S")) )
				 bRet = true;
		}
		return bRet;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		checkCompleteness();		
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// ignore
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		checkCompleteness();
	}
	

}
