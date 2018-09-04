/**
 * Datei Equation.java
 *
 * @author Andreas Kohlmaier
 **/

package basis;

import java.util.Collection;

/** 
 * Diese Klasse repraesentiert eine ungerichtete Gleichung.
 * Im Sysmtem werden Gleichungen durch 2 entgegengesetzt
 * gerichtete Regeln repraesentiert.
 * Deswegen haben Gleichungen Zeiger auf ihre Partner Regel.
 **/
public class Equation extends TermPair
{
    private TermPair partner;
    
    public Equation (TermPair tp)
    {
	this(tp.getLeft(),tp.getRight());
    }

    public Equation (TermPair tp, TermPair aPartner)
    {
	this(tp.getLeft(),tp.getRight(),aPartner);

    }

    /**
     * Erzeugt neue Gleichung, deren Partner auf NULL zeigt
     **/
    public Equation (Term left, Term right)
    {
	this(left,right,null);
    }

    /**
     * Erzeugt eine neue Gleichung mit Zeiger auf die 
     * Partnerregel
     **/
    public Equation (Term left, Term right, TermPair aPartner)
    {
	super(left,right);
	partner=aPartner;
	Protocol.notifyCreation(this);
    }

    public boolean isEquation()
    {
	return true;
    }


    /**
     * liefert den Partner zurueck
     **/
    public TermPair getPartner()
    {
	return partner;
    }

    /**
     * setzt den Partner
     **/
    public void setPartner (TermPair aPartner)
    {
	partner=aPartner;
    }

}
