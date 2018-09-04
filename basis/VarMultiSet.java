/**
 * Datei: VarMultiSet.java
 **/

package basis;

import java.util.Iterator;
import java.util.Vector;

/**
 * Ein VarMultiSet ist eine Multimenge von Variablensymbolen
 * @author Markus Kaiser
 **/ 
public class VarMultiSet
{
    /**
     * Vector, in dem die Elemente der Multimenge abgelegt werden
     **/
    protected Vector vmset;

    /**
     * Konstruktor: Erzeugt ein neues, leeres VarMultiSet
     **/
    public VarMultiSet()
    {
        vmset = null;
    }

    /**
     * Konstruktor: Erzeugt ein VarMultiSet, das alle im uebergebenen Term
     * vorkommenden Variablen (mit der entsprechenden Haeufigkeit) enthaelt
     *
     * @param t der Term, dessen Variablenmultimenge erzeugt werden soll
     **/
    public VarMultiSet(Term t)
    {
        vmset = new Vector();
        Iterator it =  t.getAllSymbols();
        Symbol symb;
        while (it.hasNext()) {
               symb = (Symbol) it.next();
               if (symb.isVariable()) { vmset.add(symb); }
        }
    }

    /**
     * Gibt an, wie oft das uebergebene Variablensymbol in der
     * Multimenge enthalten ist
     *
     * @param v Die Variable, deren Haeufigkeit ausgegeben weden soll
     *
     * @return Zahl, die angibt, wie oft v in der Multimenge vorkommt 
     **/
    public int count(VariableSymbol v)
    {   
        int counter = 0;
        VariableSymbol vsymb = null;
        Iterator it = vmset.iterator();
        while (it.hasNext()) {
               vsymb = (VariableSymbol) it.next();
               if (vsymb == v ) { counter++; }
        }
	return counter;
    }
    
    /**
     * varMultiSetComp vergleicht die Variablenmultimengen der Terme s und t
     **/
    public int varMultiSetComp (Term s, Term t)
    {
        boolean res1 = true, res2 = true;
        VarMultiSet VM = new VarMultiSet(t);
        VarSet VS = new VarSet(s,t);
        Iterator it = VS.getVar();
        VariableSymbol v;
        while (it.hasNext()) {
               v = (VariableSymbol) it.next();
               if (this.count(v) < VM.count(v)) { res1 = false; }
               if (VM.count(v) < this.count(v)) { res2 = false; }
        }
        if (res1) {
            if (res2) { return TermOrdering.IS_EQUAL; }
            else { return TermOrdering.IS_GREATER; }
	}
       else {
		if (res2) { return TermOrdering.IS_LESS; }
                else { return TermOrdering.IS_UNCOMPARABLE; }
       }
    }         

}
