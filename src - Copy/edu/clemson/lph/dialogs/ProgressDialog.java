package edu.clemson.lph.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ProgressDialog extends JDialog {
	  private JPanel panel1 = new JPanel();
	  private JProgressBar jProgressBar1 = new JProgressBar();
	  private JLabel lProgress = new JLabel();
	  private int iValue;
	  private int iMax = 9;
	  private boolean bAuto = false;
	  private boolean bCancel = false;
	  private String sMsg = "Working ...";
	  private String sCurrentTask = "";
	  private JLabel lMsg = new JLabel();
	  private JLabel lblCurrentTask;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProgressDialog dialog = new ProgressDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public ProgressDialog() {
		try {
			setBounds(100, 100, 450, 300);
			initGUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ProgressDialog( Window parent, String sTitle, String sMsg ) {
		super( parent );
		this.setTitle(sTitle);
		this.sMsg = sMsg;
		try {
			setBounds(100, 100, 450, 300);
			initGUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Window getWindowParent() {
		Window wRet = null;
		Container cParent = getParent();
		while( cParent != null && !(cParent instanceof Window ) ) {
			cParent = cParent.getParent();
		}
		wRet = (Window)cParent;
		return wRet;
	}

	  public void setAuto( boolean bAuto ) {
	    this.bAuto = bAuto;
	    if( bAuto ) {
	      Thread t = new Thread( new Runnable() {
	        public void run() {
	          while( !bCancel ) {
	            synchronized (this) {
	              try {
	                wait(1000);
	                SwingUtilities.invokeLater(new Runnable() {
	                  public void run() {
	                    step();
	                  }
	                });
	              }
	              catch (InterruptedException ex) {
	              }
	            }
	          }
	        }
	      } );
	      t.start();
	    }
	  }
	  
	  public void cancel() {
		  bCancel = true;
	  }

	  public void setValue( int iValue ) {
		  this.iValue = ( iValue + 1 ) % ( iMax + 1 );
		  SwingUtilities.invokeLater( new Runnable() {
			  @Override
			  public void run() {
				  jProgressBar1.setValue( ProgressDialog.this.iValue );
			  }
		  });
	  }

	  public void step() {
		  this.iValue = ( iValue + 1 ) % ( iMax + 1 );
		  SwingUtilities.invokeLater( new Runnable() {
			  @Override
			  public void run() {
				  jProgressBar1.setValue( ProgressDialog.this.iValue );
			  }
		  });
	  }

	  public void setVisible( boolean bVis ) {
	    if( bAuto )
	      setValue( iMax / 2 );
	    pack();
	    if( bVis ) bCancel = false;
	    else bCancel = true;
	    super.setVisible(bVis);
	  }
	  
	  public void setCurrentTask( String sCurrentTask ) {
		  this.sCurrentTask = sCurrentTask;
		  SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				lblCurrentTask.setText(ProgressDialog.this.sCurrentTask);
			}
		  });
	  }

	  /**
	   * Todo rework as WindowBuilder dialog
	   * @throws Exception
	   */
	  private void initGUI() throws Exception {
	    lProgress.setBounds(219, 56, 54, 14);
	    lProgress.setText("Progress...");
	    jProgressBar1.setBounds(59, 54, 150, 16);
	    jProgressBar1.setMaximum(iMax);
	    jProgressBar1.setMinimum(0);
	    lMsg.setBounds(10, 22, 296, 19);
	    lMsg.setFont(new java.awt.Font("Dialog", 1, 14));
	    lMsg.setText(sMsg);
	    panel1.setPreferredSize(new Dimension(350, 150));
	    getContentPane().add(panel1);
	    panel1.setLayout(null);
	    panel1.add(lMsg);
	    panel1.add(jProgressBar1);
	    panel1.add(lProgress);
	    
	    lblCurrentTask = new JLabel("");
	    lblCurrentTask.setHorizontalAlignment(SwingConstants.CENTER);
	    lblCurrentTask.setBounds(10, 81, 304, 14);
	    panel1.add(lblCurrentTask);
	    this.setSize(new Dimension(332, 150));
	  }
}
