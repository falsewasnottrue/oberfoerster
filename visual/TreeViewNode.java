
/*
 * Datei: TreeViewNode.java
 */

/**
 * kapselt einen einzelnen Knoten im Auswahlbaum
 * @author Rasmus Hofmann
 **/

package visual;

import java.awt.*;
import java.util.Vector;

public class TreeViewNode {

    private String symbolWhenExpanded;
    private String symbolWhenCollapsed;
    private String symbolWhenLeave;

    private String text;

    private TreeView tribe;
    private Path path;
    private Vector sons;

    private boolean expanded = true;

    
    // ein paar Konstanten für die griechischen Buchstaben
    public static String alpha = new String("a");
    public static String beta = new String("ß");
    public static String gamma = new String("c");
    public static String delta = new String("d");

    /**
     * erzeugt neues TreeViewNode-Objekt
     *
     * @param symbExp das Symbol, falls der Knoten expandiert ist
     * @param symbColl das Symbol, falls der Knoten zusammengefaltet ist
     * @param symbLeave das Symbol, falls der Knoten ein Blatt ist
     * @param t der Text den das Objekt anzeigen soll
     **/
    public TreeViewNode(String symbExp, String symbColl, String symbLeave, String t) {
	symbolWhenExpanded = symbExp;
	symbolWhenCollapsed = symbColl;
	symbolWhenLeave = symbLeave;
	text = t;
	sons = new Vector();
	path = new Path();
    }

    /**
     * erzeugt neues TreeViewNode-Objekt
     *
     * @param symbExp das Symbol, falls der Knoten expandiert ist
     * @param symbColl das Symbol, falls der Knoten zusammengefaltet ist
     * @param symbLeave das Symbol, falls der Knoten ein Blatt ist
     * @param t der Text den das Objekt anzeigen soll
     * @param tv der TreeView zu dem der Knoten gehoert
     **/
    public TreeViewNode(String symbExp, String symbColl, String symbLeave, String t, TreeView tv) {
	this(symbExp, symbColl, symbLeave, t);
	tribe = tv;
    }

    /**
     * erzeugt neues TreeViewNode-Objekt
     *
     * @param s das Symbol das in den Klammer steht
     * @param t der Text, den das Objekt anzeigen soll
     **/
    public TreeViewNode(String s, String t) {
	this(s,s,s,t);
    }

   /**
    * erzeugt neues TreeViewNode-Objekt
    *
    * @param s das Symbol das in den Klammer steht
    * @param t der Text, den das Objekt anzeigen soll
    * @param tv der TreeView zu dem der Knoten gehört
    **/
    public TreeViewNode(String s, String t, TreeView tv) {
	this(s,s,s,t,tv);
    }

    /**
     * erzeugt neues TreeViewNode-Objekt
     * Symbol ist dann + bzw. -, wie man das halt so gewohnt ist
     *
     * @param t der Text, den das Objekt anzeigen soll
     **/
    public TreeViewNode(String t) {
	this("-", "+", "  ", t);
    }

    /**
     * erzeugt neues TreeViewNode-Objekt
     * Symbol ist dann + bzw. -, wie man das halt so gewohnt ist
     *
     * @param t der Text, den das Objekt anzeigen soll
     * @param tv der TreeView zu dem der Knoten gehört
     **/
    public TreeViewNode(String t, TreeView tv) {
	this(t);
	tribe = tv;
    }

    /**
     * nimmt den übergebenen TreeViewNode an Sohnes Statt an
     *
     * @param son der verlorene Sohn
     * @return den Pfad den der Sohn erhält
     **/
    public Path add(TreeViewNode son) {
	sons.add(son);
	son.setTribe(tribe);
	Path sonsPath = path.addPath(sons.size()-1);
	son.setPath(sonsPath);
	// Falls son ein ganzer Unterbaum ist, so muß fuer
	// alle Unterknoten der Pfad aktualisiert werden:
	if (son.hasSons()) son.updatePath();

	return sonsPath;
    }

    /**
     * aktualisiert den Pfad im übergebenen Unterbaum.
     * Diese Methode wird benoetigt, falls ein ganzer Unterbaum in einen
     * anderen Baum eingehaengt wird. In diesem Fall aendern sich ja die
     * Pfade von der Wurzel zu diesen neu eingefuegten Blaettern. Die
     * Methode add ruft dann automatisch updatePath() auf, um die Pfade
     * in den Knoten auf den aktuellen Stand zu bringen.
     **/
    public void updatePath() {
	TreeViewNode currNode;

	for (int i=0; i<sons.size(); i++) {
	    currNode = (TreeViewNode)sons.elementAt(i);

	    // Den Pfad zum aktuellen Knoten aktualisieren:
	    currNode.setPath( path.addPath(i) );
	    // Falls dieser Unterknoten hat, dann auch deren Pfad aktualisieren:
	    if ( currNode.hasSons() ) currNode.updatePath();
	}
    }

    /**
     * gibt den Namen des i.Sohnes preis
     *
     * @param i der wievielte Sohn wird gesucht
     * @return Referenz auf den i.Sohn, wenn's einen gibt, sonst null
     **/
    public TreeViewNode getSon(int i) {
	if ( i < sons.size() )
	    return (TreeViewNode)sons.elementAt(i);
	else
	    return null;
    }

    /**
     * expandiert den Knoten und zeichnet den gesamten Baum neu
     **/
    public void expand() {
	expanded = true;
	tribe.fill();
    }

    /**
     * klappt den Unterbaum dieses Knotens ein und zeichnet den gesamten Baum neu
     **/
    public void collapse() {
	expanded = false;
	tribe.fill();
    }

    /**
     * kehrt den Expandiertseinsstatus des Knotens um, soll heissen:
     * wenn der Knoten expandiert war, dann ist er anschliessend kollabiert,
     * war der Knoten hingegen nicht expandiert, dann ist er danach, na?
     * Richtig: expandiert!
     **/
    public void flip() {
	if ( isExpanded() )
	    collapse();
	else
	    expand();
    }

    /**
     * setzt die Stammeszugehörigkeit fest
     *
     * @param _tribe Referenz auf den TreeView zu dem der Knoten gehört
     **/
    public void setTribe(TreeView _tribe) {
	tribe = _tribe;
    }

    /**
     * macht dem TreeViewNode seinen eigenen Pfad bekannt
     * dadurch wird die Verwaltung der Pfade insgesamt leichter
     * 
     * @param _path der Pfad
     **/
    public void setPath(Path _path) {
	path = _path;
    }

    /**
     * gibt true zurück, falls der Unterbaum dieses Knotens expandiert ist
     **/
    public boolean isExpanded() {
	return expanded;
    }

    /**
     * gibt true zurück, falls der Unterbaum dieses Knotens zusammengefaltet ist
     **/
    public boolean isCollapsed() {
	return !expanded;
    }

    /**
     * gibt true zurueck, falls der Knoten noch Unterknoten hat
     **/
    public boolean hasSons() {
	return !sons.isEmpty();
    }

    /**
     * schreibt sich und seine Nachfahren (insofern sie ausgeklappt und mithin
     * sichtbar sind) in das Listobjekt das dem Stamm gehoert.
     **/
    public void fill (Vector items) {

	int indent = 4; // gibt an, wieviel eingerueckt werden soll

	// Raffiniert: Welches Symbol soll angezeigt werden, abhaengig vom Status des Knotens:
	String symbol = ( hasSons() ? ( isExpanded() ? symbolWhenExpanded : symbolWhenCollapsed ) : symbolWhenLeave);

	// Leerstring erzeugen, abhaengig davon wie weit der Knoten eingerueckt sein soll
	StringBuffer sb = new StringBuffer((path.length()-1) * indent);
	for (int i=1; i<path.length()*indent; i++) sb.append(" "); 
	
	tribe.add(sb+"["+symbol+"]  "+text); // schreibt den Knoten in das List-Objekt
	items.add(this); // haengt eine Referenz von sich in den Vector der angezeigten Knoten

	// Und jetzt rekursiv auch noch die Soehne in die Liste schreiben:
	if ( isExpanded() )
	    for (int i=0; i<sons.size(); i++)
		((TreeViewNode)(sons.elementAt(i))).fill(items);
    }

    /**
     * hängt eine Stringrepräsentation von sich selbst an sb an.
     *
     * @param sb darin schreibt er sich
     **/
    public void addToStringBuffer(StringBuffer sb) {
	
	path.addToStringBuffer(sb);
	sb.append(" "+symbolWhenExpanded+" "+text+'\n');

	for (int i=0; i<sons.size(); i++)
	    ((TreeViewNode)(sons.elementAt(i))).addToStringBuffer(sb);
    }

    /**
     * gibt sich in Stringform zurück
     **/
    public String toString() {
	StringBuffer sb = new StringBuffer();
	addToStringBuffer(sb);
	return sb.toString();
    }

    // Hab ich gebraucht, um das in die InfoZeile zu schreiben (AK)
    public String getText() {
	return text;
    }
}
