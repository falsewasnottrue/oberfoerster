/**
 * Unifier.java
 *
 *
 * @author Andreas
 */

package basis;

import visual.Visual;
import visual.VisualEvent;

import java.util.Iterator;
import java.io.FileReader;


/**
 * Der Unifikationsalgorithmus
 **/
public class Unifier
{    
    private Term term1;
    private Term term2;

    /**
     * Fuegt ggF. eine neue Bindung in die Substitution ein
     * und wendet sie auf die zu unifizierenden Terme an
     **/
    private void establishBinding(VariableSymbol var, Term term,Substitution subst)
	throws NoUnificationFoundException, BindingAlreadyEstablishedException
    {
	// occur check
   	if (!term.containsVariable(var)){
	    // Subst erweitern
	    subst.addIdempotent(var,term);
	    // und gleich anwenden
	    subst.apply(term1);
	    subst.apply(term2);
	    // Visualisierer benachrichtigen
	    Visual.notify(VisualEvent.newUnifikationSubstErweitern(var,term));
	} else {
	    // Wenn term auch Variable und var in term enthalten,
	    // muss term == var sein und alles ist ok
	    // ansonten schlaegt occur check zu.
	    if (!term.isVariable()) 
		throw new NoUnificationFoundException(term1,term2);
	}
    }
    
    /**
     * leistet die eingenliche Arbeit.
     * Unifiziert zwei Subterme
     **/
    private void unifySubterms(Term subTerm1,Term subTerm2,Substitution subst)
	throws BindingAlreadyEstablishedException, NoUnificationFoundException
    {
	// Visualisierer benachrichtigen
	Visual.notify(VisualEvent.newUnifikationTopVgl(subTerm1,subTerm2));

	// Sind beide Terme Funktionen ?
	if (!subTerm1.isVariable() && !subTerm2.isVariable()){
		// Wenn Funktionssymbol nicht gleich, dann Ende
		if (!subTerm1.getTopSymbol().equals(subTerm2.getTopSymbol()))
		    throw new NoUnificationFoundException(term1,term2);
		// Bei gleichen Funktionssymbolen Subterme untersuchen
		else {
		    Iterator subTerms1 = subTerm1.getSubterms();
		    Iterator subTerms2 = subTerm2.getSubterms();
		    while (subTerms1.hasNext()){
			unifySubterms((Term)subTerms1.next(),(Term)subTerms2.next(),subst);
		    } //while
		} //else
	    }
	// eine von beiden ist Variable
	else { 	  
	    if (subTerm1.isVariable())
		establishBinding((VariableSymbol)subTerm1.getTopSymbol(),subTerm2,subst);
	    if (subTerm2.isVariable())
		establishBinding((VariableSymbol)subTerm2.getTopSymbol(),subTerm1,subst);
	}
    }


    /**
     * unifiziert zwei Terme
     *
     * @param term1 der eine Term
     * @param term2 der andere Term
     *
     * @returns Substitution sigma mit sigma(Term1) = sigma(Term2)
     * @exceptions NoUnificationFoumdException
     **/
    public Substitution unify(Term t1, Term t2)
	throws NoUnificationFoundException
    {
	// Timer starten
	Protocol.notifyStartUnification();

	// erstmal leere Substitution erzeugen
	Substitution subst=new Substitution();
	// globales Unifikationsziel Festelegen
	term1=(Term)t1.clone(); 
	term2=(Term)t2.clone();

	// Visualisierer benachrichtigen
	Visual.notify(VisualEvent.newUnifikationStart(term1,term2,subst));

	// Und Rekursion starten
	try { unifySubterms(term1,term2,subst); } 
	catch (BindingAlreadyEstablishedException e) 
	    { throw new NoUnificationFoundException(term1,term2); }

       
	// wenn alles klappt, Substitution zurueckgeben.
	Protocol.notifyStopUnification();
	// Visualisierer benachrichtigen
	Visual.notify(VisualEvent.newUnifikationEnde(Boolean.FALSE));
	return subst;
   }// unify
    

    /**
     * main-Methode fuer Testzwecke
     **/
    public static void main (String [] argv){
	Unifier uf = new Unifier();
	Protocol.initProtocol(System.out);
	try {
	    Parser parser = new Parser (new FileReader (argv[0]));
	    XSpec xspec = parser.parseXSpec();
	    TermPair tp = xspec.spec.getEquations()[0];
	    Substitution s;
	    System.out.println("Versuche " + tp + " zu unifizieren.. ");
	    s=uf.unify(tp.getLeft(),tp.getRight());
	    System.out.println("Ergebnis :" + s);
	    System.out.println("altes Termpar : " + tp);
	    s.apply(tp);
	    System.out.println("Wende Substitution an : "+tp);
	    System.out.println(s);
	}
	catch (Exception e){
	    System.err.println ("Exception: " + e);
	    e.printStackTrace();
	}
   }
} // Unifier
