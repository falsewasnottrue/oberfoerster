/*
 *      Datei: Specification.java
 */

package basis;

/**
 * Eine Spezifikation beinhaltet eine Signatur und eine Menge von
 * Gleichungen, die die spezifizierte Theorie definieren.
 **/
public class Specification
{
    /**
     * Die Signatur
     **/
    private Signature sig;

    /**
     * Die zugrunde liegenden Gleichungen 
     **/
    private TermPair [] equations;

    /**
     * Konstruktor: Erzeugt eine neue Spezifikation aus einer gegebenen 
     * Signatur und einem Gleichungssystem
     *
     * @param s Die Signatur
     * @param e Das Gleichungssystem
     **/
    public Specification(Signature s, TermPair [] e)
    {
	sig       = s;
	equations = e;
    }
    
    /**
     * Gibt die Signatur zurueck
     *
     * @return Die Signatur
     **/
    public Signature getSig()
    {
	return sig;
    }
    
    /**
     * Gibt die zugrunde liegenden Gleichungen zurueck
     *
     * @return Das Gleichungssystem
     **/
    public TermPair [] getEquations()
    {
	return equations;
    }
}




