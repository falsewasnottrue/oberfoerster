package visual;

import java.awt.*;
import java.awt.event.*;
import basis.*;
import java.io.*;
import java.util.Iterator;

/**
 * UnificationView visualisiert die Schritte einer Unifikation.
 * Besteht aus 2 TermViews, einer Anzeige fuer die Substitution,
 * sowie einem Textfeld.
 **/

public class UnificationView extends EventView
    implements ItemListener
{    
    public TermView term1;
    public TermView term2;

    protected SubstitutionView subst;

    public UnificationView()
    {
	initialize();
    }
    
    /** 
     * Initialisiert UnificationView 
     **/
    public void initialize()
    {
	term1=new TermView();
	term2=new TermView();
	subst = new SubstitutionView();

	// Fuegt termViews in ScrollPanes eingebettet ein
	Container cont = new Container();
	cont.setLayout (new GridLayout (1,2));
	ScrollPane sp = new ScrollPane();
	sp.add(term1);
	cont.add(sp);
	sp = new ScrollPane();
	sp.add(term2);
	cont.add(sp);

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.weightx = 1.0;
	gbc.weighty = 1.0;

	gbl.setConstraints(cont,gbc);
	add(cont);
	
	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridheight = GridBagConstraints.REMAINDER;
	gbc.weightx = 1.0;
	gbc.weighty = 0.5;
	subst.addItemListener(this);
	gbl.setConstraints(subst,gbc);
	add (subst);
	setLayout(gbl);
    }

    public boolean visualizeEvent(VisualEvent event)
    {
	
	if (event.getID()== VisualEvent.UNIFIKATION_START) {
	    term1.resetToDefaults();
	    term2.resetToDefaults();
	    term1.setTerm ((Term)event.getParams().get(0));
	    term2.setTerm ((Term)event.getParams().get(1));
	    term1.repaint();
	    term2.repaint();
	    subst.setSubst ((Substitution)event.getParams().get(2));
	    return false;
	};
	if (event.getID()== VisualEvent.UNIFIKATION_TOPSYMBOLE_VERGLEICHEN) {
	    term1.clearTopSymbolSelection();
	    term1.selectTopSymbol((Term)event.getParams().get(0));
	    term2.clearTopSymbolSelection();
	    term2.selectTopSymbol((Term)event.getParams().get(1));
	    term1.repaint();
	    term2.repaint();
	    subst.select(-1);
	    processSubstSelection();
	};
	if (event.getID()== VisualEvent.UNIFIKATION_SUBST_ERWEITERN) {
	    term1.termChanged();
	    term2.termChanged();
	    subst.substChanged();
	    subst.select(subst.getItemCount() -1);
	    processSubstSelection();
	};
	if (event.getID()== VisualEvent.UNIFIKATION_ENDE) {
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

    protected void processSubstSelection()
    {
	term1.clearSubtermSelection();
	term2.clearSubtermSelection();
	for (int i = 0; i < subst.getItemCount(); i++)
	    if (subst.isIndexSelected(i)){
		selectTerm (subst.termAtIndex(i),term1);
		selectTerm (subst.termAtIndex(i),term2);
	    }

	term1.repaint();
	term2.repaint();
    }

    protected void selectTerm (Term what, TermView view)
    {
	Iterator it = view.getTerm().getAllSubterms();
	Term st;
	while (it.hasNext()){
	    st = (Term)it.next();
	    if ((what.getTopSymbol() == st.getTopSymbol()) &&
		(what.getSubtermArray() == st.getSubtermArray()) &&
		!(what == st)){
		view.selectSubterm(st);
	    }
	}
    }


    public void itemStateChanged(ItemEvent e) {
	processSubstSelection();
    }

    /**
     * main Methode zu Testzwecken. Zeigt die linke 
     * und rechte Seite der ersten Gleichung einer Spezifikation an.
     **/
    public static void main (String [] argv)	
    {
	try {
	    Parser parser = new Parser (new FileReader (argv[0]));
      	    XSpec xspec = parser.parseXSpec();
	    TermPair tp = xspec.spec.getEquations()[0];
	    UnificationView uv = new UnificationView ();
	    uv.term1.setTerm(tp.getLeft());
	    uv.term2.setTerm(tp.getRight());
	    Frame fram = new Frame();
	    fram.setSize(300,300);
	    fram.add(uv);
	    fram.setVisible(true);
	} 
    	catch (Exception e){
	    System.err.println ("Exception: " + e);
	    e.printStackTrace();
	}
    }
}
    
