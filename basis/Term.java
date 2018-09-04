/*
 * Datei: Term.java
 */

package basis;

import java.util.Vector;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.EmptyStackException;

/** 
 * Term stellt einen Term dar (Ach nee...) ; das kann eine Variable oder ein
 * Term der Form f(t1,...,tn) sein, wobei der Fall n=0 moeglich ist (der
 * Term ist dann eine Konstante)
 **/
public final class Term 
{
    /**
     * Das Toplevel-Symbol
     **/
    private Symbol topSymbol;
    
    /**
     * Feld mit Subtermen.
     * Invariante: subTerms == null  gdw  Term ist Variable oder Konstante
     * also ist nie subTerms.length == 0
     **/
    private Term[] subTerms;

    /**
     * Konstruktor mit einer Variablen als Argument.
     *
     * @param v Das Variablensymbol, das Toplevelsymbol des neuen Terms ist.
     **/
    public Term(VariableSymbol v)
    {
	topSymbol = v;
	subTerms  = null;
    }

    /**
     * Konstruktor mit einem Funktionssymbol und einem Vector als Argument.
     * Der Vector sollte die Subterme enthalten, wird aber nicht(!) 
     * ueberprueft. Falls die Laenge des Vectors nicht mit der Stelligkeit
     * des Funktionssymbols uebereinstimmt, wird eine ArityMismatchException
     * geworfen.
     *
     * @param f Das Funktionssymbol, das Toplevelsymbol des neuen Terms ist
     * @param ts Der Vektor mit den Subtermen
     *
     * @exception ArityMismatchException
     **/
    public Term(FunctionSymbol f, Vector ts)
	throws ArityMismatchException
    {
	int size = (ts == null) ? 0 : ts.size();
	if (f.getArity() != size)
	    throw new ArityMismatchException(f.getArity(), ts.size());
	topSymbol = f;
	if (size > 0) {
	    subTerms = new Term[size];
	    for (int i = 0; i< size; i++)
		subTerms [i] = (Term) ts.elementAt(i);
	}
    }

    /**
     * Konstruktor mit einem Funktionssymbol und einem Feld von Teiltermen.
     * Das Feld wird kopiert! Die einzelnen Eintraege werden nicht ueberprueft!
     * Falls die Laenge des Arrays nicht mit der Stelligkeit des 
     * Funktionssymbols uebereinstimmt, wird eine ArityMismatchException
     * geworfen.
     *
     * @param f Das Funktionssymbol, das Toplevelsymbol des neuen Terms ist
     * @param ts Das Array mit den Subtermen
     *
     * @exception ArityMismatchException
     **/
    public Term(FunctionSymbol f, Term[] ts)
	throws ArityMismatchException
    {
	if (f.getArity() != ts.length)
	    throw new ArityMismatchException(f.getArity(), ts.length);

	topSymbol = f;
	if (ts.length == 0)
	    subTerms = null;
	else
	    subTerms  = (Term []) ts.clone();
    }

    /**
     * Konstruktor mit nur einem Funktionssymbol; das sollte dann eine
     * Konstante sein, also Stelligkeit 0 haben, ansonsten wird eine
     * ArityMismatchException geworfen
     *
     * @param f Ein Funktionssymbol
     *
     * @exception ArityMismatchException
     **/
    public Term(FunctionSymbol f)
	throws ArityMismatchException
    {
	if (f.getArity() != 0)
	    throw new ArityMismatchException(f.getArity(), 0);

	topSymbol = f;
	subTerms  = null;
    }

    /**
     * Konstruktor mit einem bereits vorhandenen Term, der rekursiv 
     * kopiert wird
     *
     * @param t Der zu kopierende Term
     **/
    public Term(Term t)
    {
	topSymbol = t.topSymbol;
	int a = t.numberOfSubterms();
	if (a == 0)
	    subTerms = null;
	else {
	    subTerms = new Term[a];
	    for(int i = 0; i < a; i++)
		subTerms[i] = new Term(t.subTerms[i]); // Rekursion!
	}
    }
    
    /**
     * Liefert eine tiefe Kopie dieses Terms mittels des Kopierkonstruktors
     * (d.h. alle Subterme werden auch kopiert)
     *
     * @return Eine Kopie dieses Terms 
     **/
    public Object clone()
    {
	return new Term(this);
    }
    
    /**
     * Prueft, ob der Term eine Variable ist
     *
     * @return true, wenn es sich um eine Variable handelt, sonst false.
     **/
    public  boolean isVariable()
    {
	return topSymbol.isVariable();
    }
    
    /**
     * Gibt das Toplevel-Symbol aus
     *
     * @return Das Toplevel-Symbol
     **/
    public Symbol getTopSymbol()
    {
	return topSymbol;
    }
    
    /**
     * Gibt die Anzahl der direkten Teilterme zurueck. 
     *
     * @return Die Anzahl der Teilterme
     **/
    public int numberOfSubterms()
    {
	return subTerms == null ? 0 : subTerms.length;
    }
    
    /**
     * Gibt eine Aufzaehlung der Subterme aus
     *
     * Verwendet eine anonyme innere Klasse. Java-Oberstufe!!
     *
     * @return Ein Iterator ueber die direkten Teilterme (genauer: Eine 
     *  Instanz einer anonymen Klasse, die das Interface Iterator 
     *  implementiert)
     **/
    public Iterator getSubterms()
    {
	return new Iterator () {  
	    int i = 0;
	    int a = numberOfSubterms();

	    public boolean hasNext() 
	    {
		return i < a;
	    }

	    public Object next()
		throws NoSuchElementException 
	    {
		if (!(i < a))
		    throw new NoSuchElementException();
		return subTerms[i++];
	    }
	    public void remove() {/* do nothing */}
	};
    }

    /**
     * gibt eine Aufzaehlung aller Subterme aus (incl. sich selber)
     * auch mit anonymer innerer Klasse
     *
     * @return Ein Iterator ueber alle Teilterme
     **/
    public Iterator getAllSubterms()
    {
	return new Iterator (){
	    Stack s;
	    {
		s = new Stack();
		s.push(Term.this);
	    }
	    public boolean hasNext() 
	    {
		return !s.isEmpty();
	    }
	    public Object next()
		throws NoSuchElementException 
	    {
		try {
		    Term t = (Term) s.pop();
		    for (int i = t.numberOfSubterms() - 1; i >= 0; i--)
			s.push(t.subTerms[i]);
		    return t;
		}
		catch (EmptyStackException e){
		    throw new NoSuchElementException();
		}
	    }
	    public void remove() {/* do nothing */} 
	};
    }
    
    /**
     * Liefert eine Aufzaehlung aller Symbole zurueck;
     * verwendet den Iterator getAllSubterms 
     *
     * @return Ein Iterator ueber alle (Funktions- und Variablen-) Symbole
     **/
    public Iterator getAllSymbols()
    {
	return new Iterator () {
	    Iterator i = getAllSubterms();
	    public boolean hasNext() 
	    {
		return i.hasNext();
	    }
	    public Object next()
		throws NoSuchElementException 
	    {
		return ((Term)i.next()).topSymbol;
	    }
	    public void remove() {/* do nothing */}
	};
    }

    /**
     * Prueft, ob der Term den gleichen Aufbau wie der als Parameter
     * uebergeben Term hat. 
     * Verwendet einen der Iteratoren. Bei Effizienproblemen muesste
     * man evtl. eine Fassung ohne Iteratoren verwenden.
     *
     * Es geht entscheidend ein, dass die Iteratoren die Reihenfolge
     * einhalten!
     *
     * @param term der zu vergleichende Term
     * @return true, falls die beiden Terme den gleichen Aufbau haben,
     * ansonsten false
     **/
    public  boolean isEqual(Term t)
    {
	Iterator i1 = getAllSymbols();
	Iterator i2 = t.getAllSymbols();
	while (i1.hasNext() && i2.hasNext())
	    if (i1.next() != i2.next())
		return false;
	return i1.hasNext() == i2.hasNext();
    }

    /**
     * Prueft, ob Variable im Term vorkommt.
     *
     * Verwendet einen der Iteratoren
     *
     * @return true, falls Variable enthalten ist, false sonst.
     **/
    public  boolean containsVariable(VariableSymbol v)
    {
	Iterator i = getAllSymbols();
	while (i.hasNext())
	    if (i.next() == v)
		return true;
	return false;
    }

    /**
     * Liefert String-Repraesentation des Terms.
     *
     * @return String
     **/
    public String toString()
    {
	StringBuffer sb = new StringBuffer();
	addToStringBuffer(sb);
	return sb.toString();
    }

    /**
     * String Repraesentation an einen StringBuffer anfuegen.
     *
     * Der Effizienz halber ohne Iteratoren!
     *
     * @param sb Stringbuffer, an den die String-Repraesentation des Terms
     *  angehaengt wird
     **/
    public void addToStringBuffer(StringBuffer sb)
    {
	topSymbol.addToStringBuffer(sb);

	int a = numberOfSubterms();

	if (a > 0){
	    sb.append('(');
	    subTerms[0].addToStringBuffer(sb);
	    for (int i = 1; i < a; i++){
		sb.append(',');
		subTerms[i].addToStringBuffer(sb);
	    }
	    sb.append(')');
	}
    }

    /**
     * Ersetzt den Term mit einem anderen Term t, indem topSymbol
     * und Subterme von t uebernommen werden
     *
     * @param t neuer Term
     **/
    
    public void replaceWith (Term t)
    {
	// Zeiger auf neuen Term umbiegen
	topSymbol = t.getTopSymbol();
	subTerms = t.getSubtermArray();
    }

    /**
     * Liefert die Subterme als Array zurueck
     **/

    public Term[] getSubtermArray()
    {
	return subTerms;
    }

   /**
    * Ersetzt einen Subterm an einer gegeben Stelle im Term
    *
    * @param p die Stelle an der ersetzt werden soll
    * @param t der Term durch den ersetzt werden soll
    **/
   public void replaceSubterm(int p, Term t) throws InvalidPlaceException
   {
	Iterator s = getAllSubterms();
        int _p = 0;
	Term foo = (Term)s.next();

        while ( s.hasNext() && p!=_p ) {
          foo = (Term)s.next();
	  _p++;
	}

	if (p==_p) foo.replaceWith(t);
	      else throw new InvalidPlaceException();
   }

    /**
     * gibt n-ter Element vom getAllsubterms
     *
     * @param n index
     **/
    public Term  getSubtermAn(int n)
    {
	Iterator it = getAllSubterms();
	int i=0;
	Term st=null;
	while (it.hasNext() &&(i<n)){ st = (Term)it.next();i++;}
           
	if (i==n) return (Term)it.next();
         else return null;
	   
    }
    
 
}
