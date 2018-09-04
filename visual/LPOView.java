
/*
 * Datei: visual/LPOView.java
 */

/**
 **/

package visual;

import java.awt.*;

public class LPOView extends EventView {

    TreeView lpo = new TreeView(new TreeViewNode ("Huch!"));
  

    public LPOView() {
	initialize();
    }

    public void initialize() {
	setLayout(new GridLayout(1,1));
 	add (lpo);
    }

    public boolean visualizeEvent( VisualEvent e ) {

	if (e.getID()== VisualEvent.ORD_TEST) {
	    remove(lpo);
	    lpo = ((TreeView)e.getParams().get(0));
	    add(lpo);
	    lpo.repaint();
	    repaint();  
	    validate();
	};
	return true;
    }
}
