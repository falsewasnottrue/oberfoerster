package basis;

import java.util.Iterator;

/**
 * KBO implementiert eine Knuth-Bendix-Ordnung
 * @author Markus Kaiser
 **/
public class KBO extends TermOrdering
{   
    /**
     *  Praezedenz und Gewichtsfunktion der KBO
     **/
    protected Precedence Prec;
    protected WeightFunction Phi;
   
    /**
     * Konstruktor: erzeugt eine KBO mit vorgegebener Praezedenz und
     * Gewichtsfunktion
     *
     * @param prec Die zu verwendende Praezedenz
     * @param phi Die zu verwendende Gewichtsfunktion
     **/
    public KBO(Precedence prec, WeightFunction phi)
    {
        Prec = prec;
        Phi  = phi;

    }
    
    /**
     * Vergleicht zwei Terme bezueglich der Reduktionsordnung.
     *
     * @param s wird mit t verglichen 
     * @param t wird mit s verglichen
     * @return Ergebnis des Vergleichs s ~ t bezueglich der Reduktionsordnung
     *         Wertebereich: IS_LESS, IS_EQUAL, IS_GREATER, IS_UNCOMPARABLE
     **/
    protected int compareTerm(Term s, Term t)
    {
	int res = IS_UNCOMPARABLE;
        Iterator it1,it2;
        Term term1,term2;
	/* Falls einer der Terme eine Variable ist, kann mit dem
         * Ergebniss von varCase aufgehoert werden.
         */
        if (s.isVariable() || t.isVariable()) {
	    return varCase(s,t); }  // Variablenfaelle
	/* sonst geht's weiter mit dem Vergleich der Gewichte:
	 * haben beide *nicht* das gleiche Gewicht, so aufhoeren mit
         * Vergleich von |s|_x >= |t|_x alle x in V
         */
        res = phiComp(s,t);   // Vergleich der Gewichte von s und t
        if (res!=IS_EQUAL) {
	    return handleVarCond(res,s,t); }
	// Pruefe jetzt, ob f > g oder f < g oder ... in der Praezedenz.
        res = precComp(s,t);  // Praezedenzbedingung
	/* Haben die Topsymbole der Terme  *nicht* die gleiche Praezedenz,
         * so wieder 
         * |s|_x >= |t|_x fuer alle x in V testen und aufhoeren.
         */ 
        if (res!=IS_EQUAL) {
	    return handleVarCond(res,s,t); }
	/* sonst haben sie die gleiche Praezedenz, muss noch lex.Vergleich
         * UND |s|_x >= |t|_x alle x in V testen. Dann Schluss.
         */
        it1 = s.getSubterms();
        it2 = t.getSubterms();
        while (it1.hasNext() && it2.hasNext()) {  // rekursiver Abstieg 
               term1 = (Term) it1.next();
               term2 = (Term) it2.next();
               res = compareTerm(term1,term2);
               if (res!=IS_EQUAL) { return handleVarCond(res,s,t); }
        }
	if (it1.hasNext()) {
            return IS_GREATER;
        }
	if (it2.hasNext()) {
            return IS_LESS;
        }
        return IS_EQUAL;
    }

    /**
     * phiComp vergleicht das Gewicht der beiden Terme s und t
     **/
    protected int phiComp (Term s,Term t)
    {
        int weight1 = Phi.getWeight(s), weight2 = Phi.getWeight(t);
        if (weight1 > weight2) { return IS_GREATER; }
        if (weight1 < weight2) { return IS_LESS; }
        return IS_EQUAL;
    }

    /**
     * precComp vergleicht die Praezedenz der Topsymbole von s und t
     **/
    protected  int precComp (Term s, Term t)
    {
        return Prec.compare(s.getTopSymbol(),t.getTopSymbol());
    }

    /**
     * handleVarCond ueberprueft die Variablenbedingung
     **/
    protected int handleVarCond (int r, Term s, Term t)
    {
        VarMultiSet VM1 = new VarMultiSet(s);
	if (r == IS_UNCOMPARABLE) {
            return IS_UNCOMPARABLE;
        }
        if (r == IS_GREATER) {
            if (VM1.varMultiSetComp(s,t)==IS_GREATER) {
                return IS_GREATER;
            }
	    if (VM1.varMultiSetComp(s,t)==IS_EQUAL) {
		return IS_GREATER;
	    }
        }
        if (r == IS_LESS) {
            if (VM1.varMultiSetComp(s,t)==IS_LESS) {
                return IS_LESS;
            }
	    if (VM1.varMultiSetComp(s,t)==IS_EQUAL) {
		return IS_LESS;
	    }
        }
        return IS_UNCOMPARABLE;
    }

    /**
     * varCond war Hilfsfunktion fuer handleVarCond (wird zur Zeit nicht
     * benutzt)
     **/
    protected boolean varCond (Term s, Term t)
    { 
        VarMultiSet VM1 = new VarMultiSet(s), VM2 = new VarMultiSet(t);
        VarSet VS = new VarSet(t);
        Iterator it = VS.getVar();
        VariableSymbol v;
        while (it.hasNext()) {
               v = (VariableSymbol) it.next();
               if (VM1.count(v) < VM2.count(v)) { return false; }
        }
        return true;
    }

    /**
     * varCase behandelt die Variablenfaelle, d.h. mindestens einer der
     * beiden Terme (s oder t) ist eine Variable
     **/
    protected int varCase (Term s, Term t)
    { 
        if (t.isVariable()) {
            if (s.isVariable()) {
                if (s.isEqual(t)) {
                    return IS_EQUAL;
                }
                else {
                    return IS_UNCOMPARABLE;
                }
            }
            else {
                if (s.containsVariable((VariableSymbol) t.getTopSymbol())){
                    return IS_GREATER;
                }
		else {
                    return IS_UNCOMPARABLE;
                } 
            }
        }
        else {
            if (t.containsVariable((VariableSymbol) s.getTopSymbol())) {
                return IS_LESS;
            }
	    else {
		return IS_UNCOMPARABLE;
            }
        }
    }
}




