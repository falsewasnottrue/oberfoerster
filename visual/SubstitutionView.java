package visual;

import java.awt.*;
import java.util.Iterator;

import basis.Substitution;
import basis.VariableSymbol;
import basis.Term;

/**
 * Diese Klasse repraesentiert eine Liste, in der eine Substitution
 * angezeigt wird
 **/
public class SubstitutionView extends List
{
    /**
     * Zeiger auf die Substitution, die angezeigt wird
     **/
    private Substitution subst;

    public Substitution getSubst()
    {
	return subst;
    }

    /** 
     * Defaultwerte fuer Liste initialisieren
     **/
    public void initialize()
    {
	setMultipleMode(true);
    }

    public VariableSymbol varAtIndex(int anIndex)
    {
	Iterator it = subst.boundVars();
	VariableSymbol var;
	int count = getItemCount() -1;
	while (it.hasNext()) { 
	    var = (VariableSymbol)it.next();
	    //	    System.out.println("Suche : "+anIndex+" bei "+count+"  " +subst.getBinding(var));
	    if (count-- == anIndex)
		return var;
	}
	return null;
    }

    public Term termAtIndex(int anIndex)
    {
	Iterator it = subst.boundVars();
	VariableSymbol var;
	int count = getItemCount() -1;
	while (it.hasNext()) { 
	    var = (VariableSymbol)it.next();
	    //	    System.out.println("Suche : "+anIndex+" bei "+count+"  " +subst.getBinding(var));
	    if (count-- == anIndex)
		return subst.getBinding(var);
	}
	return null;
    }
    /**
     * Wenn sich die Substitution geaendert hat, muss diese
     * Methode aufgerufen werden, um die Liste neu aufzubauen.
     **/
    public void substChanged()
    {
	if (getItemCount() > 0) removeAll();
	Iterator it = subst.boundVars();
	VariableSymbol var;
	while (it.hasNext()) {
	    var= (VariableSymbol)it.next();
	    add (getString (var,subst.getBinding(var)),0);
	}
	repaint();
    }

    public void setSubst(Substitution aSubst)
    {
	subst = aSubst;
	substChanged();
    }

    public SubstitutionView()
    {
	super();
	initialize();
    }
 
    public String getString (VariableSymbol var, Term t)
    {
	return var.toString() + " <- " + t.toString();
    }
	
}
