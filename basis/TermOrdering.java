package basis;

/** 
 * Diese Klasse soll nach aussen verbergen, das es mehr als eine 
 * Reduktionsordnung gibt. Alle Reduktionsordnungen sind Verfeinerungen 
 * dieser Klasse.
 **/

public abstract class TermOrdering extends Ordering
{
    /**
     * Die folgenden Funktionen vegleichen zwei Objekte;
     * dabei testen sie, ob es sich um Terme handelt, und vergleichen sie 
     * bezueglich der Reduktionsordnung.
     *
     * @param o wird mit p verglichen 
     * @param p wird mit o verglichen
     * @return Ergebnis des Vergleichs f ~ g bezueglich der Reduktionsordnung
     *         Wertebereich: IS_LESS, IS_EQUAL, IS_GREATER, IS_UNCOMPARABLE
     **/
    public int compare(Object o, Object p)
    {
	if ((o instanceof Term) && (p instanceof Term))
	    return compareTerm((Term) o, (Term) p);
	else
	    return IS_UNCOMPARABLE;
    }
    
    /**
     * Vergleicht zwei Terme bezueglich der Reduktionsordnung.
     *
     * @param s wird mit t verglichen 
     * @param t wird mit s verglichen
     * @return Ergebnis des Vergleichs s ~ t bezueglich der Reduktionsordnung
     *         Wertebereich: IS_LESS, IS_EQUAL, IS_GREATER, IS_UNCOMPARABLE
     **/
    protected abstract int compareTerm(Term s, Term t);
}






