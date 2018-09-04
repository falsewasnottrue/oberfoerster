package basis;

/** 
 * Diese Klasse definiert die Funktionalitaet einer Praezendez auf der 
 * Funktionmenge, wie sie z.B. fuer KBO und LPO gebraucht wird
 *
 * @see KBO.java
 * @see LPO.java
 *
 * @author Markus Kaiser 
 **/
import java.util.Vector;
import java.util.Iterator;

public class Precedence extends Ordering
{  
    /**
     * Praezedenzmatrix: speichert Praezedenz
     **/
    protected int[][] precMat;
    /**
     * Konstruktor: erzeugt eine neue Praezedenz aus einem vorgegebenen Vector
     * von Ketten, z.B. 
     * ((f,g,h),(f,u,v,h)) - entspricht f > g > h, f > u > v > h
     *
     * @param chains Vector von Ketten (jeweils auch in der Form von Vectors)
     **/
    public Precedence (Vector chains)
    { 
  	/* chains enthaelt Vector's, die FunctionSymbol's enthalten */
       
        int dim = FunctionSymbol.getCounter();
        FunctionSymbol g, f = null;
        Iterator it1,it2;
        Vector vector;
        precMat = new int[dim][dim];  // Praezedenzmatrix erzeugen
        it1 = chains.iterator();
        /* Matrix initialisieren */
        for (int i=0;i<dim;i++)
          for (int j=0;j<dim;j++)
	      { precMat[i][j] = IS_UNCOMPARABLE; }
        /* Vergleiche einfuegen */
        while (it1.hasNext())
        {
          vector = (Vector) it1.next();
          it2 = vector.iterator();
          if (it2.hasNext()) {
            f = (FunctionSymbol) it2.next();
          }
          g = f;
          while (it2.hasNext()) {
            f = (FunctionSymbol) it2.next();
            precMat[g.getId()][f.getId()] = IS_GREATER;
            g = f;
          }
        }
        closure(dim);  // transitive Huelle bilden
        setEqualAndLess(dim);  // Matrix kompletieren
    }

    /**
     * Die Funktion closure bildet die transitive Huelle der Praezedenz;
     * dazu wird die Matrix precMat mit Hilfe des Algorithmus von Warshall
     * ergaenzt.
     **/
    protected int closure (int dim)
    {
        for (int j=0;j<dim;j++) {
          for (int i=0;i<dim;i++) {
             if (precMat[i][j] == IS_GREATER) {
	       for (int k=0;k<dim;k++) {
		 if (precMat[j][k] == IS_GREATER) {
                   precMat[i][k] = IS_GREATER;
                 }
               }
             }
           }
         }
         return 0;
     }
    /**
     * Die Funktion setEqualAndLess ergaenzt die fehlenden "IS_EQUAL" bzw.
     * "IS_LESS" Eintraege in precMat
     **/
    protected int setEqualAndLess (int dim)
    {
        for (int i=0;i<dim;i++) {
	    for (int j=0;j<dim;j++) {
		if (precMat[i][j] == IS_GREATER) {
		    if (precMat[j][i] == IS_GREATER) {  // Quasiordnung
                        precMat[i][j] = IS_EQUAL;
                        precMat[j][i] = IS_EQUAL;
                    }
                    else {
                        precMat[j][i] = IS_LESS;
                    }
                }
            }
        }
        for (int k=0;k<dim;k++) {
            precMat[k][k] = IS_EQUAL;
        }
        return 0;
    }
    /**
     * Die folgenden Funktionen vergleichen zwei Objekte;
     * dabei testen sie, ob es sich um Funktionen handelt,und vergleichen sie 
     * bezueglich der Praezedenz.
     *
     * @param o wird mit p verglichen 
     * @param p wird mit o verglichen
     * @return Ergebnis des Vergleichs f ~ g bezueglich der Reduktionsordnung
     *         Wertebereich: IS_LESS, IS_EQUAL, IS_GREATER, IS_UNCOMPARABLE
     **/
    public int compare(Object o, Object p)
    {
	if ((o instanceof FunctionSymbol) && (p instanceof FunctionSymbol))
	    return compareFSymbol((FunctionSymbol) o, (FunctionSymbol) p);
	else
	    return IS_UNCOMPARABLE;
    }

    /*
     * Hilfsfunktion fuer Testzwecke
     */
    public int compareTerm(Term s, Term t)
    {
	if ((!s.isVariable()) && (!t.isVariable())) {
	    return compareFSymbol((FunctionSymbol)s.getTopSymbol(),(FunctionSymbol)t.getTopSymbol());
	}
	else 
	    return IS_UNCOMPARABLE;
    }

    /**
     * Die Funktion maxPrec stellt fest, ob ein Funktionssymbol von max.
     * Praezedenz ist, oder nicht
     **/
    public boolean maxPrec (FunctionSymbol f, Precedence prec)
    {
        boolean res = true;
        int dim = FunctionSymbol.getCounter();
        for (int i=0;i<dim;i++) {
            if (prec.precMat[f.getId()][i] == IS_LESS) {
               res = false;
            }
        }
        return res;
    }
    /**
     * Vergleicht zwei Funktionssymbole bezueglich der Praezedenz.
     *
     * @param f wird mit g verglichen 
     * @param g wird mit f verglichen
     * @return Ergebnis des Vergleichs f ~ g bezueglich der Praezedenz
     *         Wertebereich: IS_LESS, IS_EQUAL, IS_GREATER, IS_UNCOMPARABLE
     **/
    protected int compareFSymbol(FunctionSymbol f, FunctionSymbol g)
    {
	return precMat[f.getId()][g.getId()];
    }

    /*
     * Testmethode
     */
    public void print()
    {
	for (int i=0;i<FunctionSymbol.getCounter();i++){
          for (int j=0;j<FunctionSymbol.getCounter();j++)	
	      System.out.print(precMat[i][j]);
	  System.out.println();
	}
    }
    
}






