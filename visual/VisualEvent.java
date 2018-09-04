package visual;

import java.util.Vector;
import java.util.Iterator;
import basis.*;

/**
 * Ein Event, enthaelt alle Informationen die noetig sind, um einen
 * Schritt zu visualisieren.
 *
 * Diese Informationen sind ungetypte Zeigen auf Objekte, die in einem
 * Vector gespeichert werden.
 * Jedes Event hat eine eindeutige ID.
 * Ueber diese ID kann der Name ermittelt werden.
 *
 * Fuer jedes dieser Events steht eine Klassenmethode zur Verfuegung
 * die ein entsprechendes Event erzeugt und die Parameter entsprechend fuellt.
 *
 * z.B. Event.newUnificationEvent (Term1,Term2)
 **/

public class VisualEvent 
{
    // Die folgende Auflistung erhebt keinen Anspruch auf Vollstaendigkeit.
    // Falls noetig kann sie gerne ergaenzt oder veraendert werden.
    // ABER: Dabei ist folgendes zu beachten:
    //  1. Es duerfen keine Konflikte auftreten. Es gibt genuegend Integers fuer alle Events!
    //  2. Die Konstanten sollen DICHT gestreut sein, d.h. nahe beieinander liegen
    //  3. Die Konstante numberOfEvents auf den neusten Stand bringen
    //  4. Die folgendes Personen moechten bei Aenderungen benachrichtigt werden;
    //     - Rasmus Hofmann
    //
    // Noch was: Fuer manche Events gibt es 2 Konstanten, z.B. NORMALIZE und NORMALIZE2. Das ist
    // nur wichtig fuer interne Zwecke von HierarchyTree. Beim Erzeugen von Events bitte immer nur
    // die erste Konstante benutzen!
    public static final int CLOOP = 0; // VERVOLLSTAENDIGUNGSSCHLEIFE
    public static final int CSTEP = 1; // VERVOLLSTAENDIGUNGSSCHRITT
    public static final int OBERSTES_AUSWAEHLEN = 2;
    public static final int ELTERN_VORHANDEN = 3;
    public static final int NORMALIZE = 4;
    public static final int NORMALIZE_WITH = 5;
    public static final int MATCH = 6;
    public static final int MATCH_TOPSYMBOLE_VGL = 7;
    public static final int MATCH_SUBST_ERW = 8;
    public static final int MATCH_ENDE = 38;
    public static final int ORD_TEST = 9;
    public static final int REDUZIEREN = 10;
    public static final int ZUSFBKT_TEST = 11;
    public static final int RICHTEN = 12;
    public static final int ORD_TEST2 = 13;
    public static final int UMWDL_REG_GL = 14;
    public static final int INTERRED = 15;
    public static final int INTERRED_LINKS = 16;
    public static final int INTERRED_RECHTS = 17;
    public static final int LOESCHEN = 18;
    public static final int NORMALIZE2 = 19; 
    public static final int NORMALIZE_WITH2 = 20;
    public static final int MATCH2 = 21;
    public static final int MATCH2_TOPSYMBOLE_VGL = 22;
    public static final int MATCH2_SUBST_ERW = 23;
    public static final int MATCH2_ENDE = 39;
    public static final int ORD_TEST3 = 24;
    public static final int REDUZIEREN2 = 25;
    public static final int IN_PF_AUFNEHMEN = 26;
    public static final int CP_BILDEN = 27;
    public static final int CP_WITH_BILDEN = 28;
    public static final int UNIFIKATION = 29;
    public static final int UNIFIKATION_START = 30;
    public static final int UNIFIKATION_TOPSYMBOLE_VERGLEICHEN = 31;
    public static final int UNIFIKATION_OCCUR_CHECK = 32;
    public static final int UNIFIKATION_SUBST_ERWEITERN = 33; 
    public static final int CP_ERZEUGEN = 34;
    public static final int IN_AF_AUFNEHMEN = 35;
    public static final int PF_AUSWAEHLEN = 36;
    public static final int UNIFIKATION_ENDE = 37;
    
    public static final int numberOfEvents = 39;
    /**
     * Die Parameter des Events. Als Vector von ungetypten Objekten.
     **/
    private Vector params;

    /**
     * Die eindeutige ID des Events
     **/
    private int ID;

    /**
     * Auf welcher Stufe in der EventHierachie befindet sich das Event ?
     **/
    private int priorityLevel;

    /**
     * Der ausgeschriebene Name des Events
     **/
    private String name;

    /**
     * Der Name der ViewKlasse, mit der das Event visualisiert werden soll
     **/
    private String view;


    public String getView()
    {
	return view;
    }

    public String getName()
    {
	return name;
    }
 
    public int getPriorityLevel()
    {
	return priorityLevel;
    }

    public int getID()
    {
	return ID;
    }

    public Vector getParams()
    {
	return params;
    }

    public void setName(String aName)
    {
	name=aName;
    }

    public void setView(String aView)
    {
	view = aView;
    }

    public void setPriority(int aPrio)
    {
	priorityLevel = aPrio;
    }
    
    public VisualEvent (int anID)
    {
	ID = anID;
	params = new Vector();
    }

    /**
     * Erzeugt ein leeres Event mit uebergebener ID
     **/
    public VisualEvent(int anID,int aPriority)
    {
	ID = anID;
	priorityLevel = aPriority;
	params = new Vector();
    }
    
    /**
     * Fuegt einen Parameter hinten an
     **/
    public void addParam(Object aParam)
    {
	params.add(aParam);
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newCLoop() {
	VisualEvent ev = new VisualEvent(VisualEvent.CLOOP,1);
	ev.setName("Vervollstaendigungsschleife");
	ev.setView("cLoopView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newCStep() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.CSTEP,2);
	ev.setName("Vervollstaendigungsschritt");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newPFAuswaehlen() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.PF_AUSWAEHLEN,3);
	ev.setName("Ein passives Faktum auswaehlen");
	ev.setView("StubView");
	return ev;
    }
    
    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newOberstesAuswaehlen() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.OBERSTES_AUSWAEHLEN,4);
	ev.setName("Oberstes Element der passiven Fakten auswaehlen");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newElternVorhanden() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.ELTERN_VORHANDEN,4);
	ev.setName("Auf Vorhandensein der Eltern testen");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newReduzieren() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.REDUZIEREN,6);
	ev.setName("Regel anwenden");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newZusfbktTest() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.ZUSFBKT_TEST);
	ev.setName("Zusammenfuehrbarkeit testen");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newRichten() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.RICHTEN, 3);
	ev.setName("Richten des Termpaares");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newUmwdlRegGl() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.UMWDL_REG_GL, 4);
	ev.setName("Umwandeln des Termpaares in eine Regel bzw. eine Gleichung");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newInterred() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.INTERRED, 3);
	ev.setName("Interreduzieren");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newInterredLinks() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.INTERRED_LINKS, 4);
	ev.setName("Interreduzieren der linken Seiten");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newInterredRechts() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.INTERRED_RECHTS, 4);
	ev.setName("Interreduzieren der rechten Seiten");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newLoeschen() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.LOESCHEN, 4);
	ev.setName("Loeschen eines aktiven Faktums");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newInPFAufnehmen() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.IN_PF_AUFNEHMEN, 4);
	ev.setName("In die passiven Fakten aufnehmen");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newCPBilden()
    {
	VisualEvent ev = new VisualEvent(VisualEvent.CP_BILDEN,3);
	ev.setName("Kritische Paare bilden");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newCPBildenWith()
    {
	VisualEvent ev = new VisualEvent(VisualEvent.CP_WITH_BILDEN,4);
	ev.setName("Kritische Paare bilden mit spezieller Regel");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newCPErzeugen()
    {
	VisualEvent ev = new VisualEvent(VisualEvent.CP_ERZEUGEN,50);
	ev.setName("Kritisches Paar erzeugen");
	ev.setView("StubView");
	return ev;
    }

    /**
     * erzeugt ein XYZ-Event
     * pending: Kommentare..
     **/
    public static VisualEvent newInAFAufnehmen() {
	// pending: Richtigen View setzen
	VisualEvent ev = new VisualEvent(VisualEvent.IN_AF_AUFNEHMEN, 4);
	ev.setName("In die aktiven Fakten aufnehmen");
	ev.setView("StubView");
	return ev;
    }

    /**
     * Erzeugt neues Event: NORMALIZE
     * Parameterreihenfolge:
     * 
     * 1) Term
     * 2) Term
     * 
     **/
    public static VisualEvent newNormalize(Term t1,Term t2)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.NORMALIZE,4);
	ev.setName("Normalform gebildet");
	ev.setView("NormalizerView");
	ev.addParam(t1);
	ev.addParam(t2);
	return ev;
    }
    
    /**
     * Erzeugt neues Event: NORMALIZE_WITH
     * Parameterreihenfolge:
     * 
     * 1) Term
     * 2) Term
     * 3) TermPair
     * 4) int
     **/
    public static VisualEvent newNormalize_with(Term t1,Term t2,TermPair tp,int n)
    {	
	VisualEvent ev = new VisualEvent(VisualEvent.NORMALIZE_WITH,5);
	ev.setName("Reduziere mit einem aFaktum");
	ev.setView("NormalizerView");
	ev.addParam(t1);
	ev.addParam(t2);
	ev.addParam(tp);
	ev.addParam(new Integer(n));
	return ev;
    }

    /**
     * Erzeugt neues Event: UNIFIKATION
     * Parameterreihenfolge:
     * 
     * 1) Term
     * 2) Term
     * 3) Regel bzw. Gleichung
     **/
    public static VisualEvent newUnifikation(Term t1,Term t2,Substitution subst)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.UNIFIKATION,5);
	ev.setName("Unifikation fertig");
	ev.setView("UnificationView");
	ev.addParam(t1);
	ev.addParam(t2);
	ev.addParam(subst);
	return ev;
    }
    /**
     * Erzeugt neues Event: UNIFIKATION_START
     * Parameterreihenfolge:
     * 
     * 1) Term
     * 2) Term
     * 3) Substitution
     **/
    public static VisualEvent newUnifikationStart(Term t1,Term t2,Substitution subst)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.UNIFIKATION_START,6);
	ev.setName("Unifikations gestarted");
	ev.setView("UnificationView");
	ev.addParam(t1);
	ev.addParam(t2);
	ev.addParam(subst);
	return ev;
    }

    /**
     * Erzeugt neues Event: UNFIKATION_TOPSYMBOLE_VERGLEICHEN
     * Parameterreihenfolge:
     * 
     * 1) Term (Subterm, dessen TopSymbol verglichen wird)
     * 2) Term ( " )
     **/
    public static VisualEvent newUnifikationTopVgl(Term top1,Term top2)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.UNIFIKATION_TOPSYMBOLE_VERGLEICHEN);
	ev.setPriority(6);
	ev.setName("Topsymbole vergleichen");
	ev.setView("UnificationView");
	ev.addParam(top1);
	ev.addParam(top2);
	return ev;
    }

    /**
     * Erzeugt neues Event: UNIFIKATION_OCCUR_CHECK
     * Parameterreihenfolge:
     * 
     * 1) Boolean
     **/
    public static VisualEvent newOccurCheckFailed()
    {
	VisualEvent ev = new VisualEvent(VisualEvent.UNIFIKATION_OCCUR_CHECK,6);
	
	ev.setName("");
	ev.setView("UnificationView");
	return ev;
    }

    /**
     * Erzeugt neues Event: UNIFIKATION_SUBST_ERWEITERN
     * Parameterreihenfolge:
     * 
     * 1) Symbol     x
     * 2) Term       t mit x <- t
     * 3) Integer    Stelle, an der Subst angewendet wurde
     * 4) Term       
     **/
    public static VisualEvent newUnifikationSubstErweitern(Symbol var, Term t)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.UNIFIKATION_SUBST_ERWEITERN,6);
	ev.setName("Substitution erweitern");
	ev.setView("UnificationView");
	ev.addParam(var);
	ev.addParam(t);
	return ev;
    }

    /**
     * Erzeugt neues Event: UNIFIKATION_ENDE
     * Parameterreihenfolge:
     * 
     * 1) Boolean
     **/
    public static VisualEvent newUnifikationEnde(Boolean geklappt)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.UNIFIKATION_ENDE,6);
	if (geklappt.booleanValue())
	    ev.setName("Unifikator gefunden");
	else
	    ev.setName("Kein Unifikator gefunden");
	
	ev.setView("UnificationView");
	ev.addParam(geklappt);
	return ev;
    }

    /**
     * Erzeugt neues Event: MATCH
     * Parameterreihenfolge:
     * 1) Term
     * 2) Term auf den gematcht werden soll
     * 3) Substitution
     **/
    public static VisualEvent newMatch(Term s, Term t, Substitution subst) {
	VisualEvent ev = new VisualEvent(VisualEvent.MATCH,6);
	//	ev.setName("Versuche "+s+" auf "+t+" zu matchen");
	ev.setView("MatchView");
	ev.addParam(s);
	ev.addParam(t);
	ev.addParam(subst);
	return ev;
    }

    /**
     * Erzeugt neues Event: MATCH_SUBST_ER
     * Parameterreihenfolge:
     * 
     * 1) Symbol
     * 2) Term
     **/
    public static VisualEvent newMatchSubstErw(Symbol var, Term t)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.MATCH_SUBST_ERW,6);
	ev.setName("MatchSubstErw");
	ev.setView("MatchView");
	ev.addParam(var);
	ev.addParam(t);
	return ev;
    }

    /**
     * Erzeugt neues Event: MATCH_TOPSYMBOLE_VGL
     * Parameterreihenfolge:
     * 
     * 1) Term (Subterm, dessen TopSymbol verglichen wird)
     * 2) Term ( " )
     **/
    public static VisualEvent newMatchTopVgl(Term top1,Term top2)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.MATCH_TOPSYMBOLE_VGL,6);
	ev.setPriority(6);
	/*	String s="Vergleiche Topsymbole : "+top1.getTopSymbol()+" und "+top2.getTopSymbol();
	if (top1.getTopSymbol() == top2.getTopSymbol())
	    ev.setName (s + " sind gleich ");
	else
	    ev.setName (s + " sind nicht gleich ");
	*/
	ev.setView("MatchView");
	ev.addParam(top1);
	ev.addParam(top2);
	return ev;
    }

/**
     * Erzeugt neues Event: MATCH_ENDE
     * Parameterreihenfolge:
     * 
     * 1) Boolean
     **/
    public static VisualEvent newMatchEnde(Boolean geklappt)
    {
	VisualEvent ev = new VisualEvent(VisualEvent.MATCH_ENDE,6);
	if (geklappt.booleanValue())
	    ev.setName("Match gefunden");
	else
	    ev.setName("Kein Match gefunden");
	
	ev.setView("MatchView");
	ev.addParam(geklappt);
	return ev;
    }

    public static VisualEvent newOrdTest(TreeView tv, Boolean geklappt) {
	VisualEvent ev = new VisualEvent(VisualEvent.ORD_TEST,6);
	ev.setView("LPOView");
	ev.addParam(tv);
	if (geklappt.booleanValue()) {
	    ev.setName("Ordnungstest (LPO) OK");
	}
	else {
	    ev.setName("Ordnungstest (LPO) fehlgeschlagen");
	}
	return ev;
    }

    public String debugString()
    {
	String s = "--------------------------------------- \n";
	s = s + "Priority " + priorityLevel + "\n";
	if (getName() != null)
	    s = s + getName() + "\n";
	s = s + "Id " + getID() + "\n";
	s = s + "View " + getView() + "\n";
	
	Iterator it = params.iterator();
	Object pa = null;
	int nr = 0;
	while (it.hasNext()){
	    pa = it.next();
	    s = s + nr++ + " : ";
	    if (pa == null)
		s = s + "null \n";
	    else
		s = s + pa +"\n";
	}
	s = s + "--------------------------------------- ";

	return s;
    }

    public String toString()
    {
	if (getName()==null)
	    return " ? ? ? ";
	else
	    return getName();
    }
}
