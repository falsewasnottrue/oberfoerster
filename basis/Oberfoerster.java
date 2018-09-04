
/*
 * Datei: Oberfoerster.java
 */

package basis;

/**
 * Das zentrale Objekt, das die 
 *
 * @author Rasmus Hofmann
 **/

import java.util.Iterator;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.io.IOException;
import visual.*;

public class Oberfoerster 
{
    /**
     * Konstanten für die Methode complete(int)
     **/
    public static int skipProof = 0;
    public static int withProof = 1;
    
    /**
     * Verweis auf die aktiven Fakten
     **/
    private static ActiveFactSet activeFacts;

    /**
     * Verweis auf die passiven Fakten
     **/
    private static PassiveFactSet passiveFacts;

    /**
     * Die Spezifikation des zu bearbeitenden Problems
     **/
    private static XSpec spec;

    /**
     * Das Objekt, das Unifizieren besorgt
     **/
    private static Unifier unifier;
	
    /**
     * Das Objekt, das Matching besorgt
     **/
    private static Matcher matcher;
	
    /**
     * Das Objekt, das Normalisieren besorgt
     **/
    private static Normalizer normalizer;
    
    /**
     * Das Objekt, das die CP-Bildung besorgt
     **/
    private static CPfinder cpfinder;

    /**
     * Das Objekt, in dem die Ziele gespeichert werden
     **/
    private static GoalSet goals;

    
    /**
     * gibt die aktiven Fakten zurück
     *
     * @return die aktiven Fakten
     **/
    public static ActiveFactSet getActiveFacts()
    {
    	return activeFacts;
    }

    /**
     * gibt die Spezification zurueck
     **/
    public static Specification getSpec()
    {
	return spec.spec;
    }

    /**
     * gibt die passiven Fakten zurück
     *
     * @return die passiven Fakten
     **/
    
    public static PassiveFactSet getPassiveFacts()
    {
      	return passiveFacts;
    }
    
    /**
     * gibt den Unifizierer zurück
     *
     * @return Unifizierer
     **/
    public static Unifier getUnifier()
    {
      	return unifier;
    }

    /**
     * gibt den Matcher zurück
     *
     * @return Matcher
     **/
    public static Matcher getMatcher()
    {
     	return matcher;
    }

    /**
     * gibt den Normalisierer zurück
     *
     * @return Normalisierer
     **/
    public static Normalizer getNormalizer()
    {
      	return normalizer;
    }

    /**
     * gibt das Array der Ziele zurück
     *
     * @return Array der Ziele 
     **/
    public static TermPair[] getGoals()
    {
      	return spec.goals;
    }
   
    /**
     * gibt das GoalSet zurueck
     *
     * @return das GoalSet
     **/
    public static GoalSet getGoalSet()
    {
	return goals;
    }
	
    /**
     * gibt die verwendete Termordnung zurück
     *
     * return die Termordnung
     **/
    public static TermOrdering getTermOrdering()
    {
	return spec.ordering;
    }

    public static void testConfluence() {
    	// alle Axiome ohne Ordnungstest richten und in
       	// die aktiven Fakten übernehmen
       	for (int i=0; i<spec.spec.getEquations().length; i++)
       		activeFacts.add(spec.spec.getEquations()[i].asRule());
 
	// alle kritischen Paare bilden
	Iterator aF = activeFacts.elements();
	while ( aF.hasNext() )
	    cpfinder.findAllCriticalPairs( (TermPair)(aF.next()) );
	
	// und diese auf Zusammenführbarkeit testen
	boolean flag = true;
	TermPair tp;

	while ( !passiveFacts.isEmpty() ) {
	    tp = passiveFacts.retrieve();
	    normalizer.normalize(tp.getLeft());
            normalizer.normalize(tp.getRight());
	    if ( !tp.getLeft().equals(tp.getRight()) ) {
		System.out.println( tp.toString() );
		flag = false;
	    } // if
	} // while 

	if (flag == true) System.out.println("R ist konfluent!");
		     else System.out.println("R ist nicht konfluent!");
    }

    public static void execReduction() {

	System.out.println("Fuehre eine Reduktion durch!");

	// alle Axiome ohne Ordnungstest richten und in
	// die aktiven Fakten übernehmen
	for (int i=0; i<spec.spec.getEquations().length; i++)
	    activeFacts.add(spec.spec.getEquations()[i].asRule()); 

	// die zu beweisenden Gleichungen werden auf
	// Normalform gebracht und ausgegeben
	for (int i=0; i<spec.goals.length; i++) {
	    System.out.println("Normalisiere: "+spec.goals[i]);
	    normalizer.normalize(spec.goals[i].getLeft());
	    normalizer.normalize(spec.goals[i].getRight());
	    System.out.println(spec.goals[i].toString());
	}
    } // execReduction()

    public static void testTermination() {
	// Das gegebene Gleichungssystem wird in ein >-verträgliches
	// Regel-/Gleichungssystem umgewandelt.
	for (int i=0; i<spec.spec.getEquations().length; i++) {
	    int foo = getTermOrdering().compareTerm(spec.spec.getEquations()[i].getLeft(),
                                                     spec.spec.getEquations()[i].getRight());
	    switch (foo) {
	    case TermOrdering.IS_GREATER : 
		activeFacts.add(spec.spec.getEquations()[i].asRule());
		break;
	    case TermOrdering.IS_LESS :
		activeFacts.add(spec.spec.getEquations()[i].asSymmetricRule());
		break;
	    default : // unvergleichbar oder gleich
		activeFacts.add(spec.spec.getEquations()[i].asEquation());
	    }
	}

	// die zu beweisenden Gleichungen werden auf
	// Normalform gebracht und ausgegeben
	for (int i=0; i<spec.goals.length; i++) {
	    normalizer.normalize(spec.goals[i].getLeft());
	    normalizer.normalize(spec.goals[i].getRight());
	    System.out.println(spec.goals[i].toString());
	}
    } // testTermination()

    public static void testConvergence() {
	// Das gegebene Gleichungssystem wird in ein >-verträgliches
	// Regel-/Gleichungssystem umgewandelt.
	for (int i=0; i<spec.spec.getEquations().length; i++) {
	    int foo = getTermOrdering().compareTerm(spec.spec.getEquations()[i].getLeft(),
						     spec.spec.getEquations()[i].getRight());
	    switch (foo) {
	    case TermOrdering.IS_GREATER : 
		activeFacts.add(spec.spec.getEquations()[i].asRule());
		break;
	    case TermOrdering.IS_LESS :
		activeFacts.add(spec.spec.getEquations()[i].asSymmetricRule());
		break;
	    default : // unvergleichbar oder gleich
		activeFacts.add(spec.spec.getEquations()[i].asEquation());
	    }
	}

	// alle kritischen Paare bilden
	Iterator aF = activeFacts.elements();
	while ( aF.hasNext() )
	    cpfinder.findAllCriticalPairs( (TermPair)(aF.next()) );

	// und diese auf Zusammenführbarkeit testen
	boolean flag = true;
	TermPair tp;
	while ( !passiveFacts.isEmpty() ) {
	    tp = passiveFacts.retrieve();
	    if ( !tp.getLeft().isEqual(tp.getRight()) ) {
		System.out.println( tp.toString() );
		flag = false;
	    }
	}

	if (flag == true) System.out.println("R ist konvergent!");
		     else System.out.println("R ist nicht konvergent!");	
    } // testConvergence()

    public static void ausgeben() throws IOException
    {
	System.out.println("---------------------------------------");
	System.out.println("Aktive Fakten : ");
	System.out.println(activeFacts);
	//System.out.println("Passive Fakten : ");
	//System.out.println(passiveFacts);
	System.out.println("Ziele : ");
	goals.print();
	System.out.println("---------------------------------------");
	//System.in.read();
    }

    public static void complete(int proofFlag) {
	TermPair currTP;	 // das aktuell bearbeitete Termpaar
	boolean proofComplete = false;   // wird im Beweismodus verwendet

	// Zunaechst werden die passiven Fakten mit den Axiomen geladen
	passiveFacts.loadAxioms();

	while ( (!passiveFacts.isEmpty()) && (!proofComplete) ) {

	    // Visualobjekt ueber Vervollstaendigungsschleifen informieren:
	    Visual.notify(VisualEvent.newCLoop());

	    // Visualobjekt ueber Vervollstaendigungsschritt informieren:
	    Visual.notify(VisualEvent.newCStep());

	    // Wähle das naechste zu bearbeitende kritische Paar (s. PassiveFactSet.retrieve() )
	    try {
	        currTP = passiveFacts.retrieve();

	    	// Prüfe ob das Termpaar richtbar ist, wenn ja, dann wandle es
	       	// in eine Regel um:

		// Visualobjekt benachrichtigen:
		Visual.notify(VisualEvent.newRichten());

	       	switch ( getTermOrdering().compareTerm(currTP.getLeft(), currTP.getRight())) {
	       	case TermOrdering.IS_GREATER : 
	       	    currTP = currTP.asRule();
	       	    break;
	       	case TermOrdering.IS_LESS :
	       	    currTP = currTP.asSymmetricRule();
	       	    break;
	       	default : // unvergleichbar oder gleich
		    // Jetzt wird die Sache haarig: Wir behandeln Gleichungen s=t, indem wir sie
		    // einmal als Regel s->t und einmal als Regel t->s in die aktiven Fakten auf-
		    // nehmen. Diese Partnerregel muss hier erzeugt werden. Die Methoden unten
		    // kuemmern sich dann aber darum, ob es sich um eine Regel oder eine Gleichung
		    // handelt und fuehren gegebenenfalls ihre Operationen zweimal aus.
	       	    currTP = currTP.asEquation();
		}
		currTP.makeVarDisjoint();

		// Falls es sich um eine Gleichung handelt und diese durch die aktiven
		// Fakten subsummiert wird, dann sind die folgenden Schritte ueberfluessig
		// Die Exception braucht es, um das try-Statement zu verlassen, break wuerde
		// gleich die ganze while-Schleife beenden.
		if ( currTP.isEquation() && activeFacts.isSubsummized(currTP) ) 
 		    throw new NoSuchElementException();	

	       	// Versuche mit dem Termpaar die linken und rechten Seiten der
	       	// aktiven Fakten zu simplifizieren
	        activeFacts.interreduce( currTP );

		// Wenn currTP eine Gleichung ist, muß auch mit deren Partner interreduziert werden:
		if ( currTP.isEquation() ) 
		    activeFacts.interreduce( ((Equation)currTP).getPartner() );
        	
	       	// Bilde mit dem aktuellen Termpaar alle kritischen Paare durch Überlappung mit
	       	// den aktiven Fakten und mit sich selbst
	       	cpfinder.findAllCriticalPairs( currTP );
		if ( currTP.isEquation() ) cpfinder.findAllCriticalPairs( ((Equation)currTP).getPartner() );

		// Visualobjekt benachrichtigen:
		Visual.notify(VisualEvent.newInAFAufnehmen());

		// Füge das aktuelle Termpaar in die aktiven Fakten ein
		activeFacts.add( currTP );

	       	// Befinden wir uns im Beweis-Modus, dann geht's gleich noch
	       	// ein klein wenig weiter:
	       	if ( proofFlag == withProof ) {
	       				
	       	    // Wir normalisieren die Ziele mit den momentanen akt.Fakten
	       	    goals.normalize();

       		    // Sodann löschen wir die Ziele die zusammengeführt wurden
       		    goals.sweep();

	       	    // Wenn nun keine Ziele mehr uebrig sind, dann war's das
	       	    if ( goals.isEmpty() ) proofComplete = true;
	       	} // if

	    }
	    catch (NoSuchElementException e) { /* bedeutet: keine kritischen Paare mehr */ }
	} // while

	System.out.println("\nVervollständigtes Regel/Gleichungssystem:");
	System.out.println(activeFacts);
	
	if ( proofFlag == withProof ) {
	    goals.normalize();
	    goals.sweep();

	    if ( goals.isEmpty()  )
		System.out.println("Damit wurden alle Ziele bewiesen!\n");
	    else {
		System.out.println("Die folgenden Ziele konnten nicht gezeigt werden:");
		goals.print();
		System.out.println();
	    }
	}
    } // complete(int)


    /**
     * Initialisiert sich. Die Spec muss schon gesetzt sein.
     **/
    public static void initialize()
    {
	// Erstmal alles anlegen
      	activeFacts = new ActiveFactSet();
	passiveFacts = new PassiveFactSet(new Heuristic());
		
	unifier = new Unifier();
	matcher = new Matcher();
	normalizer = new Normalizer();
	cpfinder = new CPfinder();
	goals = new GoalSet(getGoals());
	Protocol.initProtocol(System.out);
    };

    /**
     * Startet den Beweiser
     **/
    public static void run()
    {
	Protocol.notifyStartSystem();
	// Und schliesslich werden abhängig vom Modus die nötigen
	// Operationen durchgeführt.
	switch (spec.mode) {
	case XSpec.MODE_COMPLETION :
	    complete(skipProof);
	    break;
	case XSpec.MODE_CONFLUENCE :
	    testConfluence();
	    break;
	case XSpec.MODE_CONVERGENCE :
	    testConvergence();
	    break;					
	case XSpec.MODE_PROOF :
	    complete(withProof);
	    break;
	case XSpec.MODE_REDUCTION :
	    execReduction();
	    break;
	case XSpec.MODE_TERMINATION :
	    testTermination();
	    break;
	} //switch
	Protocol.notifyStopSystem();
    }

    /**
     * Liest eine Spezifikation aus dem angegeben Dateinamen.
     **/
    public static void parseSpec(String specName)
    {
	// Zunaechst werfen wir den Parser an und lassen uns eine
	// XSpec erzeugen
	try {
	    Parser parser = new Parser (new FileReader (specName));
	    spec = parser.parseXSpec();
	}
	catch (Exception e) {
	    System.err.println ("Exception: " + e);
	    e.printStackTrace();
	}
    } // main

    public static void main (String[] args)
    {
	parseSpec("specs/gr1.cp");// )Margs[0]);
	initialize();
	run();
    }

} // Klasse Oberfoerster
