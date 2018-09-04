/*
 * Datei: NegativeArityException.java
 */

package basis;

/**
 * Eine NegativeArityException wird geworfen, wenn versucht wird,
 * ein Funktionssymbol mit negativer Stelligkeit zu erzeugen
 *
 * @see basis.FunctionSymbol
 **/
public class NegativeArityException extends Exception
{
    /**
     * Konstruktor: erzeugt eine NegativeArityException.
     *
     * @param s Der Name des Funktionssymbols, das negative Stelligkeit haben 
     *          sollte
     * @param a Die negative Zahl, die die Stelligkeit von s sein sollte
     **/
    public NegativeArityException(String s, int a)
    {
	// something todo...
    }
}
