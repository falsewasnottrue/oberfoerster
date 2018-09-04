/**
 * Datei: VarSet.java
 **/
package basis;

import java.util.Iterator;
import java.util.Vector;

/**
 * Ein VarSet ist eine Menge von Variablen
 * @author Markus Kaiser
 **/
public class VarSet
{
    /**
     * Vector: speichert Variablen, die zur Menge gehoeren
     **/
    protected Vector vset;

    /**
     * Konstruktor: Erzeugt eine neue, leere Variablenmenge
     **/
    public VarSet()
    {
        vset = null;
    }

    /**
     * Konstruktor: Erzeugt ein VarSet, das alle im uebergebenen Term
     * vorkommenden Variablen enthaelt
     *
     * @param t der Term, dessen Variablenmenge erzeugt werden soll
     **/     
    public VarSet(Term t)
    { 
        vset = new Vector();
        Iterator it = t.getAllSymbols();
        Symbol symb = null;
        while (it.hasNext()) {
               symb = (Symbol) it.next();
               if (symb.isVariable()) {
                   if (!vset.contains(symb)) { vset.add(symb); }
               }
        }
        
    }
    
    /**
     * Konstruktor: erzeugt Variablenmenge, die die Variablen aus s und t enthaelt
     **/
    public VarSet(Term s, Term t)
    { 
        vset = new Vector();
        Iterator it1 = s.getAllSymbols(), it2 = t.getAllSymbols();
        Symbol symb = null;
        while (it1.hasNext()) {
               symb = (Symbol) it1.next();
               if (symb.isVariable()) {
                   if (!vset.contains(symb)) { vset.add(symb); }
               }
        }
	while (it2.hasNext()) {
               symb = (Symbol) it2.next();
               if (symb.isVariable()) {
                   if (!vset.contains(symb)) { vset.add(symb); }
               }
        }
        
    }

    /**
     * Gibt an, ob das uebergebene Variablensymbol in der Menge enthalten ist
     *
     * @param v Das Variablensymbol, das ueberprueft werden soll
     **/
    public boolean contains(VariableSymbol v)
    {
	return vset.contains(v);
    }
    
    /**
     * Gibt Iterator zurueck, indem vset gespeichert wird
     **/
    public Iterator getVar()
    {
        return vset.iterator();
    }

}
