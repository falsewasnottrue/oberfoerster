/*
 * Datei: Scanner.java
 */

package basis;

import java.util.Hashtable;
import java.io.Reader;

import java.io.FileReader; // nur fuer Testzwecke -> main

/**
 * Der Scanner, der vom Parser benutzt wird, um Spezifikationsfiles zu 
 * lesen; er geht die Eingabe durch und liefert dem Parser eine entsprechende 
 * Folge von Tokens.
 * 
 * @author Bernd Loechner <loechner@informatik.uni-kl.de>
 *
 * @version $Id: Scanner.java,v 1.9 1999/05/06 09:00:05 rsprak Exp $
 **/
public class Scanner
{
    /**
     * Die verschiedenen Tokens. Statt einer eigenen Klasse wird das Java-
     * Aequivalent eines Aufzaehlungstyps verwendet.
     **/
    public final static int EOI_TK         =  0; // Ende der Eingabe

    public final static int NAME_TK        =  1; // Schluesselwoerter
    public final static int MODE_TK        =  2;
    public final static int SORT_TK        =  3;
    public final static int SIGNATURE_TK   =  4;
    public final static int ORDERING_TK    =  5;
    public final static int VARIABLE_TK    =  6;
    public final static int EQUATION_TK    =  7;
    public final static int CONCLUSION_TK  =  8;
    public final static int COMPLETION_TK  =  9;
    public final static int CONFLUENCE_TK  = 10;
    public final static int CONVERGENCE_TK = 11;
    public final static int PROOF_TK       = 12;
    public final static int REDUCTION_TK   = 13;
    public final static int TERMINATION_TK = 14;
    public final static int KBO_TK         = 15;
    public final static int LPO_TK         = 16;

    public final static int IDENT_TK       = 17;  // Bezeichner etc.
    public final static int SIDENT_TK      = 18;
    public final static int VIDENT_TK      = 19;
    public final static int FIDENT_TK      = 20;
    public final static int OP_TK          = 21;
    public final static int NUMBER_TK      = 22;

    public final static int COLON_TK       = 23;  // Hilfsymbole
    public final static int ARROW_TK       = 24;
    public final static int COMMA_TK       = 25;
    public final static int EQUALS_TK      = 26;
    public final static int GREATER_TK     = 27;
    public final static int LPAREN_TK      = 28;
    public final static int RPAREN_TK      = 29;

    /**
     * Interner Zustand des Scanners: sollen Ziffernfolgen als
     * Bezeichner oder als Zahlen interpretiert werden. 
     **/
    protected boolean expectNumber;
    /**
     * Die Eingabedaten des Scanners.
     **/
    protected String input;
    /**
     * Die Laenge der Eingabedaten. Ist einfacher, als immer wieder
     * input.length aufzurufen.
     **/
    protected int inputLength;
    /**
     * Die Startadresse des aktuellen Tokens.
     **/
    protected int tkStart;
    /**
     * Die Laenge des aktuellen Tokens.
     **/
    protected int tkLength;
    /**
     * Der Wert des aktuellen Tokens (irgendetwas zwischen EOI_TK und
     * RPAREN_TK).
     **/
    protected int currentTk;

    /**
     * Eine Hashtabelle mit den bekannten Bezeichner. Wird mit den
     * Schluesselwoertern der Spezifikation (NAME, MODE, ...)
     * initialisiert.  Bei der Deklaration von Sorten-, Funktions- und
     * Variblenbezeichnern wird die knownIdents durch Aufruf der
     * Methode registerIdent entsprechend erweitert. 
     **/
    protected Hashtable knownIdents;

    /**
     * Konstruktor: Erzeugt einen neuen Scanner mit einem vorgegebenen Reader,
     * der ihm die einzelnen Characters aus einem stream (in der Regel aus
     * einem Spezifikationsfile) liefert
     *
     * @param in Der zu verwendende Reader
     **/
    public Scanner(Reader in)
	throws ParseException
    {
	this(readIntoString(in));
    }

    /**
     * Konstruktor: Erzeugt einen neuen Scanner aus einem String, der die
     * Spezifikation beinhaltet.
     *
     * @param s der String mit der Spezifikation
     **/
    public Scanner(String s)
	throws ParseException
    {
	input       = s + "\n"; // simplifies some tests, since input ends 
	                        // in whitespace
	inputLength = input.length();
	tkStart     = 0;
	tkLength    = 0;
	initKnownIdents();
	eatToken(); // initializes currentTk
    }

    /**
     * setzt das Flag expectNumber auf den vorgegebenen Wert
     *
     * Der Scanner hat bezueglich Zahlen zwei Zustaende. Normalerweise
     * interpretiert er Zahlen als Bezeichner, um z.B. Spezifikationen der
     * natuerlichen Zahlen zu erleichtern. Wird eine Gewichtsfunktion fuer
     * die KBO eingelesen, kann der Scanner umgestellt werden.
     * Beispiel:
     *
     *      KBO 0 = 1, ...
     *
     * Die ``0'' ist ein Konstantenbezeichner, die ``1'' dessen
     * Gewicht.  Deshalb sollte vor Scannen der ``1'' der Scanner
     * entsprechend umgestellt werden, danach wieder zurueck.
     *
     * @param b der neue Wert von expectNumber
     **/
    public void setExpectNumber(boolean b)
    {
	expectNumber = b;
    }

    /**
     * Akzeptiert ein Token vom Typ tk und geht zum naechsten ueber; falls das 
     * aktuelle Token nicht vom Typ tk ist, wird eine ParseException geworfen.
     *
     * @param tk der zu akzeptierende Tokentyp
     *
     * @exception ParseException falls das Token nicht uebereinstimmt oder
     *   beim Scannen des Folgetokens ein Fehler auftritt.
     **/
    public void acceptToken(int tk)
	throws ParseException
    {
	if (currentTk != tk)
	    throw new ParseException(this, "Unexpected Token");
	eatToken();
    }

    /**
     * gibt an, ob das aktuelle Token vom Typ tk ist; falls ja, wird zum 
     * naechsten uebergegangen, ansonsten nicht gemacht.
     *
     * @param tk der zu testende Tokentyp
     *
     * @result das aktuelle Token war vom Typ tk und wurde ueberlesen
     *
     * @exception ParseException 
     *         falls beim Scannen des Folgetokens ein Fehler auftritt.
     **/
    public boolean isToken(int tk)
	throws ParseException
    {
	if (currentTk == tk){
	    eatToken();
	    return true;
	}
	else
	    return false;
    }

    /**
     * Gibt das aktuelle Token aus.
     *
     * @result das aktuelle Token (Wertebereich EOI_TK ..RPAREN_TK)
     **/
    public int nextToken()
    {
	return currentTk;
    }

    /**
     * Macht einen lookahead, d.h. das naechste Token wird zurueckgegeben,
     * aber das momentan aktuelle Token bleibt erhalten.
     * 
     * Funktioniert deshalb so problemlos, weil die komplette Eingabe am 
     * Anfang in den String input gelesen wird.
     *
     * @see basis.Parser#parseConstant(basis.FunctionSymbol)
     * @result das naechste Token (Wertebereich EOI_TK ..RPAREN_TK)
     * @exception ParseException 
     *         falls beim Scannen des Folgetokens ein Fehler auftritt.
     **/ 
    public int peekToken()
	throws ParseException
    {
	int tks = tkStart; // save state
	int tkl = tkLength;
	int ctk = currentTk;

	eatToken();        // scan ahead
	int ntk = currentTk;

	tkStart = tks;     // reset state
	tkLength = tkl;
	currentTk = ctk;
	
	return ntk;
    }

    /**
     * Geht zum naechsten Token ueber; falls ein nicht erwarteter character
     * kommt, wird eine ParseException geworfen. Hier erfolgt also die 
     * Hauptarbeit des Scanners. 
     *
     * @exception ParseException
     **/
    public void eatToken()
	throws ParseException
    {
	tkStart = tkStart + tkLength; // Skip old token
	eatWhiteSpace();
	if (!(tkStart < inputLength)){// Test if end is reached
	    currentTk = EOI_TK;                                        return;
	}
	// Dispatching on first character
	char fstChar = input.charAt(tkStart);
	tkLength = 1;
	if (expectNumber && Character.isDigit(fstChar)){
	    scanNumber();            		                       return;
	}
	if (Character.isLetterOrDigit(fstChar) || fstChar == '_'){
	    scanIdentOrKeyword();		                       return;
	}
	switch (fstChar){
	case ':': currentTk = COLON_TK;   			       return;
	case ',': currentTk = COMMA_TK;   			       return;
	case '=': currentTk = EQUALS_TK;  			       return;
	case '>': currentTk = GREATER_TK; 			       return;
	case '(': currentTk = LPAREN_TK;  			       return;
	case ')': currentTk = RPAREN_TK;  			       return;
	case '#':
	case '$':
	case '&':
	case '*':
	case '+':
	case '.':
	case '/':
	case '!':
	case '?':
	case '@':
	case '^':
	case '|':
	case '~': currentTk = OP_TK;                                   return;
	case '-':
	    if (input.charAt(tkStart+1) == '>'){
		tkLength  = 2;
		currentTk = ARROW_TK;                                  return;
	    }
	    currentTk = OP_TK;                                         return;
	default:
	    throw new ParseException (this, "Unexpected Character >>>>>" + 
				            fstChar + "<<<<<");
	}    
    }

    /**
     * Registriert einen neuen Bezeichner und legt ihn in knownIdents ab.
     * Falls der Name schon vergeben ist, wird eine ParseException geworfen.
     *
     * Dies wird bei der Deklaration von Sorten-, Funktions- und Variablen-
     * bezeichnern verwendet, deren Token wird dann auf [SFV]IDENT_TK
     * gesetzt.
     *
     * @param id Der Name des neuen Bezeichners
     * @param tk der Typ des neuen Bezeichners
     *
     * @exception ParseException
     **/
    public void registerIdent(String id, int tk)
	throws ParseException
    {
	if (knownIdents.get(id) != null)
	    throw new ParseException (this, "Registering ident twice!");
	knownIdents.put(id, new Integer(tk));
    }

    /**
     * Liefert den Text des aktuellen Tokens
     *
     * @result der Text des aktuellen Tokens als String
     **/
    public String getTokenText()
    {
	if (tkStart + tkLength < inputLength)
	    return input.substring(tkStart, tkStart + tkLength);
	else
	    return "";
    }

    /**
     * Liefert den numerischen Wert des aktuellen Tokens, falls moeglich;
     * falls das Token keinen numerischen Wert haben sollte (expectNumber
     * ist false), wird eine ParseException geworfen.
     *
     * @result der numerische Wert des aktullen Tokens
     * @exception ParseException
     **/
    public int getTokenVal()
	throws ParseException
    {
	if (!expectNumber)
	    throw new ParseException (this, "Not expecting numbers!");
	try {
	    return Integer.parseInt (getTokenText());
	}
	catch (Exception e) {
	    throw new ParseException (this, "Error in parsing number!");
	}
    }

    /**
     * Liest den gesamten Input eines Readers in einen String;
     * Dies ist eine Hilfsmethode fuer den entsprechenden Konstruktor.
     * Falls das Lesen vom Reader fehlschlaegt (d.h. eine entsprechende 
     * exception kommt), wird eine ParseException geworfen.
     *
     * @result der Inhalt des Readers als String
     * @exception ParseException
     **/
    protected static String readIntoString (Reader in)
	throws ParseException
    {
	StringBuffer sb = new StringBuffer();
	try {
	    int inchar = in.read();
	    while (inchar != -1){
		sb.append((char)inchar);
		inchar = in.read();
	    }
	}
	catch (Exception e) {
	    throw new ParseException (null, "Error while reading input");
	}
	return new String(sb);
    }

    /**
     * initialisiert die hashtable knownIdents mit den Schluesselwoertern
     * aus der Spezifikation wie NAME, MODE,...
     **/
    protected void initKnownIdents ()
    {
	knownIdents = new Hashtable();
	knownIdents.put("NAME",        new Integer(NAME_TK));
	knownIdents.put("MODE",        new Integer(MODE_TK));
	knownIdents.put("SORT",        new Integer(SORT_TK));       
	knownIdents.put("SORTS",       new Integer(SORT_TK));       
	knownIdents.put("SIGNATURE",   new Integer(SIGNATURE_TK));  
	knownIdents.put("ORDERING",    new Integer(ORDERING_TK));   
	knownIdents.put("VARIABLE",    new Integer(VARIABLE_TK));   
	knownIdents.put("VARIABLES",   new Integer(VARIABLE_TK));   
	knownIdents.put("EQUATION",    new Integer(EQUATION_TK));   
	knownIdents.put("EQUATIONS",   new Integer(EQUATION_TK));   
	knownIdents.put("CONCLUSION",  new Integer(CONCLUSION_TK)); 
	knownIdents.put("CONCLUSIONS", new Integer(CONCLUSION_TK)); 
	knownIdents.put("COMPLETION",  new Integer(COMPLETION_TK)); 
	knownIdents.put("CONFLUENCE",  new Integer(CONFLUENCE_TK)); 
	knownIdents.put("CONVERGENCE", new Integer(CONVERGENCE_TK));
	knownIdents.put("PROOF",       new Integer(PROOF_TK));      
	knownIdents.put("REDUCTION",   new Integer(REDUCTION_TK));  
	knownIdents.put("TERMINATION", new Integer(TERMINATION_TK));
	knownIdents.put("KBO",         new Integer(KBO_TK));        
	knownIdents.put("LPO",         new Integer(LPO_TK));        
    }

    /**
     * Ueberspringt whitespace und Kommentare in der Spezifikation.
     **/
    protected void eatWhiteSpace()
    {
	while (tkStart < inputLength && 
	       (Character.isWhitespace(input.charAt(tkStart)) ||
		input.charAt(tkStart) == '%')){
	    // Skip comments
	    if (input.charAt(tkStart) == '%'){
		tkStart++;
		while (tkStart < inputLength && 
		       input.charAt(tkStart) != '\n')
		    tkStart++;
	    }
	    // or ordinary whitespace
	    else 
		tkStart++;
	}
    }

    /**
     * Scannt eine Zahl, d.h. eine Ziffernfolge.
     **/
    protected void scanNumber()
    {
	while (Character.isDigit(input.charAt(tkStart + tkLength)))
	    tkLength++;
	currentTk = NUMBER_TK;
    }

    /**
     * Scannt einen Bezeichner oder ein Schluesselwort.
     **/ 
    protected void scanIdentOrKeyword()
    {
	while (Character.isLetterOrDigit(input.charAt(tkStart + tkLength))||
	       input.charAt(tkStart + tkLength) == '_')
	    tkLength++;
	String id = getTokenText();
	Integer i = (Integer) knownIdents.get(id);
	if (i != null)
	    currentTk = i.intValue();
	else
	    currentTk = IDENT_TK;
    }

    /**
     * Gibt den internen Zustand des Scanners zurueck, d.h. den Input mit 
     * Markierung fuer die aktuelle Scannerposition sowie die bisher bekannten
     * Bezeichner. Dies ist fuer die Ausgabe bei ParseExceptions gedacht.
     **/
    public String scannerState()
    {
	if (tkStart + tkLength < inputLength)
	    return input.substring(0,tkStart) +">>>>>" +
		input.substring(tkStart, tkStart + tkLength) +"<<<<<" +
		input.substring(tkStart + tkLength, inputLength) + knownIdents;
	else
	    return input + ">>>>><<<<<" + knownIdents;
    }
    /**
     * main-Prozedur fuer Test-Zwecke: 
     *
     * Das erste uebergebene Argument wird als Dateibezeichner
     * interpretiert, die Datei geoeffnet und alle Tokens werden der
     * Reihe nach mit ihrem int-Wert und dem dazugehoerigen TokenText
     * ausgegeben.
     **/
    public static void main (String [] argv){
	try {
	    Scanner scanner = new Scanner (new FileReader (argv[0]));
	    do {
		System.out.println (
                  "Tk " + scanner.nextToken() + 
		  " TkText " + scanner.getTokenText());
		scanner.eatToken();
	    } while (scanner.nextToken() != Scanner.EOI_TK);
	}
	catch (Exception e){
	    System.out.println ("Exception: " + e);
	}
    }

}
