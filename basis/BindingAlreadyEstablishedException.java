/*
 * Datei: BindingAlreadyEstablishedException.java
 */

package basis;
/**
 * Eine BindingAlreadyEstablishedException wird in Substitution 
 * geworfen, falls versucht wird, eine bereits gebundene Variable 
 * nochmals zu binden
 *
 * @see basis.Substitution
 **/  
public class BindingAlreadyEstablishedException extends Exception
{
    /**
     * Konstruktor: Erzeugt eine neue BindingAlreadyEstablishedException.
     *
     * @param v Die Variable, die mehrfach gebunden werden sollte
     * @param sigma Die Substitution, in der der Fehler passierte
     **/ 
    public BindingAlreadyEstablishedException(VariableSymbol v, Substitution sigma)
    {
	// something todo...
    }
}
