/*
 * Datei: Substitution.java
 */

package basis;

import java.util.Iterator;
import java.util.HashMap;
import java.io.Reader;
import java.io.FileReader;

/** Die Klasse Substitution repraesentiert eindeutige Zuordnungen von 
 *  Variablen zu Termen, also eine Abbildung von VariableSymbol -> Term.
 **/
public class Substitution
{
    /**
     * Substitutionen werden in einer HashMap
     * VarSymb -> Term gepspeichert
     **/
    private HashMap bindings;
    
    /**
     * Konstruktor, der eine leere Substitution liefert
     **/
    public Substitution()
    {
	// HashMap wird erst angelegt, wenn sie gebraucht wird.
	// bindings=new HashMap();
    }
  
    /**
     * Konstruktor, der eine einfache Substitution {var <- term} liefert
     *
     * @param var ein Variablensymbol
     * @param term ein Term
     **/
    public Substitution(VariableSymbol var, Term term) 
    {
	addOrReplace(var,term);
    }
  
    /**
     * Fuegt term als Substitution fuer var hinzu, also die Ersetzung
     * var <- term. Existiert bereits eine Substitution fuer var, so
     * wird eine entsprechend Exception geworfen
     *
     * @param var Zuzufuegendes VariableSymbol
     * @param term Zuzufuegender Term
     *
     * @exception BindingAlreadyEstablishedException
     **/
    public void add(VariableSymbol var, Term term) 
	throws BindingAlreadyEstablishedException
    {
	if (bindings == null) bindings = new HashMap (3);
	if (isBound(var)){
	    throw new BindingAlreadyEstablishedException(var,this);
	}
	
	// System.out.println("Fuege "+var.toString()+"<-"+term.toString()+" hinzu.");
	// Substitution in HashMap aufnehmen
	addOrReplace(var,term);
    }


  
    /**
     * Fuegt term als Substitution fuer var hinzu, also die Ersetzung
     * var <- term. Existiert bereits eine Substitution fuer var, so
     * wird diese ueberschrieben.
     * Variable und Term werden vor dem Einfuegen geclont.
     *
     * @param var Zuzufuegendes VariableSymbol
     * @param term Zuzufuegender Term
     **/
    public void addOrReplace(VariableSymbol var, Term term) 
    {
	if (bindings == null) bindings = new HashMap (3);
	bindings.put(var,term); 
    }
  
    /**
     * Wie add, aber die Substitution wird idempotent gemacht, d.h. die 
     * Aenderung wird auch auf alle schon vorhandenen x <- t angewandt
     *
     * @param var Zuzufuegendes VariableSymbol
     * @param term Zuzufuegender Term
     *
     * @exception BindingAlreadyEstablishedException
     **/
    public void addIdempotent(VariableSymbol var, Term term) 
	throws BindingAlreadyEstablishedException
    {
	if (bindings == null) bindings = new HashMap (3);
	// alle gebunden Variablen durchgehen
	Iterator vars = boundVars();
	while (vars.hasNext()){
	    // und Substitution auf Bindung anwenden
	    replaceVariable(var,term,getBinding((VariableSymbol)vars.next()));
	}
	add(var,term);
    }

    /**
     * Ersetzt eine Variable durch einen Term, in einem anderen Term
     * also fuehrt die Ersetzung x <- t in s durch
     *
     * @param var Das zu ersetzende Variablensymbol
     * @param term Der Term durch den var ersetzt werden soll
     * @param into Der Term in dem var ersetzt werden sol
     **/
    protected void replaceVariable(VariableSymbol var,Term term,Term into)
    {
	// Hauptiterator geht alle Subterme durch
	Iterator subterms = into.getAllSubterms();
	VariableSymbol x;
	Term subterm;
	while (subterms.hasNext()){
	    subterm = (Term)subterms.next();
	    // ist Subterm Variable ?
	    if (subterm.isVariable()){
		x=(VariableSymbol)subterm.getTopSymbol();
		// und die richtige ?
		if (x.equals(var)){ 
		    // dann Termersetzung durchfuehren
		    subterm.replaceWith(term);
		}
	    }
	}
    }

    /**
     * Gibt an, ob das Variablensymbol v gebunden ist
     *
     * @param v Das Variablensymbol
     *
     * @return true, falls v gebunden ist, sonst false
     **/
    public boolean isBound(VariableSymbol v)
    {	
	if (bindings == null) return false;
	return bindings.containsKey(v);
    }

    /**
     * Liefert einen Iterator mit allen gebundenen Variablen
     **/
    public Iterator boundVars()
    {
	if (bindings == null) bindings = new HashMap (3);
	return bindings.keySet().iterator();
    }

    /**
     * Liefert den Term, durch den die uebergebene VariableSymbol durch die
     * Substitution ersetzt werden soll.
     *
     * @param var zu ersetzendes VariableSymbol
     * @return Ersetzender Term, falls die VariableSymbol von der Substitution 
     * betroffen ist, sonst null.
     **/

    public Term getBinding(VariableSymbol var) 
    {
	if (bindings == null) bindings = new HashMap (3);
	return (Term)bindings.get(var);
    }

    /**
     * Wendet die Substitution auf den uebergebenen Term destruktiv an.
     *
     * @param term Der zu veraendernde Term
     **/
    public void apply(Term term) 
    {
	//	System.out.print("Wende " + this + " auf " + term);
	// Hauptiterator geht alle Subterme durch
	if (bindings == null) bindings = new HashMap (3);
	Iterator subterms = term.getAllSubterms();
	Term subterm;
	// Zum Zwischenspeichern der Variable, um sich das dauernde
	// casten zu ersparen ...
	VariableSymbol var;
	while (subterms.hasNext()){
	    subterm = (Term)subterms.next();
	    // ist Subterm Variable ?
	    if (subterm.isVariable()){
		var=(VariableSymbol)subterm.getTopSymbol();
		// ist Variable in Substitution enthalten ?
		if (isBound(var)){
		    // dann Termersetzung durchfuehren
		    subterm.replaceWith(getBinding(var));
		}
	    }
	}
	// System.out.println(" an und erhalte : " + term);
    }
    
    /**
     * Wendet die Substitution auf beide Terme eines Termpaars destruktiv an
     *
     * @param tp Das zu veraendernde Termpaar
     **/
    public void apply(TermPair tp)
    {
	apply(tp.getLeft());
	apply(tp.getRight());
    }

    public Term appliedOn(Term term)
    {
	Term t2 = (Term)term.clone();
	apply(t2);
	return t2;
    }


    /**
     * Liefert String-Repraesentation der Substitution.
     * Benutzt wird dabei die rekursive Funktion addToStringBuffer.
     *
     * @return String
     **/
    public String toString()
    {
	StringBuffer sb   = new StringBuffer();
	addToStringBuffer(sb);
	return sb.toString();
    }

    /**
     * Fuegt String-Repraesentation an StringBuffer an.
     *
     * @param sb Stringbuffer, an den der String angefuegt wird
     **/
    public void addToStringBuffer (StringBuffer sb)
    {
	if (bindings == null) bindings = new HashMap (3);
	Iterator vars = boundVars();
	sb.append('{');
	while (vars.hasNext()){
	    VariableSymbol var = (VariableSymbol) vars.next();
	    var.addToStringBuffer(sb);
	    sb.append(" <- ");
	    getBinding(var).addToStringBuffer(sb);
	    if (vars.hasNext())
		sb.append(", ");
	}
	sb.append('}');
    }
}

