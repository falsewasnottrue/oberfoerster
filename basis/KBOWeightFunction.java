package basis;

import java.util.Iterator;
import java.util.Vector;

/**
 * KBOWeightFunction.java
 *
 *
 * Created: Mon May 10 13:02:01 1999
 *
 * @author Praktikum Reduktionssysteme (Markus Kaiser)
 * @version
 **/



public final class KBOWeightFunction extends WeightFunction
{    
    /**
     * Praezedenz fuer KBO
     **/
    Precedence Prec;
    
    /**
     * Vector mit Funktionssymbolen
     **/
    Vector fsymb;

    /**
     * Konstruktor, der die Argumente ueberprueft, ob diese fuer eine
     * KBO benutzt werden koennen
     **/

    public KBOWeightFunction(Vector fsymbols, Vector weights, Precedence prec)
           throws IllegalWeightFunctionException
    {
	super(fsymbols,weights);
        Prec = prec;
        fsymb = fsymbols;
	testIfOK();
    }

    /**
     * Test, ob Eingabe o.k. ist
     **/
    private void testIfOK()
	throws IllegalWeightFunctionException
    {
        int a;
        int w;
        FunctionSymbol f;
        Iterator it = fsymb.iterator();
	/* Ueberpruefung, der fuer die KBO notwendigen Bedingungen */
        while (it.hasNext()) {
             f = (FunctionSymbol) it.next();
              a = f.getArity();
              if (!Prec.maxPrec(f,Prec)) {
                 w = funcWeights[f.getId()];
                 if (w == 0) {
		     if (a == 0 || a == 1) {
                        throw new IllegalWeightFunctionException();
                     }
                 }
                 if (w < 0) {
                    throw new IllegalWeightFunctionException();
                 }
              }
        }
    }
}
