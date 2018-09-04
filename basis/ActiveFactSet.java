/**
 * Datei ActiveFactSet
 *
 * @author Andreas Kohlmaier
 **/

package basis; 

import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;

import java.io.FileReader;
import visual.*;

/**
 * repraesentiert die Menge der aktiven Fakten. ann Regeln oder 
 * Gleichungen enthalten
 **/
public class ActiveFactSet {

    // Regeln pro Topsymbol ein Vector
    private Vector[] rules;
    // Gleichungen pro Topsymbol ein Vector in eine Richtung
    private Vector[] equationsLeft;
    // und in die andere Richtung
    private Vector[] equationsRight;
    // Anzahl der unterschiedlichen topSymbole
    private int dim = 0;

    /**
     * leerer Konstruktor. Legt die Vectoren an.
     **/
    public ActiveFactSet()  
    {
	dim = FunctionSymbol.getCounter()+1;
     	rules = new Vector[dim];
	equationsLeft = new Vector[dim];
	equationsRight = new Vector[dim];
	for (int i=0;i<dim;i++){
	    rules[i] = new Vector();
	    equationsLeft[i] = new Vector();
	    equationsRight[i] = new Vector();
	}
    }

    /**
     * Teste für alle Regeln l->r aus den aktiven Fakten, ob die linke Seite mit dem
     * tp reduzierbar ist. Falls ja, dann normalisiere (l,r), lösche es aus den
     * aktiven Fakten und schreibe es in die passiven Fakten    * Reduziert mit sich selbst
     **/
    public void interreduce(TermPair tp)
    {
	// Visualobjekt benachrichtigen:
	Visual.notify(VisualEvent.newInterred());

   	TermPair currRule;
	boolean removed;
	// HauptSchleife ueber die Anzahl der topsymbole
	Vector curVect = null;
	for (int i=0;i<dim;i++){
	    // Regeln betrachten
	    for (int pos = rules[i].size()-1; pos >= 0; pos--) {
		
		currRule = (TermPair)rules[i].elementAt(pos);
		remove( currRule );
		removed=false;
	   	    
		// Wenn linke Seite normalisiert werden kann, wandert sie zurueck in die passiven Fakten
		if ( Oberfoerster.getNormalizer().normalize( currRule.getLeft(), tp ) ) {
		    // Visualobjekt benachrichtigen:
		    Visual.notify(VisualEvent.newInterredLinks());

		    // Visualobjekt benachrichtigen:
		    Visual.notify(VisualEvent.newLoeschen());

		    Protocol.notifyDeletion( (Rule)currRule );
		    removed = true;
		    // linke und rechte Seite normalisieren
		    Oberfoerster.getNormalizer().normalize( currRule );

		    // Visualobjekt benachrichtigen:
		    Visual.notify(VisualEvent.newInPFAufnehmen());

		    Oberfoerster.getPassiveFacts().insert( new CriticalPair( currRule, null, null ) );
		}
		else {
		    // Wenn nur die rechte Seite normalisiert werden kann, normalisieren und drin lassen
		    // Visualobjekt benachrichtigen:
		    Visual.notify(VisualEvent.newInterredRechts());

		    if ( Oberfoerster.getNormalizer().normalize( currRule.getRight(), tp ) ) {
			Oberfoerster.getNormalizer().normalize( currRule.getRight() );
		    }
		}
		
		// Das Topsymbol hat sich moeglicherweise geaendert
		// deswegen muss das Termpaar neu einsortiert werden
		if (!removed) add(currRule);
	    }

	    // Gleichungen betrachten
	    // equationsLeft und equationRight enthalten die gleichen Elemente nur in entgegengesetzer Richtung
	    for (int pos = equationsLeft[i].size()-1; pos>=0; pos--) {
		
		currRule = (TermPair)equationsLeft[i].elementAt(pos);
		removed=false;
		remove(currRule);

		if ( ( Oberfoerster.getNormalizer().normalize( currRule.getLeft(), tp ) ) ||
		     ( Oberfoerster.getNormalizer().normalize( currRule.getRight(), tp ) ) ) {
		    // Gleichung loeschen,..
		    Protocol.notifyDeletion( (Equation)currRule );
		    removed = true;
		    // .. normalisieren,..
		    Oberfoerster.getNormalizer().normalize( currRule );

		    // Visualobjekt benachrichtigen:
		    Visual.notify(VisualEvent.newInPFAufnehmen());

		    // ... und in passive Fakten aufnehmen
		    Oberfoerster.getPassiveFacts().insert( new CriticalPair( currRule, null, null ) );
		}
		
		// Das Topsymbol hat sich moeglicherweise geaendert
		// deswegen muss das Termpaar neu einsortiert werden
		if (!removed) add(currRule);

	    }
	}
    }
   
    /**
     * Fügt das Termpaar in die Menge ein
     * Entweder in den RegelVector oder in beide Gleichungsvectoren
     *
     * @param tp das einzufuegende Termpaar
     **/
    public void add(TermPair tp)
    {
	// Kritische Paare werden wie Gleichungen behandelt
	// sollten aber eh nie in aktive Fakten aufgenommen werden
	if (tp.isRule()){
	    rules[id(tp.getLeft())].add(tp);
	    //	    System.out.println("Adding "+tp+" to Rules Vector #" +topNr);
	}
	else {
	    // Die Gleichung wird nicht von der aktiven Fakten subsummiert.
	    // Das wurde schon in der Hauptvervollstaendigungsschleife sicher-
	    // gestellt. Daher koennen wir sie ungeprueft aufnehmen:
	    equationsLeft[id(tp.getLeft())].add(tp);
	    equationsRight[id(tp.getRight())].add(((Equation)tp).getPartner());
	}
   }

    /**
     * Überprüft, ob das Termpaar tp1 durch die Gleichung tp2 subsummiert wird
     *
     * @param tp1 das zu überprüfende Termpaar 
     * @param tp2 eine Gleichung
     **/
    public boolean isSubsummized(TermPair tp1, Equation tp2)
    {
	Substitution sigma, sigmaStrich;

	try {
     	    sigma = Oberfoerster.getMatcher().match(tp2.getLeft(), tp1.getLeft());
	    sigmaStrich = Oberfoerster.getMatcher().xmatch( tp2.getRight(), tp1.getRight(), sigma );
	    return true;
	}
	catch (NoMatchFoundException e) {}

	// Falls keine Substitutionen wie oben beschrieben existieren, dann wird tp1 halt
	//.nicht von tp2 subsummiert, also:
	return false;
    }

    /**
     * ueberprueft, ob das Termpaar von irgendeiner Gleichung in den aktiven Fakten subsummiert wird.
     *
     * @param tp das zu ueberpruefende Termpaar
     **/
    public boolean isSubsummized(TermPair tp)
    {
	boolean subsummized = false;
	Iterator allEquat = allEquations();

	while (allEquat.hasNext() && !subsummized) {
	    subsummized = subsummized || isSubsummized( tp, (Equation)allEquat.next() );
	}

	return subsummized;
    }

    /**
     * Loescht ein Termpaar und ggf. Partnerregel aus der Menge.
     * Realisiert ueber call back.
     **/
    public void remove(TermPair tp)
    {
	if (tp.isRule()){
	    rules[id(tp.getLeft())].remove(tp);
	}
	else {
	    equationsLeft[id(tp.getLeft())].remove(tp);
	    equationsRight[id(tp.getRight())].remove(((Equation)tp).getPartner());
	    /*	    if ( equationsLeft.contains( tp ) ) {
		equationsLeft.remove(tp);
		equationsRight.remove(((Equation)tp).getPartner());
	    }
	    else {
		equationsRight.remove(tp);
		equationsLeft.remove(((Equation)tp).getPartner());
	    }
	    */
	}
    }

    /**
     * Liefert einen Iterator ueber alle enthaltenen Fakten
     * zuerst Regeln, dann Gleichungen
     **/
    public Iterator elements()
    {
	MultiVector vect = new MultiVector();
	for (int i=0;i<dim;i++){
	    vect.addVector(rules[i]);
	    vect.addVector(equationsLeft[i]);
	    vect.addVector(equationsRight[i]);
	}
	return vect.iterator();
	
    }
   
    /**
     * Liefert einen Iterator ueber alle enthaltenen Fakten
     * die mit einem VariablenSymbol oder demselben TopSymbol wie
     * das uebergebene Termpaar anfangen
     **/
    public Iterator elements(Term t)
    {
	int topNr = id (t);
	MultiVector vect = new MultiVector();
	//	System.out.print("Vectoren fuer " + t + " (");
	
	vect.addVector(rules[0]);
	vect.addVector(equationsLeft[0]);
	vect.addVector(equationsRight[0]);
	if (!t.getTopSymbol().isVariable()){
	    //	    System.out.print(" " + topNr);
	    vect.addVector(rules[topNr]);
	    vect.addVector(equationsLeft[topNr]);
	    vect.addVector(equationsRight[topNr]);
	}
	//	System.out.println(" )");
	
	return vect.iterator();
    }

    /**
     * Liefert Iterator ueber alle Gleichungen
     * benutzt dazu einen MultiVector.
     **/
     
    public Iterator allEquations()
    {
  	MultiVector vect = new MultiVector();
	for (int i=0;i<dim;i++){
	    vect.addVector(equationsLeft[i]);
	    vect.addVector(equationsRight[i]);
	}
	return vect.iterator();
    }

    /**
     * Liefert Iterator ueber alle Regeln.
     * Der Iterator wird ueber einen MultiVector aufgebaut, 
     * in den alle Regel enthaltenden Vectoren eingebaut werden.
     **/
    public Iterator allRules()
    {
	MultiVector vect = new MultiVector();
	for (int i=0;i<dim;i++)
	    vect.addVector(rules[i]);
	return vect.iterator();
    }

    /** 
     * Gibt alle Elemente der Menge auf dem StringBuffer aus
     **/
    public void addToStringBuffer(StringBuffer sb)
    {
	sb.append("Aktive Fakten : \n");
	MultiVector activeFacts = new MultiVector();
	for (int i=0;i<dim;i++){
	    activeFacts.addVector(rules[i]);
	    activeFacts.addVector(equationsLeft[i]);
	}
	Iterator i = activeFacts.iterator();
	while (i.hasNext()){
	    ((TermPair)i.next()).addToStringBuffer(sb);
	    sb.append('\n');
	}
    }

    /**
     * gibt die Anzahl der aktiven Fakten zurück.
     * sinnvollerweise werden dabei Gleichungen dabei nur einmal gezählt,
     * wiewohl sie auch "zweifach" im Speicher gehalten werden.
     *
     * @returns Anzahl der aktiven Fakten
     **/
    public int getSize() {
	int size = 0;
	for (int i=0;i<dim;i++)
	    size += rules[i].size() + equationsLeft[i].size();
	return size;
    }

    public String toString()
    {
	StringBuffer sb=new StringBuffer();
	addToStringBuffer(sb);
	return sb.toString();
    }

    /**
     * Liefert die Vector ID fuer das Termpaar
     * Sortiert wird nach der linken Seite:
     * Variablen -> 0
     * FunktionsSymbole -> ID des Funktionssymbols
     **/
    private int id(TermPair tp)
    {
	if (!tp.getLeft().getTopSymbol().isVariable()) 
	    return tp.getLeft().getTopSymbol().getId()+1;
	else
	    return 0;
    }

    private int id(Term t)
    {
	if (!t.getTopSymbol().isVariable()) 
	    return t.getTopSymbol().getId()+1;
	else
	    return 0;
    }


    /**
     * Prueft, ob Termpaar in aktiver Faktenmenge enthalten ist
     **/
    public boolean contains(TermPair tp)
    {
	int topNr = id(tp);
	return (rules[topNr].contains(tp) || equationsLeft[topNr].contains(tp) || equationsRight[topNr].contains(tp));
    }


    public static void main (String [] argv){
	Protocol.initProtocol(System.out);
	try {
	    Parser parser = new Parser (new FileReader (argv[0]));
	    XSpec xspec = parser.parseXSpec();
	    ActiveFactSet af = new ActiveFactSet();

	    for (int i=0;i<xspec.spec.getEquations().length;i++)
		af.add(xspec.spec.getEquations()[i].asEquation());

	    System.out.println(af);
	    
	}
	catch (Exception e){
	    System.err.println ("Exception: " + e);
	    e.printStackTrace();
	}
   }

}
