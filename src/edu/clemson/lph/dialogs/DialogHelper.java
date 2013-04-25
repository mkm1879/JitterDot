package edu.clemson.lph.dialogs;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class DialogHelper {

	
	  public static void center( Window window ) {
		  center( window, 0, 0 );
	  }
		 
	  public static void center( Window window, int deltaX, int deltaY ) {
		    //Center the window
			boolean bSmall = false;
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    Dimension frameSize = window.getSize();
		    if( frameSize.height > screenSize.height ) {
		    	frameSize.height = screenSize.height;
		    	bSmall = true;
		    }
		    if( frameSize.width > screenSize.width ) {
		    	frameSize.width = screenSize.width;
		    	bSmall = true;
		    }
		    if( bSmall ) {
		    	window.setLocation( (screenSize.width - frameSize.width) / 2, 0);
		    }
		    else {
		    	window.setLocation( deltaX + (screenSize.width - frameSize.width) / 2,
		    			deltaY + (screenSize.height - frameSize.height) / 2);
		    }
		  }

}
