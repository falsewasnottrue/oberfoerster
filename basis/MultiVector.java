package basis;

import java.util.Vector;
import java.util.Iterator;

/**
 * Hauptzweck dieser Klasse, ist einen Iterator zur
 * Verfeuegung zu stellen, der ueber mehrere Vectoren laeft
 **/
public class MultiVector extends Object
{
    // Ein Vector von Vectoren, der die Daten enthaelt
    private Vector vectors;
   
    public MultiVector()
    {
	vectors = new Vector();
    }

    public void addVector(Vector aVector)
    {
	vectors.add(aVector);
    }

    public Iterator iterator()
    {
	return new Iterator() {
	    
	    // Welcher Vector
	    int vectPos = 0;
	    // Position im Vector
	    int pos = -1;
	    // globale Position
	    int globalPos = 0;
	    // Zahl der Elemente in allen Vektoren
	    int globalSize = size();

	    public boolean hasNext() {
		return (globalPos <= globalSize - 1);
	    }
	    

	    public Object next(){

		// Ende von aktuellem Vector erreicht ?
		if (pos >= ((Vector)vectors.elementAt(vectPos)).size() - 1){
		    // dann naechsten nicht leeren Vector betrachten
		    pos = 0; 
		    do vectPos++; while ( ((Vector)vectors.elementAt(vectPos)).isEmpty() );
		}
		else
		    pos ++;
	
		// immer globale Position hochzaehlen
		globalPos++;
		return ((Vector)vectors.elementAt(vectPos)).elementAt(pos);
	    };

	    public void remove(){
	    }
	};
    }


    public int size(){
	int globsize = 0;
	Iterator vecs = vectors.iterator();
	while (vecs.hasNext()){
	    globsize += ((Vector)vecs.next()).size();
	}
	return globsize;

    }

}
