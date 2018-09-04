
/*
 * Datei: PassiveFact.java
 */

package basis;

/**
 * Diese Klasse definiert den Typ der Eintraege in der Klasse PassiveFactSet.
 * Im Wesentlichen wird hier lediglich ein Datum vom Typ 'TermPair' mit dem
 * dazugehoerigen Heuristikwert zusammengefasst
 *
 * @author Rasmus Hofmann
 * @version 1.0
 */

public class PassiveFact
{
    /**
     * Die beiden Felder enthalten wie oben beschrieben zum einen das eigentlich Faktum,
     * naemlich das kritische Paar, und zum anderen den dazu errechneten Heuristikwert.
     * Dieser Wert wird als Array von Integer realisiert, was es ermoeglicht
     *  1) eine _totale_ Ordnung auf den passiven Fakten zu definieren
     *  2) die Implementierung relativ unabhaengig von der letzlich gewaehlten
     *     Heuristik zu machen.
     **/
    private CriticalPair fact;
    private int[] hValue;

    /**
     * Erzeugt ein Objekt aus den uebergebenen Werten
     *
     * @param _fact das kritische Paar
     * @param _hValue der dazu gehoerige Heuristikwert
     **/
    public PassiveFact(CriticalPair _fact, int[] _hValue)
    {
	fact = _fact;
	hValue = _hValue;
    }

    /**
     * gibt das enthaltene Termpaar zurueck
     *
     * @return das enthaltene Termpaar
     **/
    public CriticalPair getCriticalPair()
    {
	return fact;
    }

    /**
     * gibt den Heuristikwert zurueck
     *
     * @return der Heurisikwert
     **/
    public int[] getHValue()
    {
	return hValue;
    }

    /**
     * Nur fuer Testzwecke.
     **/
    public String toString()
    {
	StringBuffer sb = new StringBuffer();

	fact.addToStringBuffer(sb);
	sb.append('(');
	sb.append(hValue[0]);
	sb.append(',');
	sb.append(hValue[1]);
	sb.append(')');

	return sb.toString();
    }
} 
