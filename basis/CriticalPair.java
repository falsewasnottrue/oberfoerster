/**
 * Datei CriticalPair.java
 *
 * @author Andreas Kohlmaier
 **/

package basis;

public class CriticalPair extends TermPair
{
    /** 
     * Zeiger auf Eltern
     **/
    private TermPair[] parents;

    public CriticalPair (TermPair tp,TermPair mom,TermPair dad)
    {
	this(tp.getLeft(),tp.getRight(),mom,dad);
    }

    public CriticalPair (Term left,Term right,TermPair mom,TermPair dad)
    {
	super(left,right);
	parents = new TermPair[2];
	parents[0]=mom;
	parents[1]=dad;
	
    }

    /**
     * Liefert die eine ElternRegel zurueck
     **/
    public TermPair getMom()
    {
	return parents[0];
    }

    /**
     * Liefert die andere ElternRegel zurueck
     **/
    public TermPair getDad()
    {
	return parents[1];
    }
    
    /**
     * Liefert true, falls es sich um ein Axiom handelt
     **/
    public boolean isAxiom()
    {
	return ((getMom()==null) && (getDad()==null));
    }
}
