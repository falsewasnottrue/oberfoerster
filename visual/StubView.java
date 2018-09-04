
/*
 * Datei: visual/StubView.java
 */

/**
 **/

package visual;

import java.awt.*;

public class StubView extends EventView {

    private Label label;

    public StubView() {
	initialize();
    }

    public void initialize() {
	setLayout( new GridLayout(1,1) );
	label = new Label();
	add( label );
    }

    public boolean visualizeEvent( VisualEvent e ) {
	label.setText( e.getName() );
	return true;
    }
}
