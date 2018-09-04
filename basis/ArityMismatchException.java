/*
 * Datei: ArityMismatchException.java
 */

package basis;

/**
 * Eine ArityMismatchException wird beim Versuch geworfen, einen Term mit 
 * einer (fuer das Toplevel-Symbol) falschen Anzahl Argumenten zu erzeugen
 *
 * @see basis.Term
 **/ 
public class ArityMismatchException extends Exception
{
/**
 * Konstruktor: Erzeugt eine neue ArityMismatchException; Argumente 
 * sind die Stelligkeit des Toplevel-Symbols und die Anzahl tatsaechlich 
 * uebergebener Argumente
 *
 * @param a1 Die Stelligkeit des Symbols
 * @param a2 Die Anzahl uebergebener Argumente
 **/
    public ArityMismatchException(int a1, int a2)
    {
	// something todo...
    }
}
