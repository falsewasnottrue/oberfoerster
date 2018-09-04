/*
 * Datei: Sort.java
 */

package basis;

/**
 * Sort repraesentiert eine Sorte (NAT etc.) fuer die Signatur
 **/
public final class Sort
{
    /**
     * Der Name der Sorte
     **/
    private String name;
 
    /**
     * Konstruktor: Erzeugt eine Sorte mit einem vorgegebenen Namen.
     *
     * @param n Der Name der neuen Sorte
     **/
    public Sort(String n)
    {
	name = n;
    }

    /**
     * Gibt den Namen der Sorte aus
     *
     * @return Der Name der Sorte
     **/
    public String getName()
    {
	return name;
    }

    /**
     * Gibt die textuelle Repraesentation der Sorte aus;
     * der Einfachkeit halber ist das ihr Name.
     *
     * @return String-Repraesentation der Sorte
     **/
    public String toString()
    {
	return name;
    }
}
