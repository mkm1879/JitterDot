package edu.clemson.lph.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

public class MessageDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private Window parent = null;
	private String mTitle;
	private String mMessage;
	private int deltaX = 0;
	private int deltaY = 0;
	private boolean bOK = false;
	public static final int OK_ONLY = 0;
	public static final int CANCEL_ONLY = 1;
	public static final int BOTH_BUTTONS = 2;
	private int iButtons = BOTH_BUTTONS;


	/**
	 * Create the dialog.
	 */
	public MessageDialog() {
	}

	public MessageDialog( String sTitle, String sMessage ) {
		mTitle = sTitle;
		mMessage = sMessage;
	}

	public MessageDialog( Window parent, String sTitle, String sMessage ) {
		super( parent );
		this.parent = parent;
		mTitle = sTitle;
		mMessage = sMessage;
	}
	
	@Override
	public void setVisible( boolean bShow ) {
		if( bShow ) {
			initGui();
		}
		super.setVisible( bShow );
	}

	public void setDeltas( int deltaX, int deltaY ) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	public void setButtons( int iButtons ) {
		this.iButtons = iButtons;
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

	private void initGui() {
		setBounds(100, 100, 450, 300);
		setTitle( mTitle );
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JTextArea textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.setBounds(25, 11, 395, 218);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			textArea.setText( mMessage );
			contentPanel.add(textArea);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			if( iButtons == OK_ONLY || iButtons == BOTH_BUTTONS )
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						bOK = true;
						MessageDialog.this.setVisible(false);
					}
				});
			}
			if( iButtons == CANCEL_ONLY || iButtons == BOTH_BUTTONS )
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						bOK = false;
						MessageDialog.this.setVisible(false);
					}
				});
			}
		}
		center();
	}

	public static void showMessage( Window parent, String sTitle, String sMessage ) {
		showMessage( parent, sTitle, sMessage, BOTH_BUTTONS );
	}	

	public static void showMessage( Window parent, String sTitle, String sMessage, int iButtons ) {
		MessageDialog me = new MessageDialog( parent, sTitle, sMessage );
		me.setButtons(iButtons);
		me.setModal(true);
		me.setVisible(true);
		me.requestFocus();
	}

	public static void messageLater( Window parent, String sTitle, String sMessage ) {
		messageLater( parent, sTitle, sMessage, BOTH_BUTTONS );
	}	

	public static void messageLater( Window parent, String sTitle, String sMessage, int iButtons ) {
		final Window fParent = parent;
		final String fTitle = sTitle;
		final String fMessage = sMessage;
		final int fButtons = iButtons;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MessageDialog me = new MessageDialog( fParent, fTitle, fMessage );
				me.setButtons(fButtons);
				me.setModal(true);
				me.setVisible(true);
				me.requestFocus();
			}
		});
	}

	public static void messageWait( Window parent, String sTitle, String sMessage ) {
		messageLater( parent, sTitle, sMessage, BOTH_BUTTONS );
	}	

	public static void messageWait( JFrame parent, String sTitle, String sMessage, int iButtons ) {
		final Window fParent = parent;
		final String fTitle = sTitle;
		final String fMessage = sMessage;
		final int fButtons = iButtons;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					MessageDialog msg = new MessageDialog( fParent, fTitle, fMessage );
					msg.setButtons(fButtons);
					msg.setModal(true);
					msg.setVisible(true);
					msg.requestFocus();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}


}// End class MessageDialog
