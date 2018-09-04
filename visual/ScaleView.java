/* Datei ScaleView.java
 *
 * date 28.06.99
 *@author Daniel Jonietz
 */

package visual;

import java.applet.*;
import java.awt.*;
import java.awt.Graphics.*;
import java.awt.geom.*;
import basis.*;

/**
* Zeigt Skalen an und sorgt fuer ihre richtige Unterteilung.
* Es gilt : Skalierungsfaktor = (maximale Elementezahl) durch Skalenlaenge.
**/
public class ScaleView extends Component {

    /* Konstanten fuer die Lage */
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 0;

    /* Konstanten fuer die Richtung */
    public static final int LEFT_TO_RIGHT = 1;
    public static final int RIGHT_TO_LEFT = -1;
    public static final int UPWARDS = -1;
    public static final int DOWN = 1;

    /* Nullpunkt */
    private int nullx = 0;
    private int nully = 0;

    /* Laenge der Skala */
    private int length = 0;

    /* Ausrichtung vertikal, horizontal */
    private int align = HORIZONTAL;
    private int direction = LEFT_TO_RIGHT;

    /* tickfactor = Einheiten pro Teilstrich */
    private int tickfactor = 10;

    /* Skalierungsfaktor */
    private double scaler = 1;

    /* Maximal in dieser Skalierung darstellbar */
    private int max = length;

    /* Die groesten und kleinsten auftretenden Elemente werden gemerkt */
    private int maximum = 0;
    private int minimum = 10000; // !!! //

    /* Setzt die Lage */
    public void Align (int al) {
	align = al;
    }

    /* Liest die Lage */
    public int getAlign() {
	return align;
    }

    /* Setzt die Richtung, d.h. ist die Zunahme von links nach rechts oder von rechts nach links,
       von oben nach unten oder von unten nach oben */
    public void Direction (int dir) {
	direction = dir;
    }

    /* Gibt die Ausrichtung zurueck */
    public int getDirection() {
	return direction;
    }

    /* Setzt die x-Koordinate des  Nullpunktes der Skala */
    public void X (int x) {
	nullx = x;
    }

    /* Liest die x-Koordinate des  Nullpunktes der Skala */
    public int getX () {
	return nullx;
    }

    /* Setzt die y-Koordinate des  Nullpunktes der Skala */
    public void Y (int y) {
	nully = y;
    }

    /* Liest die x-Koordinate des  Nullpunktes der Skala */
    public int getY () {
	return nully;
    }

    /* Setzt die Laenge einer Skala */
    // Wird die Laenge einer Achse geaendert, so muessen einige Dinge angepasst werden.
    public void Length (int l) {
	scaler *= (double)length / (double)l; 
	length = l;
	resizeTicks();
    }

    /* Aendert Nullpunkt und Laenge der Skala */
    public void setDimension(int x, int y, int l) {
	X(x);
	Y(y);
	Length(l);
    }
    
    public void XY(int x, int y) {
	X(x);
	Y(y);
    }
    
    /* Sorgt dafuer, dass die Abstande der Teilstriche immer einigermassen sinnvoll sind.*/
    private void resizeTicks() {
	// Hier entstand das Problem einer Endlosschleife, dadurch dass tickfactor unbeabsichtigterweise
	// ziemlich schnell die int Grenzen verlassen hat bzw. auf 1 gesetzt wurde, 
	// was so nicht gewollt war. Abhilfe sollte die Eingrenzung schaffen.
	if ((Scale(tickfactor) < 10) && (tickfactor < 4096)) {
	    tickfactor *= 2;
 
	}
	else 
	    if ((Scale(tickfactor) > 50) && (tickfactor >= 2)) {
		tickfactor /= 2;
	    }
    }

    /* Gibt die Laenge der Skala zurueck */ 
    public int getLength () {
	return length;
    }

    /* Gibt die Groesse des in dieser Skalierung groessten darstellbaren Datenelementes zurueck */
    public int getMax () {
	return max;
    }

    /* Gibt desn Skalierungsfaktor zurueck */
    public double getScale() {
	return scaler; 
    }

    /* Setzt den Faktor, mit dem die Teilstriche skaliert werden */
    public void Tickfactor(int t) {
	tickfactor = t;  
    }

    /* Gibt den Faktor zurueck, mit dem die Teilstriche skaliert werden */
    public int getTick() {
	return tickfactor;
    } 

    /* Merkt sich Maximum und Minimum 
       wird im Moment nicht unbedingt gebraucht */
    /*   private void marker(int z) {
	if (z < minimum) {minimum=z;}
	if (z > maximum) {maximum=z;}
    }
    */

    /* Wendet eine Skalierungsvorschrift auf x an 
       ist z.B. der Wert x=400, scaler = 2, so wird 
       200 (Pixel) zurueckgegeben. */
    public int Scale(int x) {
	return (int)((double) x / scaler);
    }

    /* Wird eine Zahl von darzustellenden Elementen count gegeben, 
       so wird die Skala angepasst, fallse noetig.*/
    public void Enlarge(int count) {
	//marker(count);
	if (count > max) {
	    if (scaler <= 1.8) {
		scaler *= 1.5;
	    }
	    else {
		scaler = ((double)count / (double)(length));
	    }
	}
	max = (int)(scaler*length);
	resizeTicks();
    }


    /**
     * Konstruktor. Parameter: Nullpunktskoordinaten, Laenge der Skala.
     **/
    public ScaleView(int x, int y, int l) {
	this.X(x);
	this.Y(y);
	length=l;
	max=l;
    }

    /**
     * malt die Skala, ihre Spitzen und Teilstriche.
     **/
    public void paintScale(Graphics g) {
	g.setColor(Color.black);
	int tick = Scale(tickfactor);
	// Liegt die Achse ?
	if (align==HORIZONTAL) {
	    // Achse
	    g.drawLine(nullx, nully, nullx + direction*(length + 10), nully);
	    // Pfeilspitzen
	    g.drawLine(nullx + direction*(length + 10), nully, nullx + direction*(length + 5), nully +3);
	    g.drawLine(nullx + direction*(length + 10), nully, nullx + direction*(length + 5), nully -3);
	    // Teilstriche
	    for (int i=0; i*tick < length; i++) {
		//System.err.println("i"+i);
		//System.err.println("t"+tick); //Problem: tick=0 !!
		//System.err.println("it"+i*tick);
		//System.err.flush();
		g.drawLine(nullx + direction*i*tick, nully-5, nullx + direction*i*tick, nully + 5);
	    }
	    // Beschriftung 
	    String str = new String().valueOf(max);
	    g.drawString(str, nullx + direction*length-(g.getFontMetrics().stringWidth(str)), nully-10);
	}
	// oder steht sie ?
	else {//vertical
	    // Achse
	    g.drawLine(nullx, nully, nullx, nully + direction*(length + 10));
	    // Pfeilspitzen
	    g.drawLine(nullx, nully+direction*(length+10), nullx - 3, nully + direction*(length + 5));
	    g.drawLine(nullx, nully+direction*(length+10), nullx + 3, nully + direction*(length + 5));
	    // Teilstriche
	    for (int i=0; i*tick <= length; i++) {
		g.drawLine(nullx - 5, nully + direction*i*tick, nullx + 5, nully + direction*i*tick );
	    }
	    // Beschriftung 
	    String str = new String().valueOf(max);
	    g.drawString(str, nullx+10, nully+direction*(length-10));
	}
  
    }
    /**
     * malt alles
     **/
    public void paint(Graphics g) {
	paintScale(g);
    }

}
