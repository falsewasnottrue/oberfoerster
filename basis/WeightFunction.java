package basis;

import java.util.Iterator;
import java.util.Vector;

/** 
 * Diese Klasse beschreibt eine Gewichtsfunktion, wie sie in der KBO
 * benutzt wird
 *
 * @see KBO.java
 * @author Markus Kaiser 
 **/

public class WeightFunction 
{
    /** 
     *  Das Gewicht einer Variablen
     **/
    
    protected int varWeight;

    /**
     *  Die Gewichte der Funktionen als Integers in einem Feld
     **/

    protected int[] funcWeights;

    

    /**
     *  Konstruktor: Erzeugt eine neue WeightFunction mit Variablengewicht 1
     *  und den vorgegebenen Gewichten fuer die Funktionssymbole
     *
     *  @param fsymbols Vector mit Funktionssymbolen
     *  @param weights Vector mit den zu den Funktionssymbolen in fsymbols 
     *   gehoerigen Gewichten
     **/

    public WeightFunction(Vector fsymbols, Vector weights)
           throws IllegalWeightFunctionException
    {
	/* fsymbols enthaelt FunctionSymbol's, weights enthaelt Integer's */
        int dim =  FunctionSymbol.getCounter();
        int w;
        FunctionSymbol f;
        Iterator it1,it2;
        varWeight = 1;
        funcWeights = new int[dim];
        it1 = fsymbols.iterator();
        it2 = weights.iterator();
        while (it1.hasNext()) {
              f = (FunctionSymbol) it1.next();
              if (it2.hasNext()) {
                 w = ((Integer) it2.next()).intValue();
              }
              else { throw new IllegalWeightFunctionException(); }
              funcWeights[f.getId()] = w;
        }

    }

    /**
     *  Berechnet das Gewicht eines Terms; dafuer wird die Methode 
     *  getAllSymbols des Terms benutzt, und die Gewichte aller Symbole
     *  zusammengezaehlt
     *
     *  @param t Der Term, dessen Gewicht berechnet werden soll
     *
     *  @see basis.term#getAllSymbols()
     **/
    public int getWeight(Term t)
    {
	int sum    = 0;
	Iterator i = t.getAllSymbols();
	while (i.hasNext()){
	    Symbol s = (Symbol) i.next();
	    sum += s.isVariable() ? varWeight : funcWeights[s.getId()];
	}
	return sum;
    }

    /**
     *  Berechnet das Gewicht eines Termpaares, indem die Gewicht beider
     *  Terme berechnet und addiert werden.
     *
     *  @param p das Termpaar, dessen Gewicht berechnet werden soll
     **/
    public int getWeight(TermPair p)
    {
	return getWeight(p.getLeft()) + getWeight(p.getRight());
    }
}






