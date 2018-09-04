package visual;
import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import java.util.Iterator;
import java.io.FileReader;
import basis.*;

/**
 * Dient dazu einen, Subterm anzuzeigen.
 * Funktioniert nur mit @ref TermView zusammen
 **/

public class SubtermView extends Component{

    /** 
     * zu zeichnender Term
     **/
    private Term term;
    /**
     * alle dazugehoerigen SubtermViews
     **/
    private Vector subtermViews; 
    /**
     * das topSymbol des Terms wird zwischengespeichert
     **/
    private String topSymbol;
    /**
     * Zeiger auf den TermView, in dem das SubtermView enthalten ist
     **/
    private TermView parent;
    /**
     * Ausdehnung des subTerms in Pixeln
     **/
    private Dimension size;

    /**
     * Erzeugt neuen SubtermVIew mit einem Zeiger auf den 
     * Vater.
     **/
    public SubtermView(Term t,TermView aParent)
    {
	parent = aParent;
	term = t;
	Iterator it = t.getSubterms();
	subtermViews = new Vector();
	topSymbol=term.getTopSymbol().toString();
    	// Subterme durchgehen
	while (it.hasNext()){
	    subtermViews.add ( new SubtermView ((Term)it.next(),aParent));
	}
    }
    
    
    /**
     * Berechnet die Ausdehnung des Views
     **/
    public Dimension calculateDimension()
    {
	// Wenn keine Soehne da sind, einfach Textgrossen nehmen
	if (subtermViews.isEmpty()){
	    size = getBoxSize();
	    // ein bisschen Abstand zwischen zwei Teiltermen lassen
	    size.width += parent.getFont().getSize() + 5;	
	} else {
	    // Breiten und Hoehen der Subterme addieren
	    Iterator subs = subtermViews.iterator();
	    size = new Dimension (0,0);
	    while (subs.hasNext()){
		Dimension subsize = ((SubtermView)subs.next()).calculateDimension();
		// Breiten addieren
		size.width += subsize.width;
		// Maximum der Hoehen suchen
		if (subsize.height > size.height)
		    size.height = subsize.height;
	    }
	    // Platz zwischen zwei Stufen mitberechnen
	     size.height += getStepHeight();
	}

	return size;
    }

    /**
     * Liiefert die Hoehe einer Stufe zurueck
     **/
    private int getStepHeight()
    {
	return getBoxSize().height + parent.getFont().getSize() * 2;
    }

    /**
     * Liefert die Groesse der Box, die das TopSymbol umschliesst
     **/
    public Dimension getBoxSize ()
    {
	return new Dimension (getFontMetrics(parent.getFont()).stringWidth(topSymbol) + parent.getFont().getSize() * 2,
			  getFontMetrics(parent.getFont()).getHeight());
			  
    }


    /**
     * Zeichnet das TopSymbol in einer evtl. schattierten Box.
     * Falls das topSymbol selektiert ist, wird ein roter Rahmen mit
     * 5 pix Breite drumrum gezeichnet
     **/
    public void paintTopSymbol(int x,int y, Graphics g)
    {
	Dimension boxSize = getBoxSize();
	// Schatten zeichnen
       	if (parent.hasShadow()){
	    g.setColor(Color.black);
	    g.fillRect(x+2-boxSize.width/2,y+2,boxSize.width,boxSize.height);
	};

	// Selektierung
	if (parent.isSelectedTopSymbol(term)){
	    g.setColor(Color.red);
	    g.fillRect(x-3-boxSize.width/2,y-3,boxSize.width+6,boxSize.height+6);
	}

	if (term.isVariable()){
	    g.setColor (parent.getVarBGColor());
	} else {
	    g.setColor(parent.getBGColor());
	};
     	g.fillRect(x-boxSize.width/2,y,boxSize.width,boxSize.height);
   	if (term.isVariable()){
	    g.setColor (parent.getVarColor());
	} else {
	    g.setColor(parent.getColor());
	};
	g.drawString(topSymbol,
		     x-g.getFontMetrics().stringWidth(topSymbol)/2,
		     y+boxSize.height*3/4);

    }
   
    /**
     * Zeichnet den Term auf die uebergebene Position und Graphicskontext
     **/
    public void paint (Point pos, Graphics g)
    {
	// g.drawRect (pos.getX(),pos.getY(),getMySize().getX(),getMySize().getY());
	// Wenn dieser Subterm selektiert ist, wird er grau hinterlegt
	if (parent.isSelectedTerm(term)){
	    g.setColor (Color.lightGray);
	    g.fillRect (pos.x+3,pos.y-5,getMySize().width-3,getMySize().height+10);
	    g.setColor (Color.black);
	}

	// Subterme werden eine Stufe tiefer gezeichnet
	Point subPos = new Point (pos.x,pos.y+getStepHeight());
       	Iterator subs = subtermViews.iterator();

	while (subs.hasNext()){
	    SubtermView sub = (SubtermView)subs.next();
	    // Subterm zeichnen
	    sub.paint (subPos,g);
	    // Linie zeichnenno
	    g.setColor (Color.black);
	    g.drawLine (pos.x+size.width / 2,pos.y+getBoxSize().height, 
			subPos.x + sub.getMySize().width / 2, subPos.y);
	    // Position fuer naechsten Subtermberechnen
	    subPos.x += sub.getMySize().width;
	}
	// TopSymbol in die Mitte der gesamten Breite zeichnen
	paintTopSymbol(pos.x + getMySize().width / 2,pos.y,g);
	    
    }

    public Dimension getMySize()
    {
	if (size == null) { size = new Dimension (0,0); };
	return size;
    }

    
    /**
     * Liefert den Term, in dessen Box sich der uebergebene Punkt
     * befindet, oder NULL falls keine Box getroffen wurde
     **/
    public Term hitsTerm(Point mousePoint,Point pos)
    {
	Term hit = null;
	// Subterme werden eine Stufe tiefer gezeichnet
	Point subPos = new Point (pos.x,pos.y+getStepHeight());
       	Iterator subs = subtermViews.iterator();
	
	while (subs.hasNext()){
	    SubtermView sub = (SubtermView)subs.next();
	    // Subterm pruefen
	    hit = (sub.hitsTerm (mousePoint,subPos));
	    if (hit != null) return hit;
	    // Position fuer naechsten Subtermberechnen
	    subPos.x += sub.getMySize().width;
	}
	// eigene Box ueberpruefen
	return hitsMe(mousePoint,pos.x + getMySize().width / 2,pos.y);
    }

    private Term hitsMe(Point mousePoint,int x, int y)
    {
	Dimension boxSize = getBoxSize();
	Rectangle  r= new Rectangle(x-boxSize.width/2,y,boxSize.width,boxSize.height);
	if (r.contains(mousePoint)) 
	    return term;
	else
	    return null;
    }

    public void print()
    {
	System.out.println (term);
   	Iterator subs = subtermViews.iterator();
	while (subs.hasNext()){
	    System.out.print ("--");
	    ((SubtermView)subs.next()).print();
	}
	
    }

    public Term getTerm()
    {
	return term;
    }
  
}
