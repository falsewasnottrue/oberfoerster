
/*
 * Datei Matcher.java
 *
 * date: 10.05.99
 * @author Daniel
 */

package basis;

import visual.Visual;
import visual.VisualEvent;
import java.util.*;

/**
 * Diese Klasse Matcher enthaelt einen Match-Algorithmus.
 * Die Vorgehensweise ist im Moment: iterativ auf Teiltermen.
 **/

public class Matcher 
{
    public static void main( String[] args )
    throws basis.ArityMismatchException
    { // zu Testzwecken... baut Beispielterm(e) auf...
	try {
	    VariableSymbol v = new VariableSymbol();
	    VariableSymbol x = new VariableSymbol();
	    FunctionSymbol a = new FunctionSymbol("a",0);
	    FunctionSymbol b = new FunctionSymbol("b",0);
	    FunctionSymbol f = new FunctionSymbol("f",2);
	    FunctionSymbol g = new FunctionSymbol("g",1);
	    Term[] ab = new Term [2];
	    Term t1 = new Term( v );
	    Term t2 = new Term( x );
	    Term t3 = new Term( a );
	    Term t4 = new Term( b );
	    ab[0] = t3;
	    ab[1] = t4;
	    Term t5 = new Term(f, ab);
	    Term [] afab = new Term [2];
	    afab[0] = t5;
	    afab[1] = t5;
	    Term t6 = new Term( f, afab );
	    Term[] vfab = new Term [2];
	    vfab[0] = t1;
	    vfab[1] = t2;
	    Term t7 = new Term( f, vfab ); 
	    //f(x,x) x<-g(a)
	    Term[] fxx = new Term[2];
	    fxx[0]=t2;
	    fxx[1]=t2;
	    Term t8 = new Term( f, fxx );
	    Term[] ga = new Term[1];
	    ga[0]=t3;
	    Term t9 = new Term( g, ga);
	    //System.out.println("Vorher: "+t8.toString()); 
	    Matcher s = new Matcher();
	    try {
		System.out.println(t8.toString() + " auf " + t6.toString());
		Substitution sigma = s.match( t8, t6 );
		System.out.println("Ergebnis: Substitution " + sigma.toString() );
		sigma.apply( t8 );
		System.out.println("Gefundener gemeinsamer Term: " +  t8.toString() );
	    }
	    catch ( basis.NoMatchFoundException e) {
		System.out.println("Geht nicht!");
	    }
	}
	catch( basis.NegativeArityException e ) {
	    System.out.println("ERROR");
	}

    }

    /**
     * match liefert einen Match vom ersten uebergebenen Term auf den zweiten, 
     * falls ein solcher Match existiert.
     *
     * @param Term l Ausgangsterm
     * @param Term t Zielterm
     * @returns Substitution sigma mit sigma( l ) = t 
     *
     * @exception NoMatchFoundException
     **/

    public Substitution match(Term l, Term t)
	throws NoMatchFoundException {
	// neue Substitution erzeugen
	Substitution sigma = new Substitution();

	return xmatch(l,t,sigma);
    }
    public Substitution xmatch (Term l, Term t, Substitution sigma)
	throws NoMatchFoundException {
	// Iteratoren ueber alle Teilterme

	Visual.notify(VisualEvent.newMatch(l, t, sigma));
	Iterator t_subterms = t.getAllSubterms();
	Iterator l_subterms = l.getAllSubterms();
	while (l_subterms.hasNext()) {
	    Term l_ = (Term) l_subterms.next();
	    Term t_ = (Term) t_subterms.next();
	    //	    System.out.println("Behandle Teilproblem " + l_.toString() + " mit " + t_.toString() );
	    Visual.notify(VisualEvent.newMatchTopVgl(l_,t_));
	    if ( l_.getTopSymbol() == t_.getTopSymbol() ) {
		if (l_.isVariable()) {
		    // Problemfall: x <- x darf nicht einfach uebersprungen werden
		    Visual.notify(VisualEvent.newMatchSubstErw((Symbol)l_.getTopSymbol(),t_));
		    try {
			sigma.add((VariableSymbol)l_.getTopSymbol(),t_);
		    } 
		    catch (BindingAlreadyEstablishedException e) {
			if (!t_.isEqual( sigma.getBinding((VariableSymbol)l_.getTopSymbol())))
			    throw new NoMatchFoundException(l_,t_,sigma);
		    }
		} //Loesche
		else { // auch loeschen. Die Subterme sind ja im Iterator drin. 
		    //throw new NoMatchFoundException(l_, t_, sigma ); ??
		}
	    }// if
	    else {
		if (!l_.isVariable() ) {
		    Visual.notify(VisualEvent.newMatchEnde(Boolean.FALSE));
		    throw new NoMatchFoundException(l_, t_, sigma );
		}
		else { // fuehre neues Variablensymbol ein, das genau l enthaelt
		    VariableSymbol v = (VariableSymbol) l_.getTopSymbol();
		    // ** Occur Check nicht notwendig
		    

		    /*if (t_.containsVariable ( v )) { // v in Var(t_) : Occur-Check
		      row new NoMatchFoundException(l_, t_, sigma ); 
		      }*/
		    Visual.notify(VisualEvent.newMatchSubstErw(v,t_));
		    try {
			sigma.add( v, t_ );
			// hier muessen jetzt einige Terme uebersprungen werden...
			// naemlich genau alle!  Subterme von t_
			// und zwar im Iterator t_subterms
			Iterator all_t_subterms = t_.getAllSubterms();
			if (all_t_subterms.hasNext()) { all_t_subterms.next(); } // ident. Term ueberspringen
			while ( all_t_subterms.hasNext() ) {
			    all_t_subterms.next();
			    t_subterms.next();
			} // all_t_subterms kann eigentlich nicht mehr Elemente haben als t_subterms.
		    }
		    catch ( BindingAlreadyEstablishedException e) { // falls an gleichen Term gebunden wuerde
			if (t_.isEqual( sigma.getBinding( v ) )) { // Loesche..
			    // Ueberspringe Subterme
			    Iterator all_t_subterms = t_.getAllSubterms();
			    if (all_t_subterms.hasNext()) { all_t_subterms.next(); } // ident. Term ueberspringen
			    while ( all_t_subterms.hasNext() ) {
				all_t_subterms.next();
				t_subterms.next();
			    } // all_t_subterms kann eigentlich nicht mehr Elemente haben als t_subterms.
			}
			else {
			    Visual.notify(VisualEvent.newMatchEnde(Boolean.FALSE));
			    throw new NoMatchFoundException(l_, t_, sigma );
			}
		    }
		}// else
	    }// else
	}// while
	return sigma;
    }// match

}// Matcher
