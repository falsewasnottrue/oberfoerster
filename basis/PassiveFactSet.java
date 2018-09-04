
/*
 * Datei: PassiveFactSet.java
 */

package basis;

/**
 * Die Klasse PassiveFactSet kapselt die Menge der passive Fakten.
 * Intern wird diese als eine Prioritaetswarteschlange realisiert.
 *
 * @author: Rasmus Hofmann
 * @version: 11.5.99
 **/

import java.util.TreeSet;
import java.util.NoSuchElementException;

import visual.*;

public class PassiveFactSet
{
    /**
     * In TreeSet repository werden die passiven Fakten abgelegt.
     * Die Klasse TreeSet stellt so eine Art Heap dar. Der springende Punkt ist
     * das fuer alle wichtigen Operationen (einfuegen, suchen, loeschen) O(log(n))
     * garantiert wird.
     **/
    private TreeSet repository;

   
    /**
     * Enthaelt die gewaehlte Heuristik. Insbesondere erhaelt PassiveFactSet
     * von der Heuristik die Methode classify, um einen Heuristikwert
     * zu berechnen, und es erhaelt einen Comparator fuer das repository.
     **/
    private Heuristic heuristic;

    public PassiveFactSet(Heuristic _heuristic)
    {
	heuristic = _heuristic;
	repository = new TreeSet(heuristic.getComparator());
    }

    /**
     * Fuegt das kritische Paar tp in die Menge der passiven Fakten ein.
     * Dazu wird zunaechst ein Objekt vom Typ PassiveFact erzeugt, das neben
     * dem kritischen Paar auch noch den dazu gehoerigen Heuristikwert ent-
     * haelt. Diese wird dann in repository eingefuegt.
     **/
    public void insert(CriticalPair tp)
    {
	if (!tp.getLeft().isEqual(tp.getRight())){
	    PassiveFact fact = new PassiveFact(tp, heuristic.classify(tp));
	    repository.add(fact);
	}
    }

    /**
     * Gibt das oberste kritische Paar aus und ENTFERNT es aus der Warteschlange.
     * Falls das repository leer ist, wird eine NoSuchElementException geworfen.
     * Fuer dieses kritische Paar (s,t) gilt:
     *   i) Die Elternregeln befinden sich noch in den aktiven Fakten
     *  ii) s und t sind normalisiert
     * iii) s ist ungleich t
     * Diese Abfragen haetten wir uns somit im Hauptprogramm gespart! 
     *
     * @return das oberste TermPaar in der Warteschlange
     *
     * @exception NoSuchElementException
     **/
    public CriticalPair retrieve() throws NoSuchElementException
    {
	// Visualobjekt benachrichtigen:
	Visual.notify(VisualEvent.newPFAuswaehlen());

	PassiveFact foo;
	CriticalPair cp = null;
	boolean found = false;

	while ( !found ) {
	    
	    	if ( isEmpty() ) throw new NoSuchElementException();

		// Visualobjekt benachrichtigen
		Visual.notify(VisualEvent.newOberstesAuswaehlen());

		foo = (PassiveFact)(repository.first());
		repository.remove(foo);
		cp = foo.getCriticalPair();

		// Visualobjekt benachrichtigen:
		Visual.notify(VisualEvent.newElternVorhanden());

		if ( cp.isAxiom() ||
		    (Oberfoerster.getActiveFacts().contains(cp.getMom()) &&
		     Oberfoerster.getActiveFacts().contains(cp.getDad()) ) )
		    found = true;

		Oberfoerster.getNormalizer().normalize( cp );

		// Visualobjekt benachrichtigen:
		Visual.notify(VisualEvent.newZusfbktTest());

		if ( cp.getLeft().isEqual( cp.getRight() )) found = false;
	}

	return cp;
    }

    /**
     * Liefert true zurueck falls die Warteschlange leer ist, sonst false
     *
     * @return true gdw (repository==leer)
     **/
    public boolean isEmpty()
    {
	return repository.isEmpty();
    }

    /**
     * gibt die Anzahl der passiven Fakten zurück.
     *
     * @returns Anzahl der passiven Fakten
     **/
    public int getSize() {
	return repository.size();
    }

    /**
     * Lädt die Axiome aus der Spezifikation in die passiven Fakten
     * Eine Referenz auf die Spezifikation wird übergeben
     *
     **/
    public void loadAxioms()
    {
	for (int i=0; i<Oberfoerster.getSpec().getEquations().length; i++)
	    insert(new CriticalPair(Oberfoerster.getSpec().getEquations()[i],null,null));
    }

    public String toString()
    {
	return repository.toString();
    }
}

