
/*
 * Datei: Path.java
 */

/**
 * kapselt einen Pfad im Auswahlbaum
 * @author Rasmus Hofmann
 **/

package visual;

public class Path {

    private int[] path;

    /**
     * Konstruktor, mit dem ein leerer Pfad (für die Wurzel) erzeugt wird
     **/
    public Path() {
        path = new int[1];
	path[0] = 0;
    }

    /**
     * erzeugt ein Pfadobjekt aus dem übergebenen Array
     **/
    public Path(int[] _path) {
	path = _path;
    }

    /**
     * Copy-Konstruktor (man weiß nie wofür sowas mal gut sein kann...)
     **/
    public Path(Path _path) {
	path = _path.getPath();
    }

    /**
     * gibt den Pfad als Array zurück
     **/
    public int[] getPath() {
	return path;
    }

    /**
     * gibt die Länge des Pfades zurück
     **/
    public int length() {
	return path.length;
    }

    /**
     * gibt den Wegweiser an der n.Stelle zurück
     *
     * @param n Stelle im Pfad
     **/
    public int getBranch(int n) {
	if ( n < path.length )
	    return path[n];
	else
	    return 0;
    }

    /**
     * erzeugt ein Pfadobekt, welches den momentanen Pfad enthält um n erweitert
     *
     * @param n Wegweiser um den der momentane Pfad verlängert wird
     **/
    public Path addPath(int n) {
	int[] newPath = new int[path.length + 1];

	for (int i=0; i<path.length; i++)
	    newPath[i] = path[i];

	newPath[path.length] = n;

	return new Path(newPath);
    }

    /**
     * schreibt sich selbst in den Stringbuffer
     *
     * @param sb hierein schreibt sich der Pfad
     **/
    public void addToStringBuffer(StringBuffer sb) {
	sb.append('(');
	for (int i=0; i < path.length; i++) {
		 sb.append(path[i]);
		 if ( i < path.length-1) sb.append(',');
	}
	sb.append(')');
    }

    /**
     * wandelt den Pfad in einen String um
     **/
    public String toString() {
	StringBuffer sb = new StringBuffer();
	addToStringBuffer(sb);
	return sb.toString();
    }
}
