/*
 * Datei: AlreadyDefinedException.java
 */

package basis;

/**
 * Eine AlreadyDefinedException wird beim Einlesen der Signatur geworfen, 
 * falls versucht wird, ein Funktionssymbol mehrmals zu deklarieren.
 *
 * @see basis.Signature
 **/
public class AlreadyDefinedException extends Exception
{
    /**
     * Konstruktor: erzeugt eine AlreadyDefinedException fuer das
     * Funktionssymbol f
     *
     *@param f Das Funktionssymbol, das nochmal deklariert werden sollte
     **/ 
    public AlreadyDefinedException(FunctionSymbol f)
    {
	// something todo...
    }
}
