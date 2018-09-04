
/*
 * Datei: TreeView.java
 */

/**
 * kapselt einen gesamten Auswahlbaum inklusive Ausgaberoutinen
 * @author Rasmus Hofmann
 **/

package visual;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class TreeView extends List {

    // Referenz auf den Wurzelknoten
    private TreeViewNode root;

    // In diesem Vector sind die Knoten gespeichert, die in der Liste angezeigt werden
    protected Vector items;

    /**
     * erzeugt einen neuen TreeView mit _root als Wurzelknoten
     *
     * @param _root der Wurzelknoten des TreeViews
     **/
    public TreeView(TreeViewNode _root) {
	root = _root;
	root.setTribe(this);
	root.setPath(new Path());

	items = new Vector();

	// Noch ein paar listenspezifische Sachen einstellen:
	// Erstens: Es darf nur jeweils eine Zeile selektiert werden: 
	setMultipleMode(false);
	
	// Zweitens: Die Methode wird aufgerufen, wenn auf die Liste geklickt wird.
	addActionListener( new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		((TreeViewNode)items.elementAt(getSelectedIndex())).flip();
	    }
	});

    }

    /**
     * faltet den Baum auf an dem Knoten, der durch path festgelegt ist
     *
     * @param path Stelle an der der Baum aufgeklappt werden soll
     **/
    public void expandNode(Path path) {
	retrieveNode(path).expand();
    }

    /**
     * faltet den Baum zusammen, an dem Knoten, der durch path festgelegt ist
     *
     * @param path Stelle, an der der Baum zusammengeklappt werden soll
     **/
    public void collapseNode(Path path) {
	retrieveNode(path).collapse();
    }

    /**
     * selektiert den Knoten am uebergebenen Pfad, insofern er ueberhaupt
     * sichtbar ist
     *
     * @param path Pfad zum zu selektierenden Knoten
     **/
    public void selectNode(Path path) {
	if ( items.contains( retrieveNode(path) ) )
	    select( items.indexOf( retrieveNode(path)) );
    }

    /**
     * gibt den Knoten zurück der sich am übergebenen Pfad befindet
     * Wichtig: Es findet keine Überprüfung statt, ob der Pfad überhaupt
     * existiert, sollte aber keine Probleme machen, wenn wir die Methode
     * nur mit solchen Pfaden aufrufen, die vorher von add erzeugt wurden.
     *
     * @param path Pfad an dem gesucht werden soll
     **/
    public TreeViewNode retrieveNode(Path path) {
	TreeViewNode help = root;

	for (int i=1; i<path.length();  i++) {
	    help = help.getSon(path.getBranch(i));
	}

	return help;
    }

    /**
     * fügt den übergebenen Knoten in den Baum an der übergebenen Stelle ein
     *
     * @param where wo der Knoten einzufügen ist
     * @param what welcher Knoten einzufügen ist
     **/
    public Path addNode(Path where, TreeViewNode what) {
	return retrieveNode(where).add(what);
    }

    /**
     * fuellt das Listenobjekt mit dem Baum
     * D.h. im Anschluss an eine Operation in dem Baum, wie z.B. aufklappen,
     * selektieren oder aehnliches, muss diese Methode aufgerufen werden,
     * damit der Baum auch korrekt auf dem Bildschirm erscheint.
     * Aber: expand(), collapse() usw. rufen diese Methode ohnehin von
     * alleine auf. Es ist nur an einer Stelle wichtig diese Methode
     * explizit aufzurufen: Naemlich nachdem der Baum vollstaendig auf-
     * gebaut worden ist und zum ersten Mal angezeigt werden soll...
     **/
    public void fill() {
	items.clear();
	removeAll();
	root.fill(items);
    }

    /**
     * gibt eine Stringrepräsentation des Baumes zurück
     **/
    public String toString() {
	StringBuffer sb = new StringBuffer();
	root.addToStringBuffer(sb);
	return sb.toString();
    }
}
