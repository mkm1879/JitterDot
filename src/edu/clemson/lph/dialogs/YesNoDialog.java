package edu.clemson.lph.dialogs;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.*;

import edu.clemson.lph.jitter.logger.Loggers;

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
public class YesNoDialog extends JDialog {
	  private static final long serialVersionUID = 1L;
	  private String sQuestion;
	  private JTextArea jlQuestion = new JTextArea();
	  private JPanel panel1 = new JPanel();
	  private JButton jbYes = new JButton();
	  private JButton jbNo = new JButton();
	  boolean bAnswer = false;
		private int deltaX = 0;
		private int deltaY = 0;

	  public static final Logger logger = Loggers.getLogger();

	  public static boolean ask( Frame parent, String sTitle, String sQuestion ) {
	    YesNoDialog ask = new YesNoDialog( parent, sTitle, sQuestion );
	    ask.setModal(true);
	    ask.setVisible(true);
	    return ask.getAnswer();
	  }

	  protected void performEnterAction(KeyEvent e) {
	    jbNo.setSelected(true);
	    setVisible(false);
	  }

	  public static boolean ask( Dialog parent, String sTitle, String sQuestion ) {
	    YesNoDialog ask = new YesNoDialog( parent, sTitle, sQuestion );
	    ask.setModal(true);
	    ask.setVisible(true);
	    return ask.getAnswer();
	  }

	  public void setToolTip( String sToolTip ) {
	    jbYes.setToolTipText(sToolTip);
	    jbNo.setToolTipText(sToolTip);
	  }

	  public boolean getAnswer() {
	    return bAnswer;
	  }

	  public void setAnswer( boolean bAnswer ) {
	    this.bAnswer = bAnswer;
	    if( bAnswer ) {
	      jbYes.setSelected(true);
	      jbNo.setSelected(false);
	    }
	    else{
	      jbYes.setSelected(false);
	      jbNo.setSelected(true);
	    }
	  }

	  public YesNoDialog(Frame parent, String title, String sQuestion) {
	    super(parent, title, true);
	    this.sQuestion = sQuestion;
	    try {
	      jbInit();
	      pack();
	    }
	    catch(Exception ex) {
	      logger.error(ex.getMessage() + "\nError in UNIDENTIFIED" );
	    }
	  }
	  public YesNoDialog(Dialog parent, String title, String sQuestion) {
	    super(parent, title, true);
	    this.sQuestion = sQuestion;
	    try {
	      jbInit();
	      pack();
	    }
	    catch(Exception ex) {
	    	logger.error(ex.getMessage() + "\"Error in UNIDENTIFIED" );
	    }
	  }

	  public YesNoDialog() {
	    this((Frame)null, "Title", "Test Question");
	  }

		public void setDeltas( int deltaX, int deltaY ) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}

		public void center() {
			//Center the window
			boolean bSmall = false;
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = getSize();
			if( frameSize.height > screenSize.height ) {
				frameSize.height = screenSize.height;
				bSmall = true;
			}
			if( frameSize.width > screenSize.width ) {
				frameSize.width = screenSize.width;
				bSmall = true;
			}
			if( bSmall ) {
				setLocation( (screenSize.width - frameSize.width) / 2, 0);
			}
			else {
				setLocation( deltaX + (screenSize.width - frameSize.width) / 2,
						deltaY + (screenSize.height - frameSize.height) / 2);
			}
		}

	  private void jbInit() throws Exception {
	    jlQuestion.setEditable(false);
	    jlQuestion.setLineWrap(true);
	    jlQuestion.setWrapStyleWord(true);
	    jlQuestion.setBackground(SystemColor.control);
	    panel1.setLayout(null);
	    jbYes.setText("Yes");
	    jbYes.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jbYes_actionPerformed(e);
	      }
	    });
	    jbNo.setText("No");
	    jbNo.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jbNo_actionPerformed(e);
	      }
	    });
	    panel1.setMinimumSize(new Dimension(400, 150));
	    panel1.setPreferredSize(new Dimension(400, 150));
	    jlQuestion.setText(sQuestion);
	    getContentPane().add(panel1);
	    jlQuestion.setBounds(16, 20, 364, 42);
	    panel1.add(jlQuestion);
	    jbNo.setBounds(207, 93, 90, 25);
	    panel1.add(jbNo);
	    jbYes.setBounds(89, 93, 90, 25);
	    panel1.add(jbYes);
	    center();
	  }

	  void jbYes_actionPerformed(ActionEvent e) {
	    bAnswer = true;
	    setVisible(false);
	  }

	  void jbNo_actionPerformed(ActionEvent e) {
	    bAnswer = false;
	    setVisible(false);
	  }

	  public static void main( String args[] ) {
	    boolean bAnswer = YesNoDialog.ask( (Frame)null, "this is a question", "yes or no" );
	    System.out.println( "you answered " + bAnswer );
	  }
}
