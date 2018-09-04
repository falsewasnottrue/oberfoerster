package visual;

import java.awt.*;
import java.util.Iterator;
import java.awt.event.*;
import basis.*;


/**
 * Diese Klasse repraesentiert eine Liste, in der eine Substitution
 * angezeigt wird
 **/

public class ActiveFactSetView extends List
{


    /** 
     * Defaultwerte fuer Liste initialisieren
     **/
    public void initialize()
    {
	setMultipleMode(false);

    }
    
  

    public ActiveFactSetView()
    {
	super();
	initialize();
    }
  public void setChanged()
    {
	      
       if (getItemCount()>0)    removeAll();
	TermPair aFact;
	Iterator aFacts = Oberfoerster.getActiveFacts().elements();
	while (aFacts.hasNext()){ 
       	    aFact = (TermPair)aFacts.next();
	    add(aFact.toString());
	    
	}
    }
    public void setChanged(TermPair tp)
    {
	      
       if (getItemCount()>0)    removeAll();
	TermPair aFact;
	Iterator aFacts = Oberfoerster.getActiveFacts().elements();
	while (aFacts.hasNext()){ 
       	    aFact = (TermPair)aFacts.next();
	    add(aFact.toString());
	    if (aFact.isEqual(tp)) select(getItemCount()-1);
	}
    }	


       }
