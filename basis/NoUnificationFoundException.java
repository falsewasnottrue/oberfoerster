/*
 * Datei: NoUnificationFoundException.java
 */

package basis;

import visual.*;

public class NoUnificationFoundException extends Exception
{
    private Term term1;
    private Term term2;
    
    public NoUnificationFoundException(Term t1, Term t2)
    {
	term1 = t1; term2 = t2;
	Protocol.notifyStopUnification();
	Visual.notify(VisualEvent.newUnifikationEnde(Boolean.FALSE));
    }

    public String toString()
    {
	return "Subterme : " +term1 + "  " + term2;
    }
}
