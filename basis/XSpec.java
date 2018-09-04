/*
 *      Datei: XSpec.java
 */

package basis;

/**
 * Eine XSpec enthaelt alles, was man so aus einem File einliest:
 * Signatur, Ziele, Ordnung...
 * Sie ist ein reines Transportobjekt/Zusammenfassung anderer Objekte... 
 * DESWEGEN SIND ALLE OBJEKTVARIABLEN PUBLIC UND ES GIBT KEINE METHODEN
 **/

public final class XSpec
{
    /**
     * Moegliche Werte fuer das Feld mode
     **/
    public final static int MODE_COMPLETION  = 0;
    public final static int MODE_CONFLUENCE  = 1;
    public final static int MODE_CONVERGENCE = 2;
    public final static int MODE_PROOF       = 3;
    public final static int MODE_REDUCTION   = 4;
    public final static int MODE_TERMINATION = 5;

    /**
     * Der Name des Problems
     **/
    public String name;

    /**
     * Der Mode (Proof,Completion,...), vgl. die Konstanten MODE_*
     **/
    public int mode;

    /**
     * Die zugrunde liegende Spezifikation
     **/

    public Specification spec; 

    /**
     * Die Menge von Zielen
     **/

    public TermPair[] goals;

    /**
     * Die Ordnung
     **/

    public TermOrdering ordering;

    public String toString ()
    {
	StringBuffer sb = new StringBuffer();
	addToStringBuffer(sb);
	return sb.toString();
    }
    public void addToStringBuffer(StringBuffer sb)
    {
	sb.append("XSpec:\nNAME: ");
	sb.append(name);
	sb.append("\nMODE: ");
	sb.append(mode);
	sb.append("\n" + spec.getSig());
	sb.append("\nEQUATIONS:\n");
	TermPair.addArrayToStringBuffer(spec.getEquations(),sb);
	sb.append("GOALS:\n");
	TermPair.addArrayToStringBuffer(goals,sb);
	sb.append("ORDERING:\n" + ordering);
    }
}
