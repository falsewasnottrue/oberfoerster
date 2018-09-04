/*
 * Datei: VarAlreadyDefinedException.java
 */

package basis;
/**
 * Eine VarAlreadyDefinedException wird beim Versuch geworfen, ein 
 * Variablensymbol mit einem bereits vergebenen Names zu erzeugen.
 * 
 * @see basis.VariableSymbol
 **/
public class VarAlreadyDefinedException extends Exception
{
    /**
     * Konstruktor: Erzeugt eine VarAlreadyDefinedException zu einem
     * vorgegebenen Variablennamen
     *
     * @param s Der Name des Variablensymbols, das mehrfach deklariert
     *  werden sollte
     **/ 
    public VarAlreadyDefinedException(String s)
    {
	// something todo...
    }
}
