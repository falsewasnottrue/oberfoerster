/*
 * Datei: NoMatchFoundException.java
 * @author Daniel
 */

package basis;
/**
 * Eine NoMatchFoundException wird in match geworfen, falls kein Match existiert.
 *
 * @see basis.Matcher
 **/  
public class NoMatchFoundException  extends Exception
{
    /**
     * Konstruktor: Erzeugt eine neue NoMatchFoundException.
     *
     * @param l Der Term von dem aus gematcht werden sollte
     * @param t Der Term auf den gematcht werden sollte 
     * @param sigma Die momentane Substitution
     **/ 
    public NoMatchFoundException(Term l, Term t, Substitution sigma)
    {
	// something todo...
    }
}
