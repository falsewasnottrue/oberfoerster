package visual;

import java.awt.*;
import java.awt.event.*;
import basis.*;
import java.io.*;

/**
 * MatchView visualisiert die Schritte eines Matches.
 * Besteht aus 2 TermViews, einer Anzeige fuer die Substitution,
 * sowie einem Textfeld.
 **/

public class MatchView extends UnificationView
{
    
    public boolean visualizeEvent(VisualEvent event)
    {
	
	if (event.getID()== VisualEvent.MATCH) {
	    term1.resetToDefaults();
	    term2.resetToDefaults();
	    term1.setTerm ((Term)event.getParams().get(0));
	    term2.setTerm ((Term)event.getParams().get(1));
	    term1.repaint();
	    term2.repaint();
	    subst.setSubst ((Substitution)event.getParams().get(2));
	    return false;
	};
	if (event.getID()== VisualEvent.MATCH_TOPSYMBOLE_VGL) {
	    term1.clearTopSymbolSelection();
	    term1.selectTopSymbol((Term)event.getParams().get(0));
	    term2.clearTopSymbolSelection();
	    term2.selectTopSymbol((Term)event.getParams().get(1));
	    term1.repaint();
	    term2.repaint();
	    subst.select(-1);
	    processSubstSelection();
	};
	if (event.getID()== VisualEvent.MATCH_SUBST_ERW) {
	    term1.termChanged();
	    term2.termChanged();
	    subst.substChanged();
	    subst.select(subst.getItemCount() -1);
	    processSubstSelection();
	};
	if (event.getID()== VisualEvent.MATCH_ENDE) {
	    if ( ((Boolean)event.getParams().get(0)).booleanValue() == true) {
		term1.setBGColor(new Color(0,155,0));
		term1.setVarBGColor(new Color(200,255,200));
		term1.setColor (Color.white);
		term2.setColor (Color.white);
		term2.setBGColor(new Color(0,155,0));
		term2.setVarBGColor(new Color(200,255,200));
	    } else {
		term1.setBGColor(Color.red);
		term1.setColor (Color.white);
		term2.setBGColor(Color.red);
		term2.setColor (Color.white);
		term1.setVarBGColor (new Color(255,200,200));
		term2.setVarBGColor (new Color(255,200,200));
	   }
  	    term1.termChanged();
	    term2.termChanged();
	};

	return true;
    }
   
}
    
