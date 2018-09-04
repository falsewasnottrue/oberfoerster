
/*
 * Datei: visual/HierarchyTree.java
 */

/**
 * Enthaelt den Hierarchiebaum, der dazu dient grafisch zu veranschaulichen
 * welche Phase der Vervollstaendigung gerade visualisiert wird.
 **/

package visual;

import java.awt.*;
import java.awt.event.*;

public class HierarchyTree extends TreeView {

    // Array, der alle Pfade im Baum enthaelt
    private Path[] path;
    
    // Nummer des aktuell selektierten Pfades:
    int selectedPathID;

    // Referenz auf das Visual-Objekt
    Visual visual;

    private int requestedEventID = -1;

    public int getRequestedID() {
	return requestedEventID;
    }

    public HierarchyTree(Visual _visual) {
	super( new EventTreeViewNode("Vervollstaendigungsschleife", VisualEvent.CLOOP ));

	visual = _visual;
	path = new Path[VisualEvent.numberOfEvents+1];
	path[VisualEvent.CLOOP] = new Path();

	path[VisualEvent.CSTEP] = addNode( path[VisualEvent.CLOOP], new EventTreeViewNode("Vervollstaendigungsschritt", VisualEvent.CSTEP ));
	path[VisualEvent.PF_AUSWAEHLEN] = addNode( path[VisualEvent.CSTEP], new EventTreeViewNode("Passives Faktum auswaehlen", VisualEvent.PF_AUSWAEHLEN ));
	path[VisualEvent.OBERSTES_AUSWAEHLEN] = addNode( path[VisualEvent.PF_AUSWAEHLEN], 
							 new EventTreeViewNode("Oberstes Element der passiven Fakten auswaehlen", VisualEvent.OBERSTES_AUSWAEHLEN ));
	path[VisualEvent.ELTERN_VORHANDEN] = addNode( path[VisualEvent.PF_AUSWAEHLEN],
						      new EventTreeViewNode("Auf Vorhandensein der Elternregel pruefen", VisualEvent.ELTERN_VORHANDEN ));
	path[VisualEvent.NORMALIZE] = addNode( path[VisualEvent.PF_AUSWAEHLEN], new EventTreeViewNode("Normalisieren", VisualEvent.NORMALIZE ));
	path[VisualEvent.NORMALIZE_WITH] = addNode( path[VisualEvent.NORMALIZE],
						    new EventTreeViewNode("Reduzieren mit einer speziellen Regel", VisualEvent.NORMALIZE_WITH ));
	path[VisualEvent.MATCH] = addNode( path[VisualEvent.NORMALIZE_WITH], new EventTreeViewNode("Matchen", VisualEvent.MATCH ));
	path[VisualEvent.MATCH_TOPSYMBOLE_VGL] = addNode( path[VisualEvent.MATCH], new EventTreeViewNode("Top-Symbole vergleichen", VisualEvent.MATCH_TOPSYMBOLE_VGL ));
	path[VisualEvent.MATCH_SUBST_ERW] = addNode( path[VisualEvent.MATCH], new EventTreeViewNode("Substitution aufbauen", VisualEvent.MATCH_SUBST_ERW ));
	path[VisualEvent.MATCH_ENDE] = addNode( path[VisualEvent.MATCH], new EventTreeViewNode("Matchen beendet", VisualEvent.MATCH_ENDE ));
	path[VisualEvent.ORD_TEST] = addNode( path[VisualEvent.NORMALIZE_WITH], new EventTreeViewNode("Ordnungstest", VisualEvent.ORD_TEST ));
	path[VisualEvent.REDUZIEREN] = addNode( path[VisualEvent.NORMALIZE_WITH], new EventTreeViewNode("Regel anwenden/Reduzieren", VisualEvent.REDUZIEREN ));
	path[VisualEvent.ZUSFBKT_TEST] = addNode( path[VisualEvent.PF_AUSWAEHLEN], new EventTreeViewNode("Zusammenfuehrbarkeit testen", VisualEvent.ZUSFBKT_TEST ));
	path[VisualEvent.RICHTEN] = addNode( path[VisualEvent.CSTEP], new EventTreeViewNode("Richten", VisualEvent.RICHTEN ));
	path[VisualEvent.ORD_TEST2] = addNode( path[VisualEvent.RICHTEN], new EventTreeViewNode("Ordnungstest", VisualEvent.ORD_TEST2 ));
	path[VisualEvent.UMWDL_REG_GL] = addNode( path[VisualEvent.RICHTEN], new EventTreeViewNode("Umwandeln in Regel/Gleichung", VisualEvent.UMWDL_REG_GL ));
	path[VisualEvent.INTERRED] = addNode( path[VisualEvent.CSTEP], new EventTreeViewNode("Interreduzieren", VisualEvent.INTERRED ));
	path[VisualEvent.INTERRED_LINKS] = addNode( path[VisualEvent.INTERRED], new EventTreeViewNode("Linke Seiten reduzieren", VisualEvent.INTERRED_LINKS ));
	path[VisualEvent.INTERRED_RECHTS] = addNode( path[VisualEvent.INTERRED], new EventTreeViewNode("Rechte Seiten reduzieren", VisualEvent.INTERRED_RECHTS ));
	path[VisualEvent.LOESCHEN] = addNode( path[VisualEvent.INTERRED], new EventTreeViewNode("Loeschen", VisualEvent.LOESCHEN ));
	path[VisualEvent.NORMALIZE2] = addNode( path[VisualEvent.INTERRED], new EventTreeViewNode("Normalisieren", VisualEvent.NORMALIZE2 ));
	path[VisualEvent.NORMALIZE_WITH2] = addNode( path[VisualEvent.NORMALIZE2],
						     new EventTreeViewNode("Reduzieren mit einer speziellen Regel", VisualEvent.NORMALIZE_WITH2 ));
	path[VisualEvent.MATCH2] = addNode( path[VisualEvent.NORMALIZE_WITH2], new EventTreeViewNode("Matchen", VisualEvent.MATCH2 ));
	path[VisualEvent.MATCH2_TOPSYMBOLE_VGL] = addNode( path[VisualEvent.MATCH2], new EventTreeViewNode("Top-Symbole vergleichen", VisualEvent.MATCH2_TOPSYMBOLE_VGL ));
	path[VisualEvent.MATCH2_SUBST_ERW] = addNode( path[VisualEvent.MATCH2], new EventTreeViewNode("Substitution aufbauen", VisualEvent.MATCH2_SUBST_ERW ));
	path[VisualEvent.MATCH2_ENDE] = addNode( path[VisualEvent.MATCH2], new EventTreeViewNode("Matchen beendet", VisualEvent.MATCH2_ENDE ));
	path[VisualEvent.ORD_TEST3] = addNode( path[VisualEvent.NORMALIZE_WITH2], new EventTreeViewNode("Ordnungstest", VisualEvent.ORD_TEST3 ));
	path[VisualEvent.REDUZIEREN2] = addNode( path[VisualEvent.NORMALIZE_WITH2], new EventTreeViewNode("Regel anwenden/Reduzieren", VisualEvent.REDUZIEREN2 ));
	path[VisualEvent.IN_PF_AUFNEHMEN] = addNode( path[VisualEvent.INTERRED], new EventTreeViewNode("In Passive Fakten aufnehmen", VisualEvent.IN_PF_AUFNEHMEN ));
	path[VisualEvent.CP_BILDEN] = addNode( path[VisualEvent.CSTEP], new EventTreeViewNode("Kritische Paare bilden", VisualEvent.CP_BILDEN ));
	path[VisualEvent.CP_WITH_BILDEN] = addNode( path[VisualEvent.CP_BILDEN],
						    new EventTreeViewNode("Kritische Paare mit speziellem Termpaar bilden", VisualEvent.CP_WITH_BILDEN ));
	path[VisualEvent.UNIFIKATION] = addNode( path[VisualEvent.CP_WITH_BILDEN],
						 new EventTreeViewNode("Subterm auf Unifizierbarkeit pruefen", VisualEvent.UNIFIKATION ));
	path[VisualEvent.UNIFIKATION_START] = addNode( path[VisualEvent.UNIFIKATION],
						       new EventTreeViewNode("Unifikation gestartet", VisualEvent.UNIFIKATION_START ));
	path[VisualEvent.UNIFIKATION_TOPSYMBOLE_VERGLEICHEN] = addNode( path[VisualEvent.UNIFIKATION], 
									new EventTreeViewNode("Top-Symbole vergleichen", VisualEvent.UNIFIKATION_TOPSYMBOLE_VERGLEICHEN ));
	path[VisualEvent.UNIFIKATION_OCCUR_CHECK] = addNode( path[VisualEvent.UNIFIKATION],
							     new EventTreeViewNode("occur-check", VisualEvent.UNIFIKATION_OCCUR_CHECK ));
	path[VisualEvent.UNIFIKATION_SUBST_ERWEITERN] = addNode( path[VisualEvent.UNIFIKATION], 
								 new EventTreeViewNode("Substitution erweitern", VisualEvent.UNIFIKATION_SUBST_ERWEITERN ));
	path[VisualEvent.UNIFIKATION_ENDE] = addNode( path[VisualEvent.UNIFIKATION],
						      new EventTreeViewNode("Unifikation beendet", VisualEvent.UNIFIKATION_ENDE ));
	path[VisualEvent.CP_ERZEUGEN] = addNode( path[VisualEvent.CP_WITH_BILDEN], new EventTreeViewNode("Kritische Paare erzeugen", VisualEvent.CP_ERZEUGEN ));
	path[VisualEvent.IN_AF_AUFNEHMEN] = addNode( path[VisualEvent.CSTEP], new EventTreeViewNode("In die aktiven Fakten aufnehmen", VisualEvent.IN_AF_AUFNEHMEN ));

	fill();


	addItemListener( new ItemListener() {
	    public void itemStateChanged(ItemEvent e) {
		
		requestedEventID = ((EventTreeViewNode)items.elementAt(getSelectedIndex())).getID();
		visual.controlPanel.setInfo("Warte auf Event " + ((EventTreeViewNode)items.elementAt(getSelectedIndex())).getText());
		System.out.println("Id : "+requestedEventID);
	    }
	});
    }

    /**
     * selektiert den Eintrag des Events im Hierarchiebaum
     *
     * @param event das Event zu dem der Eintrag selektiert werden soll
     **/
    public void selectEvent(VisualEvent event) {
	// Das folgende switch-Statement dient dazu, zwischen Events die an mehreren Stellen
	// im Hierarchiebaum auftreten zu unterscheiden. Dies berechnet die Methode aus dem
	// aktuell selektierten Pfad: Erhaelt sie z.B. die Aufforderung MATCH zu selektieren,
	// dann sieht die Methode nach, wo im Baum wir uns zuletzt befunden haben, und sucht
	// automatisch :) den naechsten Eintrag aus!
	requestedEventID = -1;
	switch ( event.getID() ) {
	case VisualEvent.NORMALIZE : {
	    if (selectedPathID==VisualEvent.ELTERN_VORHANDEN)
		selectedPathID = VisualEvent.NORMALIZE;
	    else
		selectedPathID = VisualEvent.NORMALIZE2;
	    break;
	}
	case VisualEvent.NORMALIZE_WITH : {
	    if (selectedPathID==VisualEvent.NORMALIZE)
		selectedPathID = VisualEvent.NORMALIZE_WITH;
	    else
		selectedPathID = VisualEvent.NORMALIZE_WITH2;
	    break;
	}
	case VisualEvent.MATCH : {
	    if ((selectedPathID==VisualEvent.NORMALIZE_WITH) ||
		(selectedPathID==VisualEvent.REDUZIEREN))
		selectedPathID = VisualEvent.MATCH;
	    else
		selectedPathID = VisualEvent.MATCH2;
	    break;
	}
	case VisualEvent.MATCH_TOPSYMBOLE_VGL : {
	    if ((selectedPathID==VisualEvent.MATCH) ||
		(selectedPathID==VisualEvent.MATCH_ENDE))
		selectedPathID = VisualEvent.MATCH_TOPSYMBOLE_VGL;
	    else
		selectedPathID = VisualEvent.MATCH2_TOPSYMBOLE_VGL;
	    break;
	}
	case VisualEvent.MATCH_SUBST_ERW : {
	    if (selectedPathID==VisualEvent.MATCH_TOPSYMBOLE_VGL)
		selectedPathID = VisualEvent.MATCH_SUBST_ERW;
	    else
		selectedPathID = VisualEvent.MATCH2_SUBST_ERW;
	    break;
	}
	case VisualEvent.MATCH_ENDE : {
	    if (selectedPathID==VisualEvent.MATCH_SUBST_ERW)
		selectedPathID = VisualEvent.MATCH_ENDE;
	    else
		selectedPathID = VisualEvent.MATCH2_ENDE;
	    break;
	}
	case VisualEvent.REDUZIEREN : {
	    if (selectedPathID == VisualEvent.ORD_TEST )
		selectedPathID = VisualEvent.REDUZIEREN;
	    else
		selectedPathID = VisualEvent.REDUZIEREN2;
	    break;
	}
	case VisualEvent.ORD_TEST : {
	    if ((selectedPathID==VisualEvent.MATCH) || (selectedPathID==VisualEvent.MATCH_SUBST_ERW))
		selectedPathID = VisualEvent.ORD_TEST;
	    if ( selectedPathID == VisualEvent.RICHTEN )
		selectedPathID = VisualEvent.ORD_TEST2;
	    if ((selectedPathID==VisualEvent.MATCH2) || (selectedPathID==VisualEvent.MATCH2_SUBST_ERW))
		selectedPathID = VisualEvent.ORD_TEST3;
	    break;
	}
	default : {
	    selectedPathID = event.getID();
	}

	// So, und nachdem jetzt die ID des neuen zu selektierenden Pfades bestimmt ist,
	// muss er noch selektiert werden:
	selectNode( path[selectedPathID] );
	}
    }

    /**
     * gibt true zurueck, falls an der momentan selektierten Stelle ein StepInto
     * moeglich ist, sonst false.
     *
     * @returns true, falls StepInto moeglich
     **/
    public boolean canStepIn() {
	// Dem Leser zum Geleit: Reinzoomen ( eng.: Reinsuumen ) kann man, wenn
	// der Knoten in dem man sich gerade befindet, Nachfahren hat, also:
	return ( retrieveNode( path[selectedPathID] ).hasSons() );
    }

    /**
     * gibt true zurueck, falls an der momentan selektierten Stelle ein StepOut
     * moeglich ist, sonst false.
     *
     * @returns true, falls StepOut moeglich
     **/
    public boolean canStepOut() {
	// Rauszoomen ( zur Aussprache, s.o. ) kann man nur an endlich vielen
	// Stellen nicht, naemlich an der Wurzel des Hierarchiebaums, also:
	return ( selectedPathID != VisualEvent.CLOOP );
    }

    // Testprogramm:
    public static void main( String[] args ) {

	HierarchyTree ht = new HierarchyTree((Visual)null);
	Frame fram = new Frame();
	fram.setSize(300,300);
	fram.add(ht);
	fram.setVisible(true);
	fram.repaint();
    }
}
