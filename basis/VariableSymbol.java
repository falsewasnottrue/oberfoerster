/*
 * Datei: VariableSymbol.java
 */

package basis;

import java.util.Hashtable;
/**
 * VariableSymbol repraesentiert ein Variablensymbol.
 **/
public final class VariableSymbol extends Symbol
{
    /**
     * Klassenvariable: Zaehler fuer die Anzahl der Variablen
     **/
    static private int counter = 0;

    /**
     * Hashtable, in der gespeichert wird, welche Variablennamen vergeben sind
     **/    
    static private Hashtable usedNames = new Hashtable();

    /**
     * Konstruktor: Erzeugt eine neue Variable mit neuer id und einem
     * Namen der Form x<id>
     **/
    public VariableSymbol()
    {
	id   = counter++;
	name = "x" + id;
    }

    /**
     * Konstruktor mit einem vorgegebenen Namen.
     * Es wird ueberprueft, ob eine Variable gleichen Namens schon vergeben
     * wurde. 
     * BUG: Dieser Test funktioniert nicht mit Variablen der Form x123,
     * die also mit dem anderen Konstruktur erzeugt wurden.
     **/
    public VariableSymbol(String n)
	throws VarAlreadyDefinedException
    {
	if(usedNames.get(n) != null)
	    throw new VarAlreadyDefinedException(n);

	id   = counter++;
	name = n;
	usedNames.put(n,this);
    }

    /**
     * Eine Variable ist eine Variable ist eine Variable.
     *
     * @return true, da dies nunmal ein Variablensymbol ist.
     **/
    public boolean isVariable()
    {
	return true;
    }

    /**
     * Gibt die Anzahl der angelegten Variablen an
     **/
    public static int numberOfVariables()
    {
	return counter;
    }

    /**
     * Gibt zu einem Namen das Variablensymbol zurueck (falls es ex.)
     * ansonsten null
     **/
    public static VariableSymbol variableFromName(String vname)
    {
	return (VariableSymbol) usedNames.get(vname);
    }
 
    /**
     * Liefert den Index, nachdem Variablensymbole in einer
     * Hashtabelle gespeichert werden. Verwendet wird die eindeutige
     * ID einer Variablen.
     *
     * @author Andreas
     * @returns int
     **/

    public int hashCode()
    {
	return id;
    }

}

