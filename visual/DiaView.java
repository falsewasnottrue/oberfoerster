/*
 * Datei DiaView.java
 *
 * date 28.06.99
 * @author Daniel Jonietz
 */

package visual;

import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import basis.*;
import java.util.Vector;
import java.util.Iterator;
import java.util.Random;


/**
* Zeichnet 2 und 3achsige Diagramme, eine Achse ist idR die Zeitachse.
* Achtung: 
* Vom Nullpunkt aus wird *negativer* Platz benoetigt, 
* die Pfeilspitzen und die Teilstriche gehen von Null aus zurueck !
* Deswegen Rand von 10P rund um alles, also insgesamt 20P zusaetzlich in Laenge und Hoehe.
**/

public class DiaView extends Panel {

    /* Das offScreen - Bild */
    private Image offScreen;

    /* Modus: Zoom -> ScrollPane passt sich der Diagrammgroesse an
       Modus: Norm -> Diagrammgroesse passt sich der ScrollPanegroesse an */
    private static final int ZOOM = 0;
    private static final int NORM = 1;

    /* Der aktuelle Modus */
    private int modus = NORM;

    /* Anzahl Skalen */
    private int nrScales = 1;

    /* Nullpunkt und Skalenschnittpunkt */
    private int nullx = 0;
    private int nully = 0;

    /* Laenge der drei Skalen 
       ist in ScaleView gespeichert */

    /* aktuelle "Zeit", in Zeiteinheiten */
    private int time = 0;

    /* die drei Achsen 
       Standardmaessig x nach rechts, y nach oben und z nach unten gerichtet */
    private ScaleView xScale; 
    private ScaleView yScale;
    private ScaleView zScale;

    /* die Fuellfarben fuer die Diagramme */
    private Color y_Color = Color.red;
    private Color z_Color = Color.green;

    /* die zu zeichnenden Daten, werden aufbewahrt. */
    private Vector data = new Vector();
    private Vector datb = new Vector();

    /* Breite und Hoehe des zur Verfuegung stehenden Bereiches */
    private int width = 100;
    private int height = 100;

    /* setzt den Nullpunkt, x-Koordinate */
    public void X (int x) {
	nullx = x;
    }

    /* liest den Nullpunkt, x-Koordinate */
    public int getX() {
	return nullx;
    }

    /* s. X */
    public void Y (int y) {
	nully = y;
    }

    /* s. X */
    public int getY() {
	return nully;
    }

    /* Setzt die Farbe, mit der die auf der y-Achse aufgetragenen Daten gemalt werden */
    public void yColor (Color c) {
	y_Color = c;
    }

    /* Setzt die Farbe, mit der die auf der z-Achse aufgetragenen Daten gemalt werden */
    public void zColor (Color c) {
	z_Color = c;
    }

    /* gibt die momentane "Zeit" zurueck
       Zeit ist das, wieviele Daten schon vorher eingetragen wurden. 
       Das jetzige Datum ist das Zeit + erste. */
    public int getTime() {
	return time;
    }

    /* Laesst die Zeit verstreichen. */
    protected void nextTime() {
	time++;
    }

    /* Teilt mit, dass es *ein* neues Datum v gibt, das gezeichnet werden soll */
    public void newdata(int v) {
	this.nextTime();
	// Muss die Zeit-Achse neu skaliert werden ?
	xScale.Enlarge(this.getTime());
	data.addElement(new Integer(v));
	// Muss die Werte-Achse neu skaliert werden ?
	yScale.Enlarge(v);
	repaint();
    }

    /* Es gibt *zwei* neue Daten, (fuer dreiachsige Systeme), v und w */
    public void newdata(int v, int w) {
	this.nextTime();
	// Muss die Zeit-Achse neu skaliert werden ?
	xScale.Enlarge(this.getTime());
	data.addElement(new Integer(v));
	datb.addElement(new Integer(w));
	// Muss die Werte-Achse neu skaliert werden ?
	yScale.Enlarge(v);
	// Muss die zweite Werte-Achse neu skaliert werden ?
	zScale.Enlarge(w);
	repaint();
    }

    /** Konstruktor: 
     *
     * legt zweiachsiges Diagramm an
     * liest x, y-Koordinaten des Nullpunktes sowie die Laenge und Breite des Platzes 
     **/
    public DiaView (int x, int y, int width, int height) {
	nrScales = 2;
	this.width = width;
	this.height = height;
	this.X(x);
	this.Y(y);
	xScale = new ScaleView(nullx, nully, width);
	xScale.Align(ScaleView.HORIZONTAL);
	xScale.Direction(ScaleView.LEFT_TO_RIGHT);
	yScale = new ScaleView(nullx, nully, height);
	yScale.Align(ScaleView.VERTICAL);
	yScale.Direction(ScaleView.UPWARDS);
	addMouseListener (new MyMouseListener());
    }

    /** Konstruktor: 
     *
     * legt dreiachsiges Diagramm an
     * liest x, y-Koordinaten des Nullpunktes sowie die Laenge, Breite und Tiefe des Platzes 
     **/
    public DiaView (int x, int y, int width, int height, int deep) {
	nrScales = 3;
	this.width = width;
	this.height = height + deep;
	this.X(x);
	this.Y(y);
	xScale = new ScaleView(nullx, nully, width);
	xScale.Align(ScaleView.HORIZONTAL);
	xScale.Direction(ScaleView.LEFT_TO_RIGHT);
	yScale = new ScaleView(nullx, nully, height);
	yScale.Align(ScaleView.VERTICAL);
	yScale.Direction(ScaleView.UPWARDS);
	zScale = new ScaleView(nullx, nully, deep);
	zScale.Align(ScaleView.VERTICAL);
	zScale.Direction(ScaleView.DOWN);
	addMouseListener (new MyMouseListener());
    }

     /** Konstruktor: 
     *
     * legt ein (zwei Werte-Achsen und eine Zeitachse)-Diagramm an
     * liest nur die Breite und Hoehe des Platzes 
     * der Rest wird ueber doLayout geregelt.
     **/
    public DiaView (int width, int height) {
	nrScales = 3;
	xScale = new ScaleView(getX(), getY(), width-20);
	xScale.Align(ScaleView.HORIZONTAL);
	xScale.Direction(ScaleView.LEFT_TO_RIGHT);
	yScale = new ScaleView(getX(), getY(), (height-20)/2);
	yScale.Align(ScaleView.VERTICAL);
	yScale.Direction(ScaleView.UPWARDS);
	zScale = new ScaleView(getX(), getY(), (height-20)/2);
	zScale.Align(ScaleView.VERTICAL);
	zScale.Direction(ScaleView.DOWN);
	addMouseListener (new MyMouseListener());
    }

    /** Konstruktor: 
     *
     * legtein (zwei Werte-Achsen und eine Zeitachse)-Diagramm an
     * regelt alles selbst
     **/
    public DiaView() {
	this(100,100);
	offScreen = createImage (width,height);
    }
    /**
     * kuemmert sich um Teile der Initialisierung und Platzbedarf 
     **/
    public void doLayout() {
	System.out.println("doLayout!");
	
	// muss unterschieden werden nach modus
	// wieviel Platz wird mir zur Verfuegung gestellt ?
	if (modus == NORM) {
	    width=((ScrollPane)getParent()).getViewportSize().width;
	    height=((ScrollPane)getParent()).getViewportSize().height;
	    if (! (width > 0)) {
		width = 1;
	    }
	    if (!(height > 0)) {
		height = 1;
	    }
	}
	else {
	    if (modus == ZOOM) {
		width = xScale.getLength()+20;
		height = yScale.getLength()+zScale.getLength()+20;
	    }
	    else { // Wenn er hierhin kommt, ist irgendwas schief gelaufen ...
		System.out.println("ERROR: Unbekannter Modus in DiaView");
	    }
	}
	//Setze den Nullpunkt (Achsenschnittpunkt), X gibt den Abstand zum Rand an, 
	X(10);
	// richte die Achsen ein: Nullpunkt, Laenge.
	if (modus == NORM) {
	    Y((height-20)/2+10);
	    xScale.setDimension(nullx,nully,width-20);
	    yScale.setDimension(nullx,nully,(height-20)/2);
	    zScale.setDimension(nullx,nully,(height-20)/2);
	}
	else { // modus == ZOOM
	    Y(yScale.getLength()+10);
	    xScale.XY(nullx,nully);
	    yScale.XY(nullx,nully);
	    zScale.XY(nullx,nully);
	}
	//	validate();
	//	System.out.println("DooooooooL");

	System.out.println("breit : "+width+" hoch : "+height);
	offScreen = createImage (width,height);
	setSize(width,height);
	super.doLayout();
    }

    /**
     * malt die Diagramme
     **/ 
    public void paint (Graphics g) {
       	if (offScreen == null)
	    offScreen = createImage (width,height);
	Graphics goff = offScreen.getGraphics();
	goff.setColor (getBackground());
	goff.fillRect(0,0,width,height); // Loeschen ...
	// Daten eintragen
	goff.setColor(y_Color);
	int dx = xScale.Scale(2)-xScale.Scale(1); // Breite einer Saeule soll ja wohl konstant bleiben
	for (int i=0; i<data.size(); i++) {
	    int x = nullx + xScale.getDirection()*xScale.Scale(i);
	    int y = nully;
	    int xx = x;
	    int yy = nully + yScale.getDirection()*yScale.Scale(((Integer) data.elementAt(i)).intValue());
	    int dy = yScale.Scale(((Integer) data.elementAt(i)).intValue());
	    int t = xScale.Scale(xScale.getTick());
	    if (t == 1) {
		    goff.drawLine(x, y, xx, yy);
	    }
	    else {
		goff.fillRect(x,nully-dy,dx+1,dy);
	    }
	}
	if (nrScales == 3) {
	    goff.setColor(z_Color);
	    for (int i=0; i<data.size(); i++) {
		int x = nullx + xScale.getDirection()*xScale.Scale(i);
		int z = nully;
		int xx = x;
		int zz = nully + zScale.getDirection()*zScale.Scale(((Integer) datb.elementAt(i)).intValue()); 
		int t = xScale.Scale(xScale.getTick());
		if (t == 1) {
		    goff.drawLine(x, z, xx, zz);
		}
		else {
		    goff.fillRect(x,z,dx+1,zScale.getDirection()*zScale.Scale(((Integer) datb.elementAt(i)).intValue()));
		}
	    }
	    zScale.paint(goff);
	}
	// Achsen malen
	xScale.paint(goff);
	yScale.paint(goff);
	g.drawImage(offScreen,0,0,this);
    }


    /**
     * Wieviel Platz haette ich gerne ?
     **/
    public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    /**
     * Wieviel Platz brauche ich mindestens ?
     **/
    public Dimension getMinimumSize() {
	return new Dimension(width, height);
    }


    /* Stellt fest, welche der Skalen geaendert werden soll und aendert diese */
    protected void rescale(int x, int y, double factor) {
	System.out.println("rescale!");
	
	if (Math.abs(x-getX())<5) {// -> Hochachse, entweder y- oder z-Achse !
	    if (y < getY() ) {
		yScale.Length((int)(factor*yScale.getLength()));
		height = zScale.getLength()+yScale.getLength();
		modus = ZOOM;
	    }
	    else {
		if (y > getY() ) {
		    zScale.Length((int)(factor*zScale.getLength()));
		    height = zScale.getLength()+yScale.getLength();
		    modus = ZOOM;
		}
	    }
	}
	else // also nicht auf der Hochachse...
	    if (Math.abs(y-getY())<5) { // -> tatsaechlich auf der x-Achse !
		xScale.Length((int)(factor*xScale.getLength()));
		width = xScale.Scale(width);
		modus = ZOOM;
	
	    }
	// offScreen mit neuer Groesse anlegen
	offScreen = createImage (width,height);
	doLayout();
     	if (getParent()!=null) {
	    System.out.println("parent.validate!");
	    
	    getParent().validate();
	    getParent().repaint();
	}
	repaint();
 
    }

    /**
     * MyMouseListener
     * 
     *kuemmert sich um Mausereignisse, erlaubt es, die Achsen zu Skalieren.
     **/
    class MyMouseListener extends MouseAdapter implements MouseListener {
	public void mouseClicked(MouseEvent e) {
	    if (e.getModifiers()==Event.META_MASK) {
		rescale(e.getX(), e.getY(), (double) 0.7);
	    }
	    else {
		rescale(e.getX(), e.getY(), (double) 1.4);
	    }
 

	}
    }

    /**
     * Main zum Testen
     **/
    public static void main (String[] args) {
  
	// DiaView first = new DiaView(50,250,200,100,300);  
	DiaView first = new DiaView(200,200);
	ScrollPane zp = new ScrollPane();
	Panel p = new Panel();
	zp.add(first);
	Frame f = new Frame();
	f.setSize(200, 300);
	p.add(zp);
	p.setLayout(new GridLayout(1,1));
	f.add(p);
	first.doLayout();
	f.setVisible(true);
 
	Random r = new Random();
	for (int i=0; i<300; i++){
	    first.newdata(r.nextInt(300), r.nextInt(50));
	}
    }

}
