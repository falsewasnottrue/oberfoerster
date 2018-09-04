
/*
 * Datei: CPfinder.java
 */

package basis;

/** 
 * Diese Klasse implementiert den Algorithmus 'Kritische-Paar-Bildung'
 *
 * @author Rasmus Hofmann
 * @version 25.5.99
 **/

import java.util.Iterator;
import visual.*;

public class CPfinder 
{
    /**
     * Diese Konstanten werden von der Methode findDirectedCriticalPairs(TermPair, TermPair, int) benutzt.
     * Durch deren dritten Parameter wird gesteuert, ob die Methode die Stelle Lambda bei der CP-Bildung 
     * berücksichtigt oder nicht.
     **/
    public final static int skipLambda = 0;
    public final static int touchLambda = 1;
    
    /**
     * erzeugt ein kritisches Paar mit den uebergebenen Parametern.
     * Dies geschieht nach der Vorschrift
     *   tp1 = (l,r), tp2 = (l',r')
     *   CP  = ( sigma(l)[p<-sigma(r')] , sigma(r) )
     *
     * For convenience: Das kritische Paar wird gleich noch normalisiert,
     * das Protokoll-objekt benachrichtigt und der ganze Kram in die
     * passiven Fakten geworfen.
     *
     * @param tp1 das erste Elternpaar
     * @param tp2 das zweite Elternpaar
     * @param sigma die Substitution
     * @param p die Stelle an der ersetzt wird
     **/
    private void createCriticalPair(TermPair tp1, TermPair tp2, TermPair tp3, Substitution sigma, int p)
    {
	Term l, r, left, right;
	CriticalPair cp;

	/*
	 * So, wie Ihr das hier hattet, funktioniert das nicht.
	 * Einzahlige Stellen koennen sich (im Gegensatz zu mehrzahligen)
	 * bei der Anwendung von Substitutionen verschieben... KD
	 *	
	 * Zunächst wird der Überlappungsterm berechnet:
	 * l = sigma.appliedOn(tp1.getLeft());
	 * dann die linke Seite des CP:
	 * r = sigma.appliedOn(tp2.getRight()); 
	 * left = (Term)l.clone();
	 * try { left.replaceSubterm(p, r); }
	 * catch (InvalidPlaceException e) {}
	 */
	l = (Term)tp1.getLeft().clone();
	r = (Term)tp3.getRight().clone(); 
	try { l.replaceSubterm(p, r); }
	catch (InvalidPlaceException e) {}
	left = sigma.appliedOn(l);
	
	// und schließlich die rechte Seite des CP:
	right = sigma.appliedOn(tp1.getRight());
	 
	cp = new CriticalPair(left, right, tp1, tp2);

	// Aufruf des Visualisierers:
	Visual.notify( VisualEvent.newCPErzeugen() );

	Protocol.notifyCreation(cp);
	
	Oberfoerster.getNormalizer().normalize(cp);

	if ( cp.getLeft().isEqual( cp.getRight() ) )
	    Protocol.notifyConfluentCriticalPair();
	else
	    if  ( ( tp1.isRule() && tp2.isRule() ) ||
		  ( Oberfoerster.getTermOrdering().compareTerm(cp.getLeft(),l) != Ordering.IS_GREATER &&
		    Oberfoerster.getTermOrdering().compareTerm(cp.getRight(),l)!= Ordering.IS_GREATER) )
		Oberfoerster.getPassiveFacts().insert(cp);
    }

    /**
     * führt eine 'gerichtete' CP-Bildung durch.
     * D.h. es wird versucht, die linke Seite des 2.Termpaares in die linke Seite des
     * 1.Termpaares zu unifizieren. Wenn eine Unifikation existiert, dann wird damit das
     * entsprechende kritische Paar gebildet und dieses in die Menge der passiven Fakten
     * geschrieben.
     * Durch den Parameter lambdaFlag wird dabei gesteuert, ob auch mit der Stelle
     * Lambda unifiziert werden soll.
     *
     * @param tp1 die erste Elternregel
     * @param tp2 die zweite Elternregel
     * @param lambdaFlag steuert, ob die Stelle Lambda benutzt werden soll
     **/
    private void findDirectedCriticalPairs(TermPair tp1, TermPair tp2, int lambdaFlag)
    {
	Iterator subterms = tp1.getLeft().getAllSubterms();
	Term currTerm;
	Substitution sigma;
	int p=0; // hierin wird die Stelle gespeichert

	Term left = (Term)tp2.getLeft().clone();
	Term right = (Term)tp2.getRight().clone();
        TermPair tp3 = new TermPair(left , right);
	tp3.makeVarDisjoint();

	while ( subterms.hasNext() ) {	    
	    currTerm = (Term)subterms.next();

	    // Die folgende Bedingung soll folgendes besagen:
	    // Wir wollen nur dann überlappen wenn
	    //  i) der aktuell behandelte Term keine Variable ist (Variablenüberlappung) und
	    // ii) an der Stelle Lambda wird nur überlappt, wenn lambdaFlag != skipLambda
	    if ( !currTerm.isVariable() &&  
		 ((lambdaFlag != skipLambda) || (p != 0)) )
	
		try {
		    sigma = Oberfoerster.getUnifier().unify( currTerm, tp3.getLeft() );
		    createCriticalPair(tp1,tp2,tp3,sigma,p);
		}
	    catch (NoUnificationFoundException e) { }
		
	    p++; // Im nächsten Durchlauf geht's um die nächste Stelle
	}
    }
    
    /**
     * erzeugt alle kritischen Paare zwischen den beiden Termpaaren tp1 und tp2.
     * 
     * @param tp1 Erstes Termpaar zu dem die kritischen Paare gebildet werden sollen		
     * @param tp2 Zweites Termpaar zu dem die blablabla
     **/
    private void findCriticalPairs(TermPair tp1, TermPair tp2)
    {
	findDirectedCriticalPairs(tp1, tp2, touchLambda);
	findDirectedCriticalPairs(tp2, tp1, skipLambda);
    }

    /**
     * erzeugt alle kritischen Paare eines Termpaares mit sich selbst
     *
     * @param tp das Termpaar zu dem die kritische Paare gebildet werden sollen
     **/
    private void findInherentCriticalPairs(TermPair tp)
    {

	if ( tp.isRule() ) 
	    {
		// Es handelt sich um eine Regel, daher muß nicht
		// auf Top-Level überlappt werden
		findDirectedCriticalPairs(tp, tp, skipLambda);
	    } 
	else 
	    {
		// Wenn's keine Regel war, dann kann sich's nur
		// um eine Gleichung handeln. Dann muß aber auch
		// auf Top-Level überlappt werden...
		findDirectedCriticalPairs(tp, tp, touchLambda);
	    }
    }

    /**
     * erzeugt alle möglichen kritischen Paare zu dem gegebenen Termpaar
     * D.h. alle kritischen Paare die mit den aktiven Fakten erzeugbar sind und
     * auch diejenigen die durch Überlappung mit sich selbst entstehen.
     *
     * @param tp das Termpaar zu dem alle kritischen Paare gebildet werden sollen
     **/
    public void findAllCriticalPairs(TermPair tp)
    {
	// Aufruf des Visualisierers:
	Visual.notify( VisualEvent.newCPBilden() );

	// Zunächst werden alle kritische Paare mit sich selbst gebildet
	// (falls es welche gibt)
	findInherentCriticalPairs(tp);
	
	// Sodann, wird mit allen aktiven Fakten versucht kritische Paare
	// zu bilden. Es muss nicht überprüft werden, ob es sich um Regeln
	// oder Gleichungen handelt...
	Iterator activeFacts = Oberfoerster.getActiveFacts().elements();
	
	while ( activeFacts.hasNext() ) {
	    // Aufruf des Visualisierers:
	    Visual.notify( VisualEvent.newCPBildenWith() );
	    findCriticalPairs(tp, (TermPair)activeFacts.next() );
	}
    }
}
