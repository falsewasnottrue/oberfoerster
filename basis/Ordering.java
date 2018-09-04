/*
 * Datei: Ordering.java
 */

package basis;

/** 
 * Diese Klasse beschreibt eine abstrakte Partialordnung - Praezedenzen, 
 * Reduktionsordnungen usw. sind Spezialisierungen dieser Klasse
 **/
public abstract class Ordering 
{
    /** 
     * Konstanten, die das Ergebnis beim Vergleich zweier Objekte bescheiben
     * myOrdering.compare( s , t ) == IS_LESS          <=> s < t
     *                                IS_EQUAL         <=> s = t
     *                                IS_GREATER       <=> s > t
     *                                IS_UNCOMPARABLE  <=> s # t (also sonst)
     **/
    public final static int IS_LESS         = -1;
    public final static int IS_EQUAL        = 0;
    public final static int IS_GREATER      = 1;
    public final static int IS_UNCOMPARABLE = 2;

    /**
     * Vergleich zweier Objekte.
     *
     * @param o wird mit p verglichen 
     * @param p wird mit o verglichen
     * @return Ergebnis des Vergleichs o ~ p bezueglich der Ordnung
     *         Wertebereich: IS_LESS, IS_EQUAL, IS_GREATER, IS_UNCOMPARABLE
     **/
    public abstract int compare(Object o , Object p);

    /**
     * Vergleiche vom Typ boolean, die angeben, ob das erste Objekt kleiner/
     * groesser/etc. dem zweiten ist. Die default-Implementierungen greifen
     * auf compare zurueck.
     *
     * @param o wird mit p verglichen 
     * @param p wird mit o verglichen
     **/
    public boolean isGreater(Object o , Object p)
    {
	return compare(o,p) == IS_GREATER;
    }
    public boolean isGreaterEqual(Object o , Object p)
    {
	int res = compare(o,p);
	return res == IS_GREATER || res == IS_EQUAL;
    }
    public boolean isLess(Object o , Object p)
    {
	return compare(o,p) == IS_LESS;
    }
    public boolean isLessEqual(Object o , Object p)
    {
	int res = compare(o,p);
	return res == IS_LESS || res == IS_EQUAL;
    }
    public boolean isEqual(Object o , Object p)
    {
	return compare(o,p) == IS_EQUAL;
    }
    public boolean isUncomparable(Object o , Object p)
    {
	return compare(o,p) == IS_UNCOMPARABLE;
    }
}
