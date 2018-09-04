/*
 * Datei: Symbol.java
 */

package basis;

/**
 * Symbol ist die gemeinsame Oberklasse von FunctionSymbol und VariableSymbol
 **/

public abstract class Symbol
{
    /**
     * Die Nummer des Symbols
     **/
    protected int id;
    
    /**
     * Der Name des Symbols
     **/
    protected String name;
 
    /**
     * Gibt den Namen der Funktion oder Variablen aus.
     *
     * @return Der Name des Symbols 
     **/
    public String getName()
    {
	return name;
    }

    /**
     * Gibt die id zurueck;
     * Die id-Raeume fuer Funktions- und Variablensymbole sind nicht disjunkt!
     *
     * @return Die id des Symbols
     **/
    public int getId()
    {
	return id;
    }

    /**
     * Gibt eine String-Repraesentation des Symbols aus;
     * Der Einfachkeit halber ist das der Name des Symbols.
     **/
    public String toString()
    {
	return name;
    }

    /**
     * Fuegt eine String-Repraesentation des Symbols an einen StringBuffer an
     **/
    public void addToStringBuffer(StringBuffer sb)
    {
	sb.append(name);
    }

    /**
     * Gibt an, ob das Symbol ein Variablensymbol ist
     **/
    public abstract boolean isVariable();

    /**
     * Symbole sind gleich, wenn beide vom selben Typ sind
     * und die selbe ID haben
     **/
    public boolean equals(Symbol aSymb)
    {
	return (isVariable() == aSymb.isVariable()) 
	    && (getId() == aSymb.getId());
    }

}
