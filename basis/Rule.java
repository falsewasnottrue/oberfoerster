/*
 * Datei Rule.java
 * 
 * @author Andreas Kohlmaier
 */

package basis;

public class Rule extends TermPair
{
    public Rule(TermPair tp)
    {
	this(tp.getLeft(),tp.getRight());
    }

    public Rule(Term left,Term right)
    {
	super(left,right);
	Protocol.notifyCreation(this);
    }

    /**
     * Prueft, ob es sich um eine Regel handelt.
     * Das ist in diesem Falle immer so
     **/
    public boolean isRule()
    {
	return true;
    }

     /**
     * Diese Methode fuegt die String-Repraesentation an einen StringBuffer
     * an. Bei Regeln in der Form l -> r.
     *
     * @param sb Stringbuffer, an den die String-Repraesentation abgehaengt 
     *  wird
     **/
    public void addToStringBuffer (StringBuffer sb)
    {
	lhs.addToStringBuffer(sb);
	sb.append(" -> ");
	rhs.addToStringBuffer(sb);
    } 
}
