package visual;

import java.util.Vector;
import java.awt.*;
import java.awt.event.*;

import basis.*;
import java.io.*;
import java.util.Iterator;


/**
 * Dient dazu, einen kompletten Term dazustellen.
 * Hier ist es moeglich, Schriftart, Schrtiftgrosse,
 * Vordergrund und Hintergrundfarbe einzustellen.
 * Hier wird eine List von Topsymbolen, sowie eine Liste von Subtermen
 * angegeben, die selektiert werden.
 * Das eigentliche zeichnen der Terme uebernehmen die 
 * SubtermViews.
 *
 * Es bietet sich an ein TermView immer in ein ScrollPane einzubinden ...
 **/
public class TermView extends Panel implements ActionListener{
    /**
     * SubtermView, das den gesamten Term zeichnet
     **/
    private SubtermView termView;
    /**
     * Menge von Subtermen, die selektiert sind
     **/
    private Vector selectedSubterms;
    /** 
     * Menge von Subtermen, deren TopSymbole selektiert sind
     **/
    private Vector selectedTopSymbols;

    /**
     * die OffScreen Surface, die zum zeichnen verwendet wird
     **/
    private Image offScreen;

    /**
     * hier wird zwischengespeichert, wie gross das TermView ist
     **/
    private Dimension size = new Dimension (100,100);

    /**
     * Textfarbe fuer Funktionen
     **/
    private Color color;
    /**
     * Texthintergrundfarbe fuer Funktionen
     **/
    private Color bgColor;
   /**
     * Textfarbe fuer Variablen
     **/
    private Color varColor;
    /**
     * Texthintergrundfarbe fuer Variablen
     **/
    private Color varBgColor;
    /**
     * Schriftart und groesse
     **/
    private Font font;
    /**
     * Textboxen mit Schatten
     **/
    private boolean shadow;
    /**
     * dieses Popupmenu enthaelt die Funktionen zum Zoomen
     **/
    public PopupMenu popup;

    /**
     * Legt ein TermView ohne Inhalt mit den Defaultwerten an
     **/
    public TermView()
    {
	initialize();
	resetToDefaults();
	termView = null;
    }
    
    /** 
     * Setzt wieder die StandardWerte:
     * Schriftart : Helvetica, normal, 12 pt
     * Hintergrundfarbe : Gelb
     * Vordergrundfarbe : Schwarz
     * Schatten : Ja
     * Zeichnet nicht neu !
     **/
    public void resetToDefaults()
    {
  	// Standardwerte setzten
	setFont (new Font ("Helvetica",Font.PLAIN,12));
	setBGColor (Color.yellow);
	setColor (Color.black);
	setVarBGColor (Color.white);
	setVarColor (Color.black);
	setShadow (true);
    }

    /**
     * Informiert den TermView, dass sich der angezeigte
     * Term geaendert hat, wahrscheinlich durch Anwendung einer
     * Substitution.
     * Berechnet neue Ausdehnung und zeichnet sich neu.
     * Diese Methode soll immer dann aufgerufen werden, wenn das TermView
     * neu gezeichnet werden soll.
     **/
    public void termChanged()
    {
	// Bewirkt, dass ein neuer SubtermView fuer den alten
	// Term erzeugt wird.
	setTerm (termView.getTerm());
    }

    /**
     * Erzeugt ein TermView fuer den uebergebenen Term.
     **/
    public TermView(Term t)
    {
	initialize();
	resetToDefaults();
	setTerm(t);
    }

    /**
     * Initialisiert das TermView
     **/
    public void initialize()
    {	    
	// Selektierungsmengen initialisieren
	selectedSubterms = new Vector();
	selectedTopSymbols = new Vector();
	// MouseListener einbauen
	addMouseListener (new MyMouseListener());
       	// PopupMenu einbauen
	constructPopup();
	add(popup);
    }

    /**
     * Aendert den angezeigten Term und zeichnet sich neu
     **/
    public void setTerm(Term t)
    {
	// SubtermView fuer Term erzeugen
	termView = new SubtermView (t,this);
	termView.calculateDimension();
	size = new Dimension(termView.getMySize().width+10,termView.getMySize().height+10);
	offScreen = createImage (size.width+10,size.height+10);
	setSize(size.width,size.height);
	repaint();
    }

    /**
     * Liefert Zeiger auf den Term zurueck, der angezeigt wird
     **/
    public Term getTerm()
    {
	return termView.getTerm();
    }

  
    /**
     * Zeichnet rekursiv die SubtermViews.
     * Zeichnet zuerst auf ein offscreen image und flippt dann.
     **/
    public void paint(Graphics g)
    {
	if (offScreen == null)
	    offScreen = createImage (size.width+10,size.height+10);

	Graphics goff = offScreen.getGraphics();
	goff.setColor (getBackground());
	goff.fillRect(0,0,size.width,size.height);
	goff.setFont(font);
	if (termView != null)
	    termView.paint(new Point(5,5),goff);
	g.drawImage(offScreen,0,0,this);
       	getParent().validate();
	
	    
    }

    /**
     * Legt Schriftart fest
     **/
    public void setFont(Font aFont)
    {
	font = aFont;
    }
   
    /**
     * Legt Textfarbe fest
     **/
    public void setColor(Color aColor)
    {
	color = aColor;
    }
    
    /**
     * Legt Hintergrundfarbe fest
     **/
    public void setBGColor(Color aBGColor)
    {
	bgColor = aBGColor;
    }

    /**
     * Legt Textfarbe fuer Variablen fest
     **/
    public void setVarColor(Color aColor)
    {
	varColor = aColor;
    }
    
    /**
     * Legt Hintergrundfarbe fuer Variablen fest
     **/
    public void setVarBGColor(Color aBGColor)
    {
	varBgColor = aBGColor;
    }

    public void setShadow (boolean aShadow)
    {	
	shadow = aShadow;
    }
  

    public boolean hasShadow()
    {
	return shadow;
    }

    public Font getFont()
    {
	return font;
    }
    
    public Color getBGColor()
    {
	return bgColor;
    }
    
    public Color getColor()
    {
	return color;
    }

    public Color getVarBGColor()
    {
	return varBgColor;
    }
    
    public Color getVarColor()
    {
	return varColor;
    }

    /**
     * a TermView implements a Component with a fixed size.
     * Therefore preferredSize returns the size of the SubtermView
     **/
    public Dimension getPreferredSize()
    {
	return size;
    }

    /**
     * a TermView implements a Component with a fixed size.
     * Therefore minimumSize returns the size of the SubtermView
     **/
    public Dimension getMinimumSize()
    {
	return size;
    }
    
    /**
     * prueft, ob ein Term ausgewaehlt ist
     **/
    public boolean isSelectedTerm (Term t)
    {
	/*	Iterator it = selectedSubterms.iterator();
	Term t1;
	while (it.hasNext()){
	    t1 = (Term)it.next();
	    if ((t1.getTopSymbol() == t.getTopSymbol()) && (t1.getSubtermArray() == t.getSubtermArray()))
		return true;
	}
	return false;*/
	    
	return selectedSubterms.contains(t);
    }

    /**
     * prueft, ob ein topSymbol ausgeawhlt ist.
     **/
    public boolean isSelectedTopSymbol (Term s)
    {
	return selectedTopSymbols.contains(s);
    }
	
    /**
     * fuegt einen Subterm in die Auswahl hinzu
     **/
    public void selectSubterm (Term t)
    {
	selectedSubterms.add(t);
    }
    
    public void deselectSubterm (Term t)
    {
	if (selectedSubterms.contains (t))
	    selectedSubterms.remove(t);
    }

    /**
     * Waehlt das topSymbol des uebergebenen Terms aus
     **/
    public void selectTopSymbol (Term s)
    {
	selectedTopSymbols.add(s);
    }

    public void deselectTopSymbol (Term t)
    {
	if (selectedTopSymbols.contains (t))
	    selectedTopSymbols.remove(t);
    }

    /**
     * Setzt die Auswahl der Subterme zurueck
     **/
    public void clearSubtermSelection()
    {
	selectedSubterms.clear();
    }
    
    /**
     * Setzt die Auswahl der Topsymbole zurueck
     **/
     public void clearTopSymbolSelection()
    {
	selectedTopSymbols.clear();
    }

    /**
     * Baut ein PopUp Menu, das die Funktionen zum Zommen enthaelt.
     **/
    private void constructPopup()
    {
	popup = new PopupMenu();
	MenuItem menuItem = new MenuItem("Zoom in");
      	menuItem.addActionListener(this);
	popup.add(menuItem);
	menuItem = new MenuItem("Zoom out");
       	menuItem.addActionListener(this);
	popup.add(menuItem);
	popup.addSeparator();
    	menuItem = new MenuItem("Normal Size");
       	menuItem.addActionListener(this);
	popup.add(menuItem);
    }

    /**
     * Aendert die Grosse des TermViews, indem die Font
     * um den angegebenen Faktor in Punkten skaliert wird.
     **/
    public void zoom(int delta)
    {
	int newSize = font.getSize() + delta;
	if (newSize < 5) newSize = 5;
	setFont (new Font (font.getName(),font.getStyle(),newSize));
	termChanged();
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equals("Zoom in"))
	    zoom(+2);
	if (e.getActionCommand().equals("Zoom out"))
	    zoom(-2);
	if (e.getActionCommand().equals("Normal Size")){
	    resetToDefaults();   
	    termChanged();
	}
    }

    class MyMouseListener  extends MouseAdapter implements MouseListener {
	public void mousePressed(MouseEvent e) {
	    maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
	    maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
	    if (e.isPopupTrigger()) {
		popup.show(e.getComponent(),
			   e.getX(), e.getY());
	    }
	}
	public void mouseClicked(MouseEvent e) { 
	    // linke Maustaste
	    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK){
		Term t = termView.hitsTerm(e.getPoint(),new Point (5,5));
		if (t!=null){
		    if (isSelectedTopSymbol(t))
			selectSubterm(t);
		    else
			selectTopSymbol(t);
		}
	    }
	   if ((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK){
	       clearTopSymbolSelection();
	       clearSubtermSelection();
	    }

	    repaint();
	}

    } 


    /**
     * main Methode zu Testzwecken. Zeigt die linke Seite
     * der ersten Gleichung einer Spezifikation an.
     **/
    public static void main (String [] argv){
	try {
	    Parser parser = new Parser (new FileReader (argv[0]));
      	    XSpec xspec = parser.parseXSpec();
	    TermPair tp = xspec.spec.getEquations()[0];
	    Term t = tp.getLeft();
	    TermView tv = new TermView(tp.getLeft());
	    ScrollPane sp = new ScrollPane();
	    Panel p = new Panel();
	    sp.add(tv);
	    p.add(sp);
	    
	    Frame fram = new Frame();
	    p.setLayout(new GridLayout(1,1));
	    fram.setSize(300,300);
	    fram.add(p);

	    System.out.println (tp.getLeft());
	    fram.setVisible(true);
	} 
    	catch (Exception e){
	    System.err.println ("Exception: " + e);
	    e.printStackTrace();
	}
    }

}
