
/*
 * Datei: LPO.java
 *
 * date: 14.05.99
 * @author Daniel
 */

package basis;
import java.util.*;
import visual.Visual;
import visual.VisualEvent;
import visual.TreeViewNode;
import visual.TreeView;
import visual.Path;

/**
 * LPO implementiert eine lexikographische Pfadordnung
 **/
public class LPO extends TermOrdering
{
    private Precedence prec;
    public TreeView tv;
    public TreeViewNode ttvn;


    /**
     * Konstruktor: Erzeugt eine LPO mit der vorgegebenen Praezedenz
     *
     * @param prec Die zu verwendende Praezedenz
     **/
    public LPO(Precedence preced)
    {
	prec = preced;
    }
    
 
    /*
     * Fall (alpha) aus der LPO-Definition
     * liefert true, falls die Bedingungen erfuellt werden.
     */
    protected boolean alpha(Term s, Term t, Hashtable results, TreeViewNode tvn) { // Ein si majorisiert t
	Iterator s_subterms = s.getSubterms(); 
	TreeViewNode test;
	while (s_subterms.hasNext()) { // Suche ein majorisierendes si
	    Term s_sub = (Term) s_subterms.next();
	    test = new TreeViewNode("a", s_sub+" majorisiert "+t, tv);	   
	    if (subCompareTerm( s_sub, t, results, test ) == IS_GREATER) {
		tvn.add(test);
		return true;
	    }
	}
	return false;
    }


    /*
     * Fall (beta) aus der LPO-Definition
     * liefert true, falls die Bedingungen erfuellt werden.
     */
    protected boolean beta(Term s, Term t, Hashtable results, TreeViewNode tvn ) {
	int prectest = prec.compareTerm(s, t); // Praezedenzbedingung
	if (prectest==IS_GREATER) {
	    TreeViewNode test = new TreeViewNode("b",s +" > "+t, tv);
	    boolean res = major(s, t, results, test); 
	    if (res) {
		tvn.add(test);
	    }
	    return res;
	}
	else {
	    return false;
	}

    }

    /*
     * Lexikographischer Vergleich der Elemente zweier Iteratoren.
     * liefert true, falls der lex. Vergleich positiv ausfaellt.
     * rekursiv. Sind die zwei momentan vorderen Terme equal, so die naechsten rekursiv.
     * Abbruchbedingung: keine Terme mehr da: =,..,=   gibt false
     *                                                 gibt false
     *                                        =,..,=,< gibt false
     *                                        =,..,=,> gibt true
     *                                        =,..,=,# gibt false.
     */
    protected boolean lexcompare( Iterator s_sub, Iterator t_sub, Hashtable results, TreeViewNode tvn ) {
	if ( s_sub.hasNext() && t_sub.hasNext() ) { // gibt es zu vergleichende Elemente ?
	    // Dann ergleiche die Beiden
	    Term s = (Term) s_sub.next();
	    Term t = (Term) t_sub.next();
	    int res = (int) subCompareTerm (s, t, results, tvn ); 
	    if ( res == IS_EQUAL )  { // Sind sie gleich ? dann die naechsten Beiden ...
		//TreeViewNode node = new TreeViewNode("*",s+" = "+t,tv);
		if ( s_sub.hasNext() && t_sub.hasNext() ) { // falls es sie gibt.
		    boolean lexres = lexcompare(s_sub, t_sub, results, tvn);
		    //  if (lexres) {
		    //tvn.add(node);
		    //}
		    return lexres;
		}
		return false; // wenn es keine gibt: lex. Vergleich negativ. 
	    } 
	    else { // Die anderen Faelle, die beim Vergleich auftreten koennen
		if ( res == IS_GREATER ) { // Sind sie in der >-Rel. , dann positiv.
		    //  TreeViewNode node = new TreeViewNode("*",s+" > "+t,tv);
		    //  tvn.add(node);
		    return true;
		}
		return false; // sonst
	    }
	}
	return false;  // falls es keine Zwei Terme gibt:
    }//lexcompare


    /* Majorisierungsbedingung fuer Faelle (beta) und (gamma)
     * s >lpo tj fuer alle j
     * liefert true, falls erfuellt.
     */
    protected boolean major( Term s, Term t, Hashtable results, TreeViewNode tvn ) {//s >LPO tj alle j   
	boolean subtest = true;
	Iterator t_subterms = t.getSubterms(); 
	TreeViewNode test = new TreeViewNode("");
	while ( t_subterms.hasNext() && subtest ) {
	    Term t_sub = (Term) t_subterms.next();
	     test = new TreeViewNode(s+" > "+t_sub,tv);
	    subtest = subtest && (subCompareTerm( s, t_sub, results, test ) == IS_GREATER);
	    if (subtest) {
		tvn.add(test);
	    }
	}
	return subtest; 
    } 


    /*
     * Fall (gamma) aus der LPO-Definition
     * liefert true, falls die Bedingungen erfuellt werden.
     */
    protected boolean gamma(Term s, Term t, Hashtable results, TreeViewNode tvn ) {
	// Praezedenzbedingung
	int prectest = prec.compareTerm(s, t); 
	if (prectest==IS_EQUAL) {
	    TreeViewNode majortest = new TreeViewNode("M",s+" majorisiert "+t, tv);
	    if ( major(s,t,results, majortest) ) {
		
		// wenn major dann auch lexikograph. Vergleich auf Subtermen: s1..sm >*LPO t1..tn  
      		Iterator t_subterms = t.getSubterms();
		Iterator s_subterms = s.getSubterms();
		TreeViewNode lextest = new TreeViewNode("*","LexComp", tv);
		boolean res = lexcompare( s_subterms, t_subterms, results, lextest );
		if (res) {
		    TreeViewNode node = new TreeViewNode("g",s+" > "+t, tv);
		    node.add(majortest);
		    node.add(lextest);
		    tvn.add(node);
		}
		return res;
	    }
	}
	return false; //Falls an einem der Tests (prec. oder major) gescheitert.
    }

    /*
     * Fall (delta) aus der LPO-Definition
     * liefert true, falls die Bedingungen erfuellt werden.
     */
    protected boolean delta(Term s, Term t, TreeViewNode tvn) {
	if ( t.getTopSymbol().isVariable() ) { // ist Toplevel-Symbol von t ein Variablensymbol ?
	    if ( s.containsVariable( (VariableSymbol)  t.getTopSymbol() ) && !s.isEqual( t ) ) {
		// Baue den Baum auf: > steht hier fest. 
		TreeViewNode node = new TreeViewNode("d",s+" > "+t);
		tvn.add(node);
		return true; // falls t in Var(s) , aber auch t!=s ?
	    }
	}                                     
	return false; // sonst 
    }

    /**
     * Vergleicht zwei Terme bezueglich der Reduktionsordnung.
     *
     * @param s wird mit t verglichen 
     * @param t wird mit s verglichen
     * @return Ergebnis des Vergleichs s ~ t bezueglich der Reduktionsordnung
     *         Wertebereich: IS_LESS, IS_EQUAL, IS_GREATER, IS_UNCOMPARABLE
     **/
    protected int compareTerm(Term s, Term t) {
	Hashtable results = new Hashtable();	// Neue Hashtabelle anlegen

	TreeViewNode root = new TreeViewNode("Teste:",s+" > "+t, tv);
	ttvn = root; // Hebe mir den Knoten auf.
	tv = new TreeView(root);

	

	String key = s.toString() + t.toString();
	// Schluessel festlegen: Vielleicht nicht sehr guenstig so.	
	int result = subCompareTerm(s, t, results, ttvn);

	tv.fill();
	Visual.notify(VisualEvent.newOrdTest(tv, new Boolean(result==IS_GREATER)));
	return result;
    }

    /*
     * liefert true, falls s >lpo t
     * false sonst.
     */
    protected boolean subCompareTermGreater( Term s, Term t, Hashtable results, TreeViewNode tvn) {
	// Vergleicht nur auf IS_GREATER
	// nur dieses wird visualisiert !!
	if ( beta(s, t, results, tvn ) ) { 
	    return true; 
	} 
	if ( gamma(s, t, results, tvn ) ) { 
	    return true; 
	}
	if ( delta(s, t, tvn ) ) { 
	    return true; 
	}
        if ( alpha(s, t, results, tvn ) ) { 
	    return true; 
	}
	return false;
    }//subCompareTermGreater

    /*
     * liefert true, falls s = t
     * false sonst.
     */
    protected boolean subCompareTermEqual( Term s, Term t, Hashtable results, TreeViewNode tvn ) {
	// Vergleicht nur auf IS_EQUAL
	// so vielleicht nicht richtig. s = t gdw s<t und s>t ???
	boolean res = s.isEqual( t );
	if (res) {
	    TreeViewNode test = new TreeViewNode("=", s+" = "+t);
	    tvn.add(test);
	}
	return res;
    }

    /*
     * liefert true, falls s <lpo t
     * false sonst.
     */
    protected boolean subCompareTermLess( Term s, Term t, Hashtable results, TreeViewNode tvn ) {
	// Vergleicht nur auf IS_LESS
	return subCompareTermGreater( t, s, results, tvn );
    }


    /*
     * Vergleicht s mit t durch aufruf der subCompareTerm* und liefert 
     * Ergebnisse IS_LESS, IS_EQUAL, IS_GREATER oder IS_UNCOMPARABLE.
     * Speichert Ergebnisse bereits erfolgter Vergleiche in Hashtable zwischen.
     */
    protected int subCompareTerm(Term s, Term t, Hashtable results, TreeViewNode tvn) {
	// erst vergleichen, wenn feststeht, 
	// dass der Vergleich noch nicht existiert.
	int res = IS_UNCOMPARABLE;
	// Schluessel fuer Hashtable ?
	String key = s.toString() + t.toString();
	// Gibt es den gesuchten Vergleich schon ?
	if ( results.containsKey( key ) ) {
	    TreeViewNode test = new TreeViewNode("^");
	    return ( (Integer) results.get( key ) ).intValue();
	}
	else { // Sonst vergleichen... 
	    if ( subCompareTermEqual( s, t, results, tvn  ) ) {
		res = IS_EQUAL;
	    }
	    else {
		if ( subCompareTermGreater( s, t, results, tvn ) ) {
		    res = IS_GREATER;
		}
		else {
		    if ( subCompareTermLess( s, t, results, tvn ) ) {
			res = IS_LESS;
		    }
		}// sonst IS_UNCOMPARABLE (Startwert)
	    } 
	    results.put( key, new Integer( res ) );
	    return res;
	}
    }// subCompareTerm


}// LPO






