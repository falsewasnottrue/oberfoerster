
/*
 * Datei: EventTreeViewNode
 */

package visual;

public class EventTreeViewNode extends TreeViewNode {

    public EventTreeViewNode(String s, int _ID) {
	super(s);
	ID = _ID;
    }

    private int ID;

    public int getID() {
	return ID;
    }
}
