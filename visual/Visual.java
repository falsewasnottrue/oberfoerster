package visual;

import basis.Oberfoerster;

import java.awt.*;

/**
 * HauptView zur Visualisierung.
 * Dieses Objekt ist als Singleton implementiert.
 * d.h. es gibt genau eine Instanz der Klasse Visual
 * welche Instanz das ist, wird als Klassenvariable gespeichert.
 * durch diesen Trick ist Visual von ueberall aus sichtbar. 
 * Und die Visualisierung kann ueber Visual.notify (Event) angestossen werden.
 **/

public class Visual extends Panel
{
    /**
     * Die einzige moegliche Instanz der Klasse Visual
     **/
    private static Visual myInstance;
    
    /**
     * Das z.Zt. aktive View zum Visualisieren der Events
     **/
    private EventView currentView;
    /**
     * Der Baum, der die komplette Eventhierachie anzeigt
     **/
    public HierarchyTree hierarchyTree;
    /**
     * Die Buttonleiste. mit der der Benutzer die Kontrolle uebernimmt
     **/
    public ControlPanel controlPanel;

    private cLoopView cloopView;

    private int currentPriority;

    private static int globWidth = 800;
    private static int globHeigth = 600;

    public int getCurrentPriority()
    {
	return currentPriority;
    }

    public void setCurrentPriority(int anInt)
    {
	currentPriority = anInt;
    }

    public EventView getCurrentView()
    {
	return currentView;
    }

    /**
     * liefert Referenz auf den Hierarchiebaum zurueck
     * 
     * @returns Referenz auf den Hierarchiebaum
     **/
    public HierarchyTree getHierarchyTree() {
	return hierarchyTree;
    }

    /**
     * liefert Referenz auf das ControlPanel zurueck
     *
     * @returns Referenz auf das ControlPanel
     **/
    public ControlPanel getControlPanel() {
	return controlPanel;
    }

    /**
     * Schnittstelle zur Instanz.
     **/
    public static void notify(VisualEvent event)
    {
	if (myInstance != null)
	    myInstance.notifyMe(event);
    }
    

    /**
     * Faengt alle Events, die vom Basissystem kommen ab, und
     * leitet sie bei Bedarf an die entsprechenden Views weiter.
     **/
    public void notifyMe(VisualEvent event)
    {
	boolean viewing = false;
	boolean correctEvent = false;
	// alle cLoop Events zum Aufzeichnen der Statistik an das Diagramm senden
	if (event.getPriorityLevel() == 1)
	    cloopView.visualizeEvent(event);
	
		
	if ( hierarchyTree.getRequestedID() == -1){
	    // Nur Events anzeigen, die auf einer Stufe der Hierachie stehen
	    correctEvent = (event.getPriorityLevel() == getCurrentPriority());
	    
	    // Wenn die Kontrolle nicht gerade umschalted (step-into oder step-out)
	    // dann auch nur solche Events anzeigen, die sich in der aktuellen
	    // Schleife befinden, sprich die denselben View haben
	    if (!controlPanel.isSwitching()){
		correctEvent = correctEvent && 
		    ( currentView.getClass().getName().equals("visual." + event.getView()));
	    }
	} else {
	    // Im HierachyTree wurde auf ein Event geklickt.
	    // Jetzt warte ich auf das naechste Event mit passender ID
	    correctEvent = ( hierarchyTree.getRequestedID() == event.getID() );
	}


	// Wenn das Event die Filter passiert hat, dann anzeigen
	if (correctEvent){

	    //	    System.out.println(event.debugString());
	    // Ausgabe auf dem ControlPanel, welches Event gerade gezeigt wird:
	    controlPanel.setInfo(event.toString());
	    currentPriority = event.getPriorityLevel();

	    // Selektierung im Hierarchybaum:
	    hierarchyTree.selectEvent(event);

	    // Falls anderer View gewuenscht wird, View wechseln
	    selectView (event.getView());
	    // und Event an den View zur Visualisierung schicken
	    viewing = currentView.visualizeEvent(event);
	    currentView.repaint();
	    // das Event wurde akzeptiert, Kontrolle benachrichtigen,
	    // dass das Umschalten erfolgreich war
	    controlPanel.stopSwitching();

	    // und solange warten, bis die Kontrolle ein weitermachen
	    // erlaubt
	    // Manche Events werden nur gesammelt und nicht angezeigt,
	    // dann laeuft der Beweiser auch weiter
	    while ( (!controlPanel.canGo() && viewing && (hierarchyTree.getRequestedID() == -1) )  );
	    // System.out.println("Warte auf naechstes Event mit Prioritaet " + currentPriority);  
	    // Naechster Schritt beginnt
	    controlPanel.reset();
 	};
    }

    /**
     * Falls ein anderer View gewuenscht wird, als der uebergebene
     * String, currentView austauschen
     **/
    public void selectView(String aViewClassName)
    {
	String className = "visual."+aViewClassName;
	if (!currentView.getClass().getName().equals(className)){
	    // neues View Objekt aus KlassenNamen generieren
	    try {
		GridBagConstraints gbc = ((GridBagLayout)getLayout()).getConstraints(currentView);
		remove(currentView);
		if (currentPriority == 1)
		    // immer denselben Statistik View verwenden
		    currentView = cloopView;
		else
		    // alle anderen Views immer neu aufbauen
		    currentView = (EventView)Class.forName(className).newInstance();
		add(currentView,1);
		((GridBagLayout)getLayout()).setConstraints(currentView,gbc);
		currentView.validate();
		currentView.repaint();
		validate();
		repaint();
	    } 	
	    catch (Exception e){
		System.err.println ("Exception: " + e);
		e.printStackTrace();
	    };
	}
	repaint();
    }


    public void initialize()
    {
	// Objekte initialisieren:
	currentView = new EventView();
	cloopView = new cLoopView();
	hierarchyTree = new HierarchyTree(this);
	controlPanel = new ControlPanel(this);

	// Ausgaben auf dem Bildschirm anordnen:
	GridBagLayout gridBag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;

	c.gridx = 0; c.gridy = 0;
	c.weightx = 0.4; c.weighty = 1.0;
	c.gridheight = GridBagConstraints.REMAINDER;
	gridBag.setConstraints(hierarchyTree, c);
        add(hierarchyTree);
  
	c.gridx = 1; c.gridy = 0;
	c.weightx = 1.0; c.weighty = 0.9;
	c.gridheight = GridBagConstraints.RELATIVE;
	gridBag.setConstraints(currentView, c);
        add(currentView);

	c.gridx = 1; c.gridy = 1;
	c.weightx = 1.0; c.weighty = 0.0;
	gridBag.setConstraints(controlPanel, c);
	add(controlPanel);

	setLayout(gridBag);

	// diverse Einstellungen
	validate();
	setCurrentPriority(3);
    }
 
    public static void main(String[] args)
    {
	myInstance = new Visual();
	myInstance.initialize();
	myInstance.setCurrentPriority( new Integer(args[1]).intValue() );
	Frame fram = new Frame ();
	fram.setSize(Visual.globWidth, Visual.globHeigth);
	fram.add(myInstance);
	fram.setVisible(true);
	Oberfoerster.parseSpec(args[0]);
	Oberfoerster.initialize();
	Oberfoerster.run();
    }
    
  
}
