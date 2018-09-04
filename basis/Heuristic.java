
/*
 * Datei: Heuristic.java
 **/

package basis;

/**
 * Wie der Name der Klasse subtil andeutet, wird durch sie die Heuristik implementiert,
 * die bei der Bewertung der passiven Fakten zur Anwendung kommt.
 * Durch die Kapselung in einer eigenen Klasse sollte es theoretisch eigentlich ganz,
 * ganz einfach sein, diese durch eine andere Heuristik zu ersetzen.
 * Damit das aber hinhaut, muss jede alternative Implementierung von Heuristik auch
 * alle unten beschriebenen Methoden - aeh - implementieren...
 * 
 * @author Rasmus Hofmann
 * @version 1.0
 **/

import java.util.Comparator;

public class Heuristic
{
    /**
     * Die Klassenvariable counter ist genaugenommen ziemlich spezifisch fuer die
     * von uns gewaehlte Heuristik. Um naemlich eine totale Ordnung auf den passiven
     * Fakten zu erhalten, bekommen unsere Heuristikwerte als zweiten Eintrag eine
     * Zahl, die angibt als wievielte sie bewertet wurden, mithin eine eindeutige
     * Kennzeichnung. Indem wir die Heuristikwerte lexikographisch vergleichen,
     * ergibt sich daraus eine totale Ordnung
     **/
    private static int counter = 0;

    /**
     * Der Konstruktor. Es gibt nur wenig zu tun.
     **/
    public Heuristic()
    {
    }
    
    /**
     * Berechnet den Heuristikwert zu einem gegebenen Termpaar (s,t).
     * Hier benutzen wir einfach: hValue:= ( |s|+|t| , uniqueID )
     * Dadurch kann auf den passiven Fakten eine totale Ordnung definiert
     * werden. ( Siehe getComparator() )
     *
     * @param tp das zu bewertende Termpaar
     **/
    public int[] classify(TermPair tp)
    {
	StringBuffer lTerm = new StringBuffer();
	StringBuffer rTerm = new StringBuffer();
	tp.getLeft().addToStringBuffer(lTerm);
	tp.getRight().addToStringBuffer(rTerm);

	int foo[] = { lTerm.length()+rTerm.length(), counter++ };
	return foo;
    }

    /**
     * Erzeugt einen zur Heuristik passenden Comparator.
     * Da der Comparator, der die Ordnung auf den passiven Fakten realisiert
     * natuerlich von der gewaehlten Heuristik abhaengt, enthaelt diese
     * Klasse noch eine Methode, die einen passenden Comparator zur Ver-
     * fuegung stellt.
     *
     * Gibt eine Instanz einer anonymen Klasse zurueck, die das Interface
     * Comparator implementiert. (Tja, wir lernen schnell...)
     * Verwendet eine anonyme innere Klasse. (Wow!)
     *
     * @return siehe oben
     **/
    public Comparator getComparator()
    {
	return new Comparator () {
	    public int compare(Object o1, Object o2)
		throws ClassCastException
	    {
		if (!((o1 instanceof PassiveFact) && (o2 instanceof PassiveFact)))
		    throw new ClassCastException();
		int[] i = ((PassiveFact)o1).getHValue();
		int[] j = ((PassiveFact)o2).getHValue();

		int d1 = i[0] - j[0];
		int d2 = i[1] - j[1];

		if ((d1>0) || ((d1==0) && (d2>0))) return 1;  // o1 > o2
		if ((d1<0) || ((d1==0) && (d2<0))) return -1; // o1 < o2
		else return 0; // ist nur moeglich, wenn o1==o2
	    }
	};
    }
}
