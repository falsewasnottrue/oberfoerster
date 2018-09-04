/*
 * Datei: FunctionSymbol.java
 */

package basis;

import java.lang.String;
import java.util.Hashtable;

/**
 * FunctionSymbol repraesentiert ein Funktionssymbol. Neben seinem Namen und 
 * seiner id wird so ein Funktionssymbol durch seine Stelligkeit 
 * charakterisiert (da wir uns auf eine einzige Sorte beschraenken,
 * reduziert sich das auf einen int - die Anzahl der Argumente)
 **/
public final class FunctionSymbol extends Symbol
{
    /**
     * Klassenvariable: Zaehler fuer die Anzahl der Funktionssymbole
     **/
    static private int counter = 0;

    /**
     * Die Stelligkeit des Funktionssymbols. Es sollte auf jeden Fall 
     * arity >= 0 gelten; bei arity = 0 handelt es sich um ein Konstantensymbol
     **/
    private int arity;

    /**
     * Konstruktor: erzeugt ein neues Funktionssymbol.
     * Es wir zwar ueberprueft, ob die Stelligkeit sinnvoll ist, aber nicht,
     * ob der Funktionsbezeichner schonmal vergeben wurde. Dies wird im
     * Signatur-Objekt verwaltet.
     *
     * @param n Der Name des neuen Funktionssymbols
     * @param a Die Stelligkeit des neuen Funktionssymbols (muss >=0 sein)
     *
     * @exception NegativeArityException
     **/
    public FunctionSymbol(String n, int a)
	throws NegativeArityException
    {
	if (a < 0)
	    throw new NegativeArityException(n,a);
	name  = n;
	arity = a;
	id    = counter++;
    }
    
    /** Die Funktion getCounter liest die Anzahl der benutzten Funktions-
     *  symbole aus
     **/
    static public int getCounter ()
    {
       return counter;
    }
    /**
     * Ein Funktionssymbol ist keine Variable, deshalb gibt isVariable in
     * einem FunctionSymbol immer false zurueck.
     **/
    public boolean isVariable()
    {
	return false;
    }

    /**
     * Liefert die Stelligkeit des Funktionssymbols zurueck
     *
     * @returns Die Stelligkeit des Funktionssymbols
     **/
    public int getArity()
    {
	return arity;
    }
}
