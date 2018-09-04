package visual;

import java.awt.*;
import java.awt.event.*;
import basis.*;
import java.io.*;
import java.util.Iterator;
/**
 * NormalizerView visualisiert die Schritte einer Normalisierung.
 * Besteht aus 2 TermViews, einem TextFeld fuer die aktive Fakten  ,
 * 
 **/

public  class NormalizerView extends EventView implements ItemListener
{
    // Das Aussehen vor Normaisierung
    public TermView before;
    // Normalform
    public TermView after;
    // Aktive Fakten als Liste
    private ActiveFactSetView aFacts;
    // Info
    private Label info;

   
    public NormalizerView()
    {
	before=new TermView();
	after=new TermView();
	ScrollPane sp = new ScrollPane();
	sp.add(before);
	add(sp);
	ScrollPane sp2 = new ScrollPane();
	sp2.add(after);
       	add(sp2);
       
	info = new Label();
	info.setAlignment(Label.CENTER);
	info.setText("Hier passiert was !");
	add(info);
	
	aFacts = new ActiveFactSetView(); 
       	aFacts.addItemListener(this);
	add(aFacts);

	// Layout festlegen
	GridBagLayout layout = new   GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();


       	c.fill=GridBagConstraints.BOTH;
       	c.weightx = 1.0;
	c.weighty = 1.0;
       	c.gridx = 0;
	c.gridy = 0;
	
      	layout.setConstraints(sp,c);

	c.gridx=0;
	c.gridy=1;
	layout.setConstraints(sp2,c);
	
	c.weightx = 0.4;
	c.gridx=1;
	c.gridy=0;
	//c.gridwidth=2;
	c.gridheight=GridBagConstraints.RELATIVE;
	layout.setConstraints(aFacts,c);

	c.weightx = 0.4;
	c.weighty = 0.0;
	c.gridx=0;
	c.gridy=2;
	c.gridheight=GridBagConstraints.RELATIVE;
	c.gridwidth=GridBagConstraints.REMAINDER;
	layout.setConstraints(info,c);
	setLayout(layout);
    }
     public void itemStateChanged(ItemEvent e) {
	versuche_mit(aFacts.getSelectedIndex());

    }

    /**
     * versucht den reduzierten Term mit i-ten Regel, die der Benutzer 
     * mit der Maus auswaehlt, weiter zu reduzieren
     * und setzt entsprechend info, ob der Term reduzierbar, oder nicht
     **/
public void versuche_mit(int i)
    {
	TermPair aFact=null;
       	Iterator aF = Oberfoerster.getActiveFacts().elements();
	while (i>=0){ 
       	    aFact = (TermPair)aF.next();
	    i--;
	}
       	boolean b=Oberfoerster.getNormalizer().normal(after.getTerm(),aFact);
	if (b) { info.setText(" Damit kann der untere Term reduziert werden");
	         
	       }
	else { info.setText(" Damit kann der untere Term nicht reduziert werden");
	        }
	repaint();
    }

 public boolean visualizeEvent(VisualEvent event)
    {
       	if (event.getID() == VisualEvent.NORMALIZE)
	    {
		Term t1=(Term)event.getParams().get(0);
		Term t2=(Term)event.getParams().get(1);
		if(t1.isEqual(t2)){
		    before.setTerm (t1);
		    after.setTerm (t2);
		    aFacts.setChanged();
		    info.setText(" Dieser Term und seine Normalform   sind gleich ");
		}
		else {
		    before.setTerm (t1);
		    after.setTerm (t2);
		    aFacts.setChanged();
		    info.setText(" Normalform des oberen Termes bezueglich den aktiven Fakten ist unten");}
	
	    }
if (event.getID() == VisualEvent.NORMALIZE_WITH)
             {  
       		 Term t=(Term)event.getParams().get(0);
		 before.setTerm (t);
		 after.setTerm ((Term)event.getParams().get(1));
		 aFacts.setChanged((TermPair)event.getParams().get(2));	
		//	System.out.println(((Integer)event.getParams().get(2)).intValue());
		// 	aFacts.select(((Integer)event.getParams().get(2)).intValue());
		 int n=((Integer)event.getParams().get(3)).intValue();
	       	before.selectSubterm(t.getSubtermAn(n));
		before.selectTopSymbol(t.getSubtermAn(n));
		 info.setText("Oberer Term wurde mit der markierten Regel reduziert");
		before.repaint();
	       	after.repaint();
	      	aFacts.repaint();
		info.repaint();
	    }
return true; 
    }

    /**
     * main Methode zu Testzwecken. 
     **/
    public static void main (String [] argv)	
    {
	try {
	    Parser parser = new Parser (new FileReader (argv[0]));
      	    XSpec xspec = parser.parseXSpec();
	    TermPair tp = xspec.spec.getEquations()[0];
	    NormalizerView nv = new NormalizerView ();
	    nv.before.setTerm(tp.getLeft());
	    nv.after.setTerm(tp.getRight());
	    Frame fram = new Frame();
	    fram.setSize(640,480);
	    fram.add(nv);
	    fram.setVisible(true);
	} 
    	catch (Exception e){
	    System.err.println ("Exception: " + e);
	    e.printStackTrace();
	}
    }

}
