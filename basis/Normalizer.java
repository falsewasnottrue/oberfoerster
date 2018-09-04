/*
 * Datei : Normalizer.java
 *
 * 13.05.99
 *
 * @author Juri
 */

package basis;
import visual.Visual;
import visual.VisualEvent;
import java.util.Iterator;

public class Normalizer 
{    /**
     * versucht den Term mit dem übergebenen aktiven Faktum zu normalisieren.
     *
     * @param term der zu normalisierende Term
     * @param tp das aktive Faktum, mit dem normalisiert werden soll
     *
     * @return true, falls mit tp reduziert werden konnte, sonst false
     **/


    public boolean normalize(Term term, TermPair tp)
    {
	//	System.out.println("Normalisiere "+term+" mit Termpaar "+tp);
	boolean flag = false; // zeigt an, ob tatsaechlich mit tp normalisiert wurde
	Term subterm, r;
	Substitution sigma;
	Term t=(Term)term.clone();
	// Durch den Term gehen
	Iterator subterms = term.getAllSubterms();
	int n=0;
	while (subterms.hasNext()){
	    subterm = (Term)subterms.next();	  
	    //System.out.println("Normalisiere Subterm: "+subterm);
	    try {
		// matche subterm mit linker Teil der Regel
		sigma = Oberfoerster.getMatcher().match(tp.getLeft(),subterm);

		r = sigma.appliedOn( tp.getRight() );

		if ( tp.isRule() || (Oberfoerster.getTermOrdering().compareTerm(subterm,r)==TermOrdering.IS_GREATER) ) {
		    subterm.replaceWith(r);  //ersetze Teilterm durch sigma(r)
		    //System.out.println("Erfolg; neuer Term: "+term);
		    //System.out.println("Substitution: "+sigma);
		    
		    flag=true; 
       		    Visual.notify(VisualEvent.newNormalize_with(t,term,tp,n));
		return flag;}
	    }
	    catch (NoMatchFoundException e) { }
	  n++;
	}

	//System.out.println("Fertiger Term: "+term);

	return flag;
    }


 public boolean reduziere(Term term, TermPair tp)
    {
	//System.out.println("Normalisiere "+term+" mit Termpaar "+tp);
	boolean flag = false; // zeigt an, ob tatsaechlich mit tp normalisiert wurde
	Term subterm, r;
	Substitution sigma;
	try {
	    // matche term mit linker Teil der Regel
	  
	    sigma = Oberfoerster.getMatcher().match(tp.getLeft(),term);

	    r = sigma.appliedOn( tp.getRight() );

	    if ( tp.isRule() || (Oberfoerster.getTermOrdering().compareTerm(term,r)==TermOrdering.IS_GREATER) ) {
		term.replaceWith(r);  //ersetze Term durch sigma(r)
		
       	       flag=true;}
	}
	catch (NoMatchFoundException e) { }
       

    //System.out.println("Fertiger Term: "+term);

	return flag;
}

    /** 
     * Methode, die eine Normalform vom term bildet bezueglich einer 
     * Regelmenge oder einer Gleichungmenge
     *
     * @param term der zu normalisierende Term
     **/
public void normalize (Term term)
{  
    boolean reduceable = true;
    TermPair aFact;
    Term t=(Term)term.clone();
    Term subterm;
    // Man versuche zu normalisieren bis keine Matchs mehr moeglich sind
    boolean vis=false;
    
    while ( reduceable ) {
	reduceable = false;
	//ganzen Term durchlaufen
	Iterator subterms = term.getAllSubterms();
        int n=0;
      	while ((subterms.hasNext())&&(!reduceable)){
	    subterm = (Term)subterms.next();	  
	    //  aktive Fakten durchlaufen
	    Iterator aFacts = Oberfoerster.getActiveFacts().elements(subterm);
	    int ind = 0;
	    Term tt=(Term)term.clone();
	    while ((aFacts.hasNext())&&(!reduceable)){
		aFact = (TermPair)aFacts.next();
		reduceable = reduceable || reduziere(subterm, aFact);
		if (reduceable){
		Visual.notify(VisualEvent.newNormalize_with(tt,term,aFact,n));
	}	vis=true;
		
       		ind++;
	    }
	    n++;
	} 
    }
    //	System.out.println("Schicke Nachricht an Visual");
    //System.out.println("Normalform von "+t.toString()+" ist "+term.toString()); 
    if (vis) Visual.notify(VisualEvent.newNormalize(t,term));
} //normalize

 
    /**
     * normalisiert das Termpaar
     *
     * @param tp das zu normalisierende Termpaar
     **/
    public void normalize(TermPair tp)
    {
	//System.out.println("==Normalisiere Termpaar==");
	//System.out.println(tp);
	normalize(tp.getLeft());
	normalize(tp.getRight());
	//System.out.println("==fertig==");
    }


 public boolean normal(Term term, TermPair tp)
    {
	//	System.out.println("Normalisiere "+term+" mit Termpaar "+tp);
	boolean flag = false; // zeigt an, ob tatsaechlich mit tp normalisiert wurde
	Term subterm, r;
	Substitution sigma;

	// Durch den Term gehen
	Iterator subterms = term.getAllSubterms();
	while (subterms.hasNext()){
	    subterm = (Term)subterms.next();	  
	    //System.out.println("Normalisiere Subterm: "+subterm);
	    try {
		// matche subterm mit linker Teil der Regel
		sigma = Oberfoerster.getMatcher().match(tp.getLeft(),subterm);

		r = sigma.appliedOn( tp.getRight() );

		if ( tp.isRule() || (Oberfoerster.getTermOrdering().compareTerm(subterm,r)==TermOrdering.IS_GREATER) ) {
		    subterm.replaceWith(r);  //ersetze Teilterm durch sigma(r)
		    //System.out.println("Erfolg; neuer Term: "+term);
		    //System.out.println("Substitution: "+sigma);
		    
		    flag=true; 
       		return flag;}
	    }
	    catch (NoMatchFoundException e) { }
	}

	//System.out.println("Fertiger Term: "+term);

	return flag;
    }

} //class
