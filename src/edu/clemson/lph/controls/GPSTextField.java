package edu.clemson.lph.controls;

import javax.swing.JTextField;
import javax.swing.text.Document;
import java.awt.event.*;
import java.math.BigDecimal;
import javax.swing.*;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;

/**
 * <p>Title: SC Prem ID</p>
 * <p>Description: SC Premises Identification System</p>
 * <p>This Control translates any legitimate format of geographic coordinate into
 * decimal degrees and attempts to sanity check all input.</p>
 * Cloned here to function both as a GUI control if needed and as a source of a static
 * method to translate alternative formats to decimal degrees.
 * <p>Company: Clemson Livestock Poultry Health</p>
 * @author Michael K. Martin
 * @version 1.0
 */
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
public class GPSTextField extends JTextField {
  private boolean bTyped = false;
   // Whole slew of constructors needed to mimic JTextField
  public GPSTextField() {
    try {
      jbInit();
    }
    catch(Exception e) {
      Loggers.error(e);
    }
  }

  public GPSTextField(int p0) {
    super(p0);
    try {
      jbInit();
    }
    catch(Exception e) {
      Loggers.error(e);
    }
  }

  public GPSTextField(String p0) {
    super(p0);
    try {
      jbInit();
    }
    catch(Exception e) {
      Loggers.error(e);
    }
  }

  public GPSTextField(String p0, int p1) {
    super(p0, p1);
    try {
      jbInit();
    }
    catch(Exception e) {
      Loggers.error(e);
    }
  }

  public GPSTextField(Document p0, String p1, int p2) {
    super(p0, p1, p2);
    try {
      jbInit();
    }
    catch(Exception e) {
      Loggers.error(e);
    }
  }

  public void setText( String sText ) {
    super.setText( sText );
  }

  private void jbInit() throws Exception {
    this.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        // Only check value when leaving control after making changes
        if( bTyped ) {
          bTyped = false;
          String sText = getText();
          try {
            setText(convertGPS(sText));
            repaint();
          }
          catch (NumberFormatException nfe) {
            setText(sText); // give up and catch in dialog
          } catch (InvalidCoordinateException e1) {
              setText(sText); // give up and catch in dialog
          }
        }
      }
    });

    this.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        bTyped = true;
        char cKey = e.getKeyChar();
        // Try to reject any of the printable characters other than those
        // that can appear in any of the coordinate formats
        if ( (Character.isLetter(cKey) && ("WwNnEeSs".indexOf(cKey)) < 0) ||
            ( ("!@#$%^&*()_{}[]|\\\"';:?=~`/>,<".indexOf(cKey)) >= 0)) {
          java.awt.Toolkit.getDefaultToolkit().beep();
          e.consume();
        }
      }
    });
  }

  public static String convertGPS( String sText ) throws NumberFormatException, InvalidCoordinateException {
    StringBuffer sb = new StringBuffer();
    double dParts[] = new double[3];
    int iSign = 1;
    boolean bLat = false;
    boolean bInDecimal = false;
    int iGroup = 0;
    double dRet = 0.0;
    boolean bWasSpace = false;
    for( int i = 0; i < sText.trim().length(); i++ ) {
      char cNext = sText.charAt(i);
      if( "NnSs".indexOf(cNext)>=0 ) bLat = true;  // Check > 90 rule
      if( Character.isDigit(cNext) || cNext == '.' ) {
        sb.append(cNext);
        bWasSpace = false;
      }
      else if( "WwSs-".indexOf(cNext)>=0 ) {
        iSign = -1;
        bWasSpace = false;
      }
      else if( "EeNn+".indexOf(cNext)>=0 ) {
        iSign = 1;
        bWasSpace = false;
      }
      else if( Character.isSpaceChar(cNext) ) {
        if( !bWasSpace ) {
          if( bInDecimal ) {
            throw new InvalidCoordinateException( sText + " not parsible as a GPS coordinate.\n" +
                         "No groups make sense after a decimal: dd.dddd or dd mm.mmm" +
                         "Also accept ddd mm ss or even ddd mm ss.sss" );
          }
          if (iGroup > 2) {
        	  throw new InvalidCoordinateException( sText + " not parsible as a GPS coordinate.\n" +
                        "No more than three groups make sense: ddd mm ss." +
                        "Also accept dd.dddd or dd mm.mmm");
          }
          double dVal = Double.parseDouble(sb.toString());
          if( iGroup > 0 && dVal >= 60.0 ) {
        	  throw new InvalidCoordinateException( sText + " not parsible as a GPS coordinate.\n" +
                        "Minutes or Seconds cannot be >= 60.");
          }
          dParts[iGroup++] = dVal;
          sb.setLength(0);
          bWasSpace = true;
        }
      }
      else {
    	  throw new InvalidCoordinateException( sText + " not parsible as a GPS coordinate.\nUnexpected character present.");
      }
      if( '.' == cNext ) bInDecimal = true;
    }
    if( iGroup > 2 ) {
    	throw new InvalidCoordinateException( " not parsible as a GPS coordinate.\n" +
                  "Additional sections beyond Seconds don't make sense to me.");
    }
    dParts[iGroup++] = Double.parseDouble(sb.toString());
    int iDiv = 1;
    for( int i = 0; i < iGroup; i++ ) {
      dRet += (dParts[i]/iDiv);
      iDiv *= 60;
    }
    dRet *= iSign;
    if( (dRet*iSign) > 180.0 ) {
    	throw new InvalidCoordinateException( sText + " not parsible as a GPS coordinate.\n" +
                  "No coordinates exceed 180.  Latitude up to 90, Longitude up to 180.");
    }
    if( bLat && (dRet*iSign) > 90.0 ) {
    	throw new InvalidCoordinateException( sText + " not parsible as a GPS coordinate.\n" +
                  "Latitude cannot exceed 90.");
    }
    if( bLat && dRet == 90.0 && iSign > 0) {
    	throw new InvalidCoordinateException( sText + " not parsible as a GPS coordinate.\n" +
                  "Welcome Santa Clause!  You are the only one in the world with a North Latitude of 90");
    }
    return Double.toString( round( dRet, 6 ) );
  }
  
  public static boolean isValidLatitude( double dLat ) {
	  return dLat >= -90.0 && dLat <= 90.0;
  }
  
  public static boolean isValidLongitude( double dLong ) {
	  return dLong >= -180.0 && dLong <= 180.0;
  }


  private static double round( double dVal, int iPlaces ) {
    BigDecimal bd = new BigDecimal(dVal);
    bd = bd.setScale(iPlaces, BigDecimal.ROUND_HALF_UP);
    return bd.doubleValue();
  }

  public JDialog getDialogParent() {
    java.awt.Container parent = this.getParent();
    JDialog dParent = null;
    while (! (parent instanceof JFrame) && ! (parent instanceof JDialog)) {
      if (parent == null)return dParent; // I give up.  Where am I?
      parent = parent.getParent();
    }
    if (parent instanceof JDialog) dParent = (JDialog) parent;
    return dParent;
  }

}// End class GPSTextField
