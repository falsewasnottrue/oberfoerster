
/**
 * cLoopView.java
 *
 *
 * Created: Mon Jun 28 13:50:11 1999
 *
 * Visualisierung der Haupvervollstaendigungsschleife
 * zeigt die Maechtigkeiten der Mengen der aktiven und passiven Fakten an.
 * @author Daniel Jonietz
 * @version
 */

package visual;
import basis.*;
import java.awt.*;

public class cLoopView extends EventView {
    
    private DiaView dia;

    /**
     * Konstruktor
     **/
    public cLoopView() {
	initialize();
    }
   
    /**
     * gibt die Kontrolle weiter ...
     **/
    public void doLayout() {
	dia.doLayout();
 	super.doLayout();
   }

    /**
     * Legt ein ScrollPane an, initialisiere ein Diagramm und zeige es darauf an
     **/ 
    public void initialize() {
	setLayout(new GridLayout(1,1));
	dia = new DiaView();
	ScrollPane sp = new ScrollPane();
	Panel p = new Panel();
	sp.add(dia);
	p.add(sp);
	p.setLayout(new GridLayout(1,1));
	add(p);	
	//	dia.doLayout();
	// System.out.println("dia "+dia.getParent());
	validate();
    }

    /**
     * Uebergib dem Diagramm die neuen, aktuellen Maechtigkeiten, 
     * nachdem sie vom Oberfoerster geholt wurden;
     * falls dieses Event auch hier visualisiert werden soll.
     **/
    public boolean visualizeEvent(VisualEvent event) {
	if (event.getID()==VisualEvent.CLOOP) {
	dia.newdata(Oberfoerster.getActiveFacts().getSize(), 
		    Oberfoerster.getPassiveFacts().getSize());
	}
	else {
	    System.out.println("Falsches Event in cLoopView !");
	    System.exit(0);
	}
	repaint();
	return true;
    }



} // cLoopView
