/*
 * Datei: Signature.java
 */

package basis;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Eine Signatur beinhaltet eine Menge von Funktionssymbolen.
 * Wir beschraenken uns auf Signaturen mit nur einer Sorte, per
 * default "ANY"
 **/
public class Signature
{	
    /**
     * Die Menge der Funktionssymbole in einer HashMap mit Eintraegen 
     * der Form name->FunctionSymbol
     **/
    protected HashMap functionSymbols;

    /**
     * Die in der Spezifikation verwendete Sorte
     **/
    protected Sort sort;
  
    /**
     * Konstruktor: Erzeugt ein neues Signaturobjekt mit der vorgegebenen
     * Sorte s.
     *
     * @param s Die Sorte, die unser Universum darstellt
     **/
    public Signature(Sort s)
    {
	functionSymbols = new HashMap();
	sort            = s;
    }
    
    /**
     * Erzeugt ein neues Signaturobjekt mit einer default-Sorte ("ANY").
     **/
    public Signature()
    {
	functionSymbols = new HashMap();
	sort            = new Sort("ANY");
    }

    /**
     * Fuegt der Signatur ein bereits bestehendes Funktionsobjekt hinzu,
     * oder wirft eine AlreadyDefinedException, falls es bereits ein 
     * Funktionssymbol dieses Namens gibt  
     *
     * @param f Jenes Funktionsobjekt, das der Signatur hinzugefuegt
     * werden soll.
     *
     * @exception AlreadyDefinedException
     **/
  
    public void addFunction(FunctionSymbol f)
	throws AlreadyDefinedException
    {
	if (functionSymbols.get(f.getName()) != null)
	    throw new AlreadyDefinedException(f);
	functionSymbols.put(f.getName(),f);
    }
  
    /**
     *	Sucht in der Signatur nach einem Funktionsobjekt mit dem vorgegebenen
     *  Namen.
     *
     *	@param name Der Name der gesuchten Funktion.
     *
     *	@return Das gesuchte Funktionsobjekt mit dem Namen name, 
     *     falls es in der Signatur enthalten ist, ansonsten null
     **/
    
    public FunctionSymbol getFunction(String name)
    {
	return (FunctionSymbol)functionSymbols.get(name);
    }	
    
    /**
     * Liefert eine Aufzaehlung aller in der Signatur enthaltenen 
     * Funktionsobjekte.
     *
     * @return Ein Iterator ueber alle enthaltenen Funktionssymbole
     **/
  
    public Iterator allFunctions( )
    {
	return functionSymbols.values().iterator( );
    }
    
    /**
     * Liefert eine textuelle Repraesentation der Signatur als Zeichenkette.
     * 
     * TODO: - Es sollte auch noch die Sorte ausgegeben werden.
     *       - Schoeneres Layout
     *       - Umstellen auf StringBuffer
     **/
    
    public String toString( )
    {
	return "SIGNATUR:" + " F = " + functionSymbols.toString();
    }	
}
