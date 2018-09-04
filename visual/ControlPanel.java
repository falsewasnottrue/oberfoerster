
/*
 * Datei: ControlPanel.java
 */

/**
 * Die Buttonleiste, über die man steuern kann, was auf dem View so geschieht
 * Beta-Release 1.2
 *
 * author Rasmus Hofmann
 **/

package visual;

import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends Panel implements ActionListener {

    public static String stepOver = new String("Step over");
    public static String stepInto = new String("Step into");
    public static String stepOut = new String("Step out");
    public static String play = new String("Play");
    public static String stop = new String("Stop");
    public static String exit = new String("Exit");

    private boolean isRunning = false;
    private boolean step = false;
    /**
     * Initialisierung auf true ist wichtig, sonst laeuft der Visualisierer 
     * gar nicht an
     **/
    private boolean switching = true;
    
    /**
     * Referenz auf das Visualisierungsobjekt
     **/
    public Visual visual;

    private Label label;
    /**
     * erzeugt ein neues ControlPanel
     **/
    public ControlPanel(Visual _visual) {
	visual = _visual;

	Container cont = new Container();
	cont.setLayout(new GridLayout(1,6));

	Button buttonStepOver = new Button();
	buttonStepOver.setLabel(ControlPanel.stepOver);
	buttonStepOver.addActionListener( this );
	cont.add(buttonStepOver);

	Button buttonStepInto = new Button();
	buttonStepInto.setLabel(ControlPanel.stepInto);
	buttonStepInto.addActionListener( this );
	cont.add(buttonStepInto);

	Button buttonStepOut = new Button();
	buttonStepOut.setLabel(ControlPanel.stepOut);
	buttonStepOut.addActionListener( this );
	cont.add(buttonStepOut);

	Button buttonPlay = new Button();
	buttonPlay.setLabel(ControlPanel.play);
	buttonPlay.addActionListener( this );
	cont.add(buttonPlay);

	Button buttonStop = new Button();
	buttonStop.setLabel(ControlPanel.stop);
	buttonStop.addActionListener( this );
	cont.add(buttonStop);

	Button buttonExit = new Button();
	buttonExit.setLabel(ControlPanel.exit);
	buttonExit.addActionListener( this );
	cont.add(buttonExit);

        label = new Label();
	label.setText("Ich bin ein Label!");

	setLayout(new GridLayout(2,1));
	add(cont);
	add(label);
    }

    public void setInfo(String info) {
	label.setText(info);
    }

    public void actionPerformed( ActionEvent e ) {
	// Step Over:
	if ( e.getActionCommand().equals( ControlPanel.stepOver )) {
	    step = true;
	}

	// Step Into:
	if ( e.getActionCommand().equals( ControlPanel.stepInto )) {
	    if ( visual.getHierarchyTree().canStepIn() ) {
		int currPrio = visual.getCurrentPriority();
		visual.setCurrentPriority( (currPrio < 6) ? currPrio+1 : 6 );
		step = true;
		switching = true;
	    }
	    else
		setInfo("Ich kann hier nicht runtersteigen, Einstein!");
	}

	// Step Out
	if ( e.getActionCommand().equals( ControlPanel.stepOut )) {
	    if ( visual.getHierarchyTree().canStepOut() ) {
		int currPrio = visual.getCurrentPriority();
		visual.setCurrentPriority( (currPrio > 1) ? currPrio-1 : 1 );
		step = true;
		switching = true;
	    }
	    else
		setInfo("Hier kann ich doch gar nicht rausgehen, du Hirni!");
	}

	// Play
	if ( e.getActionCommand().equals( ControlPanel.play )) {
	    isRunning = true;
	}

	// Stop
	if ( e.getActionCommand().equals( ControlPanel.stop )) {
	    isRunning = false;
	}

	// Exit
	if ( e.getActionCommand().equals( ControlPanel.exit )) {
	    System.exit(0);
	}
    }

    public void reset()
    {
	step = false;
    }

    public boolean canGo() 
    {
	boolean go = isRunning || step;
	return go;
    }	

    public boolean isSwitching()
    {
	return switching;
    }

    public void stopSwitching()
    {
	switching = false; 
    }

}
