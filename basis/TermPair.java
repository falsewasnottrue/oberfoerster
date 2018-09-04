/*
 * Datei: TermPair.java
 */

package basis;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector; 

import visual.*;

/**
 * TermPair repraesentiert ein Termpaar; das kann eine Gleichung, eine Regel
 * oder auch ein kritisches Paar sein.
 **/
public class TermPair
{
    protected Term lhs;   // linker Term
    protected Term rhs;   // rechter Term
  
    /**
     * Konstruktor: erzeugt aus den beiden vorgegebenen Termen ein neues
     * Termpaar
     *
     * @param left Der linke Term des neuen Termpaars
     * @param right Der rechte Term des neuen Termpaars
     **/
    public TermPair(Term left, Term right){
	lhs = left;
	rhs = right;
    }

    /**
     * erzeugt ein neues Termpaar mit den Termen des übergebenen Termpaares
     *
     * @param tp das Termpaar aus dem das neue erzeugt wird
     **/
    public TermPair(TermPair tp) {
	this(tp.getLeft(),tp.getRight());
    }

    /** 
     * Diese Methode liefert den linken Term eines Termpaars zurueck.
     *
     * @return Der linke Term des Termpaars 
     **/
    public Term getLeft()
    {
	return lhs;
    }

    /** 
     * Diese Methode liefert den rechten Term eines Termpaars zurueck. 
     *
     * @return Der rechte Term des Termpaars 
     **/
    public Term getRight()
    {
	return rhs;
    }

    /** 
     * Diese Methode liefert die String-Repraesentation des Termpaars;
     * Diese hat die Form <left> = <right> 
     *
     * @return String-Repraesentation des Termpaars
     **/
    public String toString()
    {
	StringBuffer sb = new StringBuffer();
	addToStringBuffer(sb);
	return sb.toString();
    }

    /**
     * Diese Methode fuegt die String-Repraesentation an einen StringBuffer
     * an. Fuer abgeleitete Klassen ggf. zu ueberladen!
     *
     * @param sb Stringbuffer, an den die String-Repraesentation abgehaengt 
     *  wird
     **/
    public void addToStringBuffer (StringBuffer sb)
    {
	lhs.addToStringBuffer(sb);
	sb.append(" = ");
	rhs.addToStringBuffer(sb);
    }
    public static void addArrayToStringBuffer(TermPair[] tps, StringBuffer sb)
    {
	int size = tps.length;
	for (int i = 0; i < size; i++){
	    tps[i].addToStringBuffer(sb);
	    sb.append('\n');
	}
    }

    /**
     * Prueft, ob es sich bei dem Termpar um eine Regel handelt
     * In der gemeinsamen Oberklasse, ist das nicht der Fall.
     **/
    public boolean isRule()
    {
	return false;
    }

    /**
     * Prueft, ob es sich bei dem Termpar um eine Gleichung handelt
     * In der gemeinsamen Oberklasse, ist das nicht der Fall.
     **/
    public boolean isEquation()
    {
	return false;
    }

    /**
     * Prueft, ob es sich bei dem Termpar um ein kritisches Paar handelt
     * In der gemeinsamen Oberklasse, ist das nicht der Fall.
     **/
    public boolean isCriticalPair()
    {
	return false;
    }
  

    /**
     * Liefert eine Iterator uber alle verschiedenen Variablensymbole 
     * in dem TermPar zurueck.
     * D.h. in dem Iterator sind alle Variablen mit verschiedener ID
     * vorhanden.
     * ! Funktioniert nur, weil Variable.equals = true bei gleicher ID.
     **/
    public Iterator getDifferentVariables ()
    {
	Iterator i1 = getLeft().getAllSymbols();
	Iterator i2 = getRight().getAllSymbols();
	Symbol symb;
	Collection diffVars = new Vector();
	
	while (i1.hasNext()){
	    symb=(Symbol)i1.next();
	    if (symb.isVariable() && !diffVars.contains(symb)) {
		diffVars.add(symb);
	    }
	}

	while (i2.hasNext()){
	    symb=(Symbol)i2.next();
	    if (symb.isVariable() && !diffVars.contains(symb)) {
		diffVars.add(symb);
	    }
	}
		
	return diffVars.iterator();
    }

    /**
     * Macht das Termpar variablendisjunkt zu allen anderen.
     * Dies geschieht z.Zt. indem alle Variablen neu besetzt werden.
     **/
    public void makeVarDisjoint()
    {
	Iterator vars = getDifferentVariables();
	// Substitution die die Umbenennung der Variablen enthaelt
	Substitution subst = new Substitution();

	while (vars.hasNext()){
	    subst.addOrReplace((VariableSymbol)vars.next(),new Term(new VariableSymbol()));
	}
	// Substitution auf Termpaar anwenden
	subst.apply(this);

    }	

    /**
     * Wandelt ein Termpaar in eine Gleichung um, und
     * erzeugt entsprechende Partner Regel.
     * 
     * @return Neues Gleichungsobjekt
     **/
    public Equation asEquation()
    {
	// Visualobjekt benachrichtigen:
	Visual.notify(VisualEvent.newUmwdlRegGl());

	Equation diese = new Equation(this);
	// Partnerregel in anderer Richtung erzeugen
	Equation partner = new Equation(this.getRight(),this.getLeft(),diese);
	// Partner auch in dieser Gleichung eintragen
	diese.setPartner(partner);
	return diese; 
    }

    /**
     * Richtet ein TermPaar zu einer Regel
     *
     * @return Regel left -> right
     **/
    public Rule asRule()
    {
	// Visualobjekt benachrichtigen:
	Visual.notify(VisualEvent.newUmwdlRegGl());

	return new Rule(this);
    }
    

    /**
     * Richtet ein TermPaar zur symmetrischen Regel
     *
     * @return Regel right -> left
     **/
    public Rule asSymmetricRule()
    {
	// Visualobjekt benachrichtigen:
	Visual.notify(VisualEvent.newUmwdlRegGl ());

	return new Rule(this.getRight(),this.getLeft());
    }

    
    /**
     * Erzeugt ein kritisches Paar aus einem TermPaar
     * 
     * @param mom die eine ElternRegel
     * @param dad die andere ElternRegel
     **/
    public CriticalPair asCriticalPair(TermPair mom, TermPair dad)
    {
	return new CriticalPair (this,mom,dad);
    }
  /**
     * Prueft, ob der TermPair den gleichen Aufbau wie der als Parameter
     * uebergeben Term Pair hat. 
     * @param term der zu vergleichende Term
     * @return true, falls die beiden Terme den gleichen Aufbau haben,
     * ansonsten false
     **/
    public  boolean isEqual(TermPair tp)
    {
	return getLeft().isEqual(tp.getLeft())&& 
	    getRight().isEqual(tp.getRight());
    }

}

