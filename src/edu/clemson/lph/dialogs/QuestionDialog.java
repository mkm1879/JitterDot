package edu.clemson.lph.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.border.*;
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
public class QuestionDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private Window mParent = (Window)null;
	private String mTitle;
	private int deltaX = 0;
	private int deltaY = 0;
	private boolean bOK = false;
	private String sQuestion;
	private String sAnswer;

	private JLabel lQuestion = new JLabel();
	private JTextField jtfAnswer = new JTextField();

	/**
	@wbp.parser.constructor 
	 */
	public QuestionDialog( String sTitle, String sQuestion, boolean bModal ) {
		setModal(bModal);
		mTitle = sTitle;
		this.sQuestion = sQuestion;
		initGui();
	}

	public QuestionDialog( Window parent, String sTitle, String sQuestion, boolean bModal ) {
		super( parent );
		setModal(bModal);
		mParent = parent;
		mTitle = sTitle;
		this.sQuestion = sQuestion;
		initGui();
	}
	
	public static String ask(Window parent, String sTitle, String sQuestion) {
		QuestionDialog dlg = new QuestionDialog( parent, sTitle, sQuestion, true);
		dlg.center();
		dlg.setVisible(true);
		return dlg.getAnswer();
	}
	
	static private String ssAnswer;
	
	public static String askWait(Window parent, String sTitle, String sQuestion) {
		final Window fparent = parent;
		final String fTitle = sTitle;
		final String fQuestion = sQuestion;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					QuestionDialog dlg = new QuestionDialog( fparent, fTitle, fQuestion, true);
					dlg.center();
					dlg.setVisible(true);
					ssAnswer = dlg.getAnswer();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return ssAnswer;
	}


	public void setAnswer(String sAnswer) { this.sAnswer = sAnswer; }
	public String getAnswer() { return sAnswer; }

	public void setDeltas( int deltaX, int deltaY ) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	public boolean isExitOK() {
		return bOK;
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

	/**
	 * Create the dialog.
	 */
	public void initGui() {
		setBounds(100, 100, 450, 300);
		setTitle(mTitle);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lQuestion.setText(sQuestion);
		lQuestion.setBounds(27, 58, 337, 14);
		contentPanel.add(lQuestion);
		jtfAnswer.setText("");
		jtfAnswer.setBounds(27, 84, 337, 20);
		contentPanel.add(jtfAnswer);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						bOK = true;
						sAnswer = jtfAnswer.getText();
						QuestionDialog.this.setVisible(false);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						bOK = false;
						QuestionDialog.this.setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
}
