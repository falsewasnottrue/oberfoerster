
/*
 * Datei: Goals.java
 */

package basis;

/**
 * Diese Klasse enthält die Menge der Ziele
 *
 * Sie dient der leichteren und übersichtlicheren Verwaltung der Ziele.
 * Insbesondere wird die Fitzelarbeit beim Normalisieren und beim
 * Löschen der Ziele in dieser Klasse versteckt.
 *
 * @author: Rasmus Hofmann
 **/

import java.util.Vector;
import java.util.Iterator;

public class GoalSet
{

    /**
     * Hierin werden die Ziele aufbewahrt
     **/
    Vector goals;
    
    public GoalSet(TermPair[] _goals)
    {
	goals = new Vector(_goals.length);
	
	for (int i=0; i<_goals.length; i++)
	    goals.addElement(_goals[i]);
    }
	
    public void add(TermPair tp)
    {
	goals.addElement(tp);
    }

    public void remove(TermPair tp)
    {
	goals.removeElement(tp);
    }

    /**
     * normalisiert die Ziele
     **/
    public void normalize()
    {
	TermPair currTP;
	
	for (int i=0; i<goals.size(); i++) {
	    currTP = (TermPair)goals.elementAt(i);
	    Oberfoerster.getNormalizer().normalize(currTP.getLeft());
	    Oberfoerster.getNormalizer().normalize(currTP.getRight());
	}
    }

    /**
     * löscht alle Ziele die zusammengeführt wurden (d.h. die identisch sind)
     **/
    public void sweep()
    {
	TermPair currTP;

	for (int i=goals.size()-1; i>=0; i--) {
	    currTP = (TermPair)(goals.elementAt(i));
	    if ( currTP.getLeft().isEqual( currTP.getRight() )) {
		Protocol.notifyDeletion( currTP );
		goals.removeElementAt(i);
	    }
	}
    }

    public void print()
    {
	Iterator iter = goals.iterator();
	
	while (iter.hasNext())
	    System.out.println((TermPair)iter.next());
    }

    public boolean isEmpty()
    {
	return goals.isEmpty();
    }
}
