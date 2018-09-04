/*
 *      Datei: Parser.java
 */

package basis;

import java.util.Vector;
import java.util.Iterator;
import java.io.Reader;
import java.io.FileReader;

/**
 * Der Parser uebernimmt das Einlesen einer Spezifikation nach dem in
 * der Ausarbeitung vorgestellte Spezifikationsformat
 * 
 * @author Bernd Loechner <loechner@informatik.uni-kl.de>
 *
 * @version $Id: Parser.java,v 1.7 1999/05/15 10:44:02 rsprak1 Exp $
 **/

public class Parser
{
    protected Scanner scanner;

    protected String name;
    protected int mode;
    protected Sort sort;
    protected Signature sig;
    protected Vector idvec;
    protected Vector ordvec;
    protected TermPair[] eqns;
    protected TermPair[] goals;

    /**
     * Konstruktor: Erzeugt einen Parser mit einem vorgegebenen Reader
     * als Input
     *
     * @param in Der Reader, mit dem die Spezifikation eingelesen wird
     * @exception ParseException Falls es beim Einlesen Probleme gibt.
     **/
    public Parser(Reader in)
	throws ParseException
    {
	scanner = new Scanner(in);
	idvec   = new Vector();
	ordvec  = new Vector();
    }

    /**
     * Konstruktor: Erzeugt einen Parser mit einem vorgegebenen String,
     * der eine Spezifikation enthalten sollte
     *
     * @param in Der String mit der Spezifikation
     **/
    public Parser(String in)
	throws ParseException
    {
	scanner = new Scanner(in);
	idvec   = new Vector();
	ordvec  = new Vector();
    }

    /**
     * Erzeugt aus der gegebenen Spezifikation mit Hilfe eines Scanners ein
     * XSpec-Objekt.
     *
     * @exception ParseException Falls es irgendeinen Parse-Fehler gibt.
     * @exception IllegalWeightFunctionException Falls Die Gewichtsfuntkion unzulaessig ist.
     * @return Die fertige XSpec
     **/
    public XSpec parseXSpec()
	throws ParseException, IllegalWeightFunctionException
    {
	parseExample();
	return mkXSpec();
    }

    /**
     * Hilfsmethode fuer parseXSpec: Konstruiert mit dem geparsten Namen,
     * Mode usw. ein neues XSpec-Objekt.
     *
     * @return Die fertige XSpec.
     **/
    private XSpec mkXSpec()
    {
	XSpec xspec    = new XSpec();
	xspec.name     = name;
	xspec.mode     = mode;
	xspec.spec     = new Specification(sig, eqns); 
	xspec.goals    = goals;
	if (ordvec.size() == 0)  
	    xspec.ordering = null;
	else
	    xspec.ordering = (TermOrdering) ordvec.elementAt(0);
	return xspec;
    }

    /**
     * Hilfsmethode fuer parseXSpec: Parst die Spezifikation Abschnittsweise
     *
     * @exception ParseException Falls es irgendeinen Parse-Fehler gibt.
     * @exception IllegalWeightFunctionException Falls Die Gewichtsfuntkion unzulaessig ist.
     **/
    private void parseExample()
	throws ParseException, IllegalWeightFunctionException
    {
	parseName();
	parseMode();
	parseSort();
	parseSignature();
	parseOrdering();
	parseVariables();
	parseEquations();
	parseGoals();
    }

    /**
     * Hilfsmethode fuer parseExample: Liest den Namen der Spezifikation
     * ein
     *
     * @exception ParseException falls nicht das Schluesselwort NAME 
     *   als erstes kommt oder nach NAME kein gueltiger Name kommt
     **/
    private void parseName()
	throws ParseException
    {
	scanner.acceptToken(Scanner.NAME_TK);
	if (scanner.nextToken() != Scanner.IDENT_TK)
	    throw new ParseException(scanner, "Name identifier expected");
	name = scanner.getTokenText();
	scanner.eatToken();
    }

    /**
     * Hilfsmethode fuer parseExample: Liest den Mode der Spezifikation
     * ein
     *
     * @exception ParseException falls nicht das Schluesselwort MODE
     *   als erstes kommt oder nach MODE kein gueltiger Mode kommt
     **/
    private void parseMode()
	throws ParseException
    {
	scanner.acceptToken(Scanner.MODE_TK);
	
	if      (scanner.isToken(Scanner.COMPLETION_TK))  
	    mode = XSpec.MODE_COMPLETION;
	else if (scanner.isToken(Scanner.CONFLUENCE_TK))  
	    mode = XSpec.MODE_CONFLUENCE;
	else if (scanner.isToken(Scanner.CONVERGENCE_TK)) 
	    mode = XSpec.MODE_CONVERGENCE;
	else if (scanner.isToken(Scanner.PROOF_TK))       
	    mode = XSpec.MODE_PROOF;
	else if (scanner.isToken(Scanner.REDUCTION_TK))   
	    mode = XSpec.MODE_REDUCTION;
	else if (scanner.isToken(Scanner.TERMINATION_TK)) 
	    mode = XSpec.MODE_TERMINATION;
	else 
	    throw new ParseException(scanner, "Mode specifier expected");
    }

    /**
     * Hilfsmethode fuer parseExample: Liest die Sorte der Spezifikation
     * ein
     *
     * @exception ParseException falls nicht das Schluesselwort SORT/SORTS
     *   als erstes kommt oder nach SORT(S) kein Bezeichner kommt. Ausserdem
     *   falls mehr als eine Sorte spezifiziert wird (Einschraenkung fuer das
     *   Praktikum).
     **/
    private void parseSort()
	throws ParseException
    {
	scanner.acceptToken(Scanner.SORT_TK);
	if (scanner.nextToken() != Scanner.IDENT_TK)
	    throw new ParseException(scanner, "Sort identifier expected");
	String sortname = scanner.getTokenText();
	scanner.registerIdent(sortname, Scanner.SIDENT_TK);
	sort = new Sort(sortname);
	scanner.eatToken();
	if (scanner.nextToken() == Scanner.IDENT_TK)
	    throw new ParseException(scanner, "Only one sort at the moment");
    }

    /**
     * Hilfsmethode fuer parseExample: Liest die Signatur ein
     **/
    private void parseSignature()
	throws ParseException
    {
	scanner.acceptToken(Scanner.SIGNATURE_TK);
	sig = new Signature(sort);
	do 
	    parseFuncDecl();
	while (scanner.nextToken() == Scanner.IDENT_TK ||
	       scanner.nextToken() == Scanner.OP_TK);
    }

    /**
     * Hilfsmethode fuer parseExample: Liest die Ordnung ein
     *
     * @exception ParseException
     * @exception IllegalWeightFunctionException Falls Die Gewichtsfuntkion unzulaessig ist.
     **/
    private void parseOrdering()
	throws ParseException, IllegalWeightFunctionException
    {
	scanner.acceptToken(Scanner.ORDERING_TK);
	while (scanner.nextToken() == Scanner.KBO_TK ||
	       scanner.nextToken() == Scanner.LPO_TK   )
	    parseOrdDecl();
	if (ordvec.size() > 1)
	    throw new ParseException(scanner, "No lexed orderings at the moment");
    }    

    /**
     * Hilfsmethode fuer parseExample: Liest die Variablen ein
     **/
    private void parseVariables()
	throws ParseException
    {
	scanner.acceptToken(Scanner.VARIABLE_TK);
	while (scanner.nextToken() == Scanner.IDENT_TK)
	    parseVarDecl();
    }    

    /**
     * Hilfsmethode fuer parseExample: Liest die Gleichungen ein
     **/
    private void parseEquations()
	throws ParseException
    {
	scanner.acceptToken(Scanner.EQUATION_TK);
	eqns = parseEqnList();
    }    

    /**
     * Hilfsmethode fuer parseExample: Liest die Beweisziele ein
     **/
    private void parseGoals()
	throws ParseException
    {
	scanner.acceptToken(Scanner.CONCLUSION_TK);
	goals = parseEqnList();
    }    

    /**
     * Hilfsmethode fuer parseSignature: Liest eine Funktionsdeklaration
     * ein
     **/
    private void parseFuncDecl()
	throws ParseException
    {
	int arity = 0;
	parseIdentList(true);
	scanner.acceptToken(Scanner.COLON_TK);
	while (scanner.isToken(Scanner.SIDENT_TK))
	    arity++;
	scanner.acceptToken(Scanner.ARROW_TK);
	scanner.acceptToken(Scanner.SIDENT_TK);

	registerFIdents(arity);
    }

    /**
     * Hilfsmethode fuer parseVariables: Liest eine Variablendeklaration
     * ein
     **/
    private void parseVarDecl()
	throws ParseException
    {
	parseIdentList(false);
	scanner.acceptToken(Scanner.COLON_TK);
	scanner.acceptToken(Scanner.SIDENT_TK);

	registerVIdents();
    }

    /**
     * Hilfsmethode fuer parseFuncDecl und parseVarDecl: liest eine Liste
     * von Bezeichnern und evtl. Operatorsymbolen ein
     *
     * @param allowOpTks Gibt an, ob Operatorsymbole in der Liste erlaubt sind.
     *
     * @exception ParseException
     **/
    private void parseIdentList(boolean allowOpTks)
	throws ParseException
    {
	do {
	    if (!(scanner.nextToken() == Scanner.IDENT_TK ||
		  allowOpTks && scanner.nextToken() == Scanner.OP_TK))
		throw new ParseException(scanner, "Identifier expected");
	    idvec.add(scanner.getTokenText());
	    scanner.eatToken();
	}
	while (scanner.isToken(Scanner.COMMA_TK));
    }

    /**
     * Hilfsmethode fuer parseFuncDecl: registriert die Bezeichner ,
     * die sich nach parseIdentList in idvec gesammelt haben, als 
     * Funktionssymbole der vorgegebenen Stelligkeit; idvec wird dann
     * geleert.
     *
     * @param arity die Stelligkeit der neuen Funktionssymbole
     *
     * @exception ParseException
     **/
    private void registerFIdents(int arity)
	throws ParseException
    {
	Iterator i = idvec.iterator();
	while (i.hasNext()){
	    try {
		String fid = (String) i.next();
		FunctionSymbol f = new FunctionSymbol(fid, arity);
		scanner.registerIdent(fid, Scanner.FIDENT_TK);
		sig.addFunction(f);
	    }
	    catch (Exception e){
		throw new ParseException(scanner, "Function symbol already defined");
	    }
	}  
	idvec.clear();
    }    

    /**
     * Hilfsmethode fuer parseVarDecl: registriert die Bezeichner ,
     * die sich nach parseIdentList in idvec gesammelt haben, als
     * Variablensymbole; idvec wird dann geleert.
     *
     * @exception ParseException
     **/
    private void registerVIdents()
	throws ParseException
    {
	Iterator i = idvec.iterator();
	while (i.hasNext()){
	    try {
		String vid = (String) i.next();
		VariableSymbol v = new VariableSymbol(vid);
		scanner.registerIdent(vid, Scanner.VIDENT_TK);
	    }
	    catch (Exception e){
		throw new ParseException(scanner, "Variable symbol already defined");
	    }
	}
	idvec.clear();
    } 

    /**
     * Hilfsmethode fuer parseOrdering: liest die Parameter (Praezedenz, ggf. 
     * Gewichtsfunktion) fuer eine Ordnung (vorgesehen sind nur KBO oder LPO) 
     * ein.
     *
     * @exception ParseException
     * @exception IllegalWeightFunctionException Falls Die Gewichtsfuntkion unzulaessig ist.
     **/
    private void parseOrdDecl()
	throws ParseException, IllegalWeightFunctionException
    {
	if (scanner.isToken(Scanner.KBO_TK)){
	    WeightFunction phi = parseWeightList();
	    Precedence prec = parsePrec();
	    ordvec.add(new KBO(prec, phi));
	}
	else if (scanner.isToken(Scanner.LPO_TK)){
	    Precedence prec = parsePrec();
	    ordvec.add(new LPO(prec));
	}
	else
	    throw new ParseException (scanner, "KBO or LPO expected");
    }

    /**
     * Hilfsmethode fuer parseOrdDecl: liest eine Liste von Funktionssymbolen
     * mit zugehoerigen Gewichten ein - fuer KBO
     *
     * @return Eine fertige WeightFunction mit den eingelesenen Gewichten
     * @exception IllegalWeightFunctionException Falls Die Gewichtsfuntkion unzulaessig ist.
     **/
    private WeightFunction parseWeightList()
	throws ParseException, IllegalWeightFunctionException
    {
	Vector fs = new Vector();
	Vector ws = new Vector();
	do 
	    parseWeight(fs,ws);
	while (scanner.isToken(Scanner.COMMA_TK));
	return new WeightFunction(fs,ws);
    }

    /**
     * Hilfsmethode fuer parseWeightList: liest ein Funktionssymbol und 
     * Gewicht und haengt sie an die entsprechenden Vectors an.
     *
     * @param fs der Vector fuer die Funktionssymbole
     * @param ws der Vector fuer die Gewichte
     *
     * @exception ParseException
     **/
    private void parseWeight(Vector fs, Vector ws)
	throws ParseException
    {
	fs.add(parseFunctionSymbol());
	scanner.setExpectNumber(true);
	scanner.acceptToken(Scanner.EQUALS_TK);
	if (scanner.nextToken() != Scanner.NUMBER_TK)
	    throw new ParseException(scanner, "Weight expected");
	ws.add(new Integer(scanner.getTokenVal()));
	scanner.setExpectNumber(false);
	scanner.eatToken();
    }

    /**
     * Hilfsmethode fuer parseOrdDecl: Liest eine Praezedenz ein
     *
     * @return Die fertige Praezedenz
     * 
     * @exception ParseException
     **/
    private Precedence parsePrec()
	throws ParseException
    {
	Vector chains = new Vector();
	while (scanner.nextToken() == Scanner.FIDENT_TK ||
	       scanner.nextToken() == Scanner.OP_TK)
	    chains.add(parsePrecChain());
	return new Precedence(chains);
    }

    /**
     * Hilfsmethode fuer parsePrec: Liest eine Kette (d.h. totale Teilordnung)
     * der Praezedenz ein.
     *
     * @return Eine Kette in Form eines Vectors von Funktionssymbolen
     *
     * @exception ParseException
     **/
    private Vector parsePrecChain()
	throws ParseException
    {
	Vector chain = new Vector();
	do 
	    chain.add(parseFunctionSymbol());
	while (scanner.isToken(Scanner.GREATER_TK));
	return chain;
    }

    /**
     * Hilfsmethode fuer parsePrecChain: Liest das naechste Funktionssymbol
     * einer Kette ein
     *
     * @return das naechste Funktionssymbol
     *
     * @exception ParseException
     **/
    private FunctionSymbol parseFunctionSymbol()
	throws ParseException
    {
	if (!(scanner.nextToken() == Scanner.FIDENT_TK || 
	      scanner.nextToken() == Scanner.OP_TK))
	    throw new ParseException(scanner, "Function symbol expected ");
	FunctionSymbol f = sig.getFunction(scanner.getTokenText());
	if (f == null)
	    throw new ParseException(scanner, "Unknown function symbol??");
	scanner.eatToken();
	return f;
    }

    /**
     * Hilfsmethode fuer parseEquations und parseGoals: Liest ein array von
     * Termpaaren ein.
     *
     * @return ein array mit Termpaaren
     *
     * @exception ParseException
     **/
    private TermPair[] parseEqnList()
	throws ParseException
    {
	Vector vec = new Vector();
	while (scanner.nextToken() == Scanner.VIDENT_TK ||
	       scanner.nextToken() == Scanner.FIDENT_TK ||
	       scanner.nextToken() == Scanner.OP_TK     ||
	       scanner.nextToken() == Scanner.LPAREN_TK   )
	    parseTermPair(vec);
	return vectorToTermPairs(vec);
    }

    /**
     * Hilfsmethode fuer parseEqnList: wandelt einen Vector von Termpaaren
     * in ein array von Termpaaren um
     *
     * @param vec Vector mit Termpaaren
     *
     * @return Ein array, das die selben Termpaare wie vec enthaelt.
     **/
    private TermPair[] vectorToTermPairs(Vector vec)
    {
	int size = vec.size();
	TermPair[] tps = new TermPair[size];
	for (int i = 0; i < size; i++)
	    tps[i] = (TermPair) vec.elementAt(i);
	return tps;
    }

    /**
     * Hilfsmethode fuer parseEqnList: Parst ein neues Termpaar und haengt
     * es an einen bestehenden Vector an.
     *
     * @param vec Der Vector, an den das neue Termpaat angefuegt werden soll
     *
     * @exception ParseException
     **/ 
    private void parseTermPair(Vector vec)
	throws ParseException
    {
	Term lhs = parseTerm();
	scanner.acceptToken(Scanner.EQUALS_TK);
	Term rhs = parseTerm();
	vec.add(new TermPair(lhs,rhs));
    }

    /**
     * Hilfsmethode fuer parseTermPair: Parst einen Term
     *
     * @return der neue Term
     *
     * @exception ParseException
     **/
    private Term parseTerm()
	throws ParseException
    {
	int tk = scanner.nextToken();
	if (tk == Scanner.VIDENT_TK)
	    return new Term (parseVariableSymbol());
	if (tk == Scanner.LPAREN_TK)
	    return parseInfixTerm();
	return parsePrefixTerm();
    }

    /**
     * Hilfsmethode fuer parseTerm: Parst ein Variablensymbol.
     *
     * @return das geparste Variablensymbol
     *
     * @exception ParseException
     **/
    private VariableSymbol parseVariableSymbol()
	throws ParseException
    {
	if (scanner.nextToken() != Scanner.VIDENT_TK)
	    throw new ParseException(scanner, "Variable symbol expected");
	VariableSymbol v = 
	    VariableSymbol.variableFromName(scanner.getTokenText());
	if (v == null)
	    throw new ParseException(scanner, "Unknown variable symbol??");
	scanner.eatToken();
	return v;
    }

    /**
     * Hilfsmethode fuer parseTerm: Parst einen Term in Infix-Schreibweise.
     *
     * @return der geparste Term
     *
     * @exception ParseException
     **/
    private Term parseInfixTerm()
	throws ParseException
    {
	Vector args = new Vector(2);
	scanner.acceptToken(Scanner.LPAREN_TK);
	args.add(parseTerm());
	if (scanner.nextToken() != Scanner.OP_TK)
	    throw new ParseException(scanner, "Operator symbol expected");
	FunctionSymbol f = parseFunctionSymbol();
	args.add(parseTerm());
	scanner.acceptToken(Scanner.RPAREN_TK);
	return mkTerm(f, args);
    }

    /**
     * Hilfsmethode fuer parseTerm: Parst einen Term in Prefix-Schreibweise.
     *
     * @return der geparste Term
     *
     * @exception ParseException
     **/
    private Term parsePrefixTerm()
	throws ParseException
    {
	FunctionSymbol f = parseFunctionSymbol();
	if (f.getArity() == 0)
	    return parseConstant(f);
	Vector args = parseArgs();
	return mkTerm(f, args);
    }

    /**
     * Hilfsmethode fuer parsePrefixTerm: Parst ein Konstantensymol,
     * d.h. ein nullstelliges Funktionssymbol. Dabei muss ein Lookahead
     * gemacht werden, um ein (optionales) Paar Klammern (()) :) zu
     * verschlucken.
     *
     * @return Ein Konstantenterm
     *
     * @exception ParseException
     **/
    private Term parseConstant(FunctionSymbol f)
	throws ParseException
    {
	if (scanner.nextToken() == Scanner.LPAREN_TK &&
	    scanner.peekToken() == Scanner.RPAREN_TK) {
	    scanner.eatToken();
	    scanner.eatToken();
	}
	return mkTerm (f, null);
    }

    /**
     * Hilfsmethode fuer parsePrefixTerm: Parst den die Argumente des Toplevel-
     * Funktionssymbols, also die direkten Subterme.
     *
     * @return Ein Vector mit Argumenten
     *
     * @exception ParseException
     **/
    private Vector parseArgs()
	throws ParseException
    {
	scanner.acceptToken(Scanner.LPAREN_TK);
	Vector args = new Vector();
	args.add(parseTerm());
	while (scanner.isToken(Scanner.COMMA_TK))
	    args.add(parseTerm());
	scanner.acceptToken(Scanner.RPAREN_TK);
	return args;
    }

    /**
     * Hilfsmethode fuer parseInfixTerm und parsePrefixTerm: konstruiert aus
     * dem geparsten Funktionssymbol und dem Argumentvector den fertigen Term.
     *
     * @param f das Toplevel-Funktionssymbol des neuen Terms
     * @param args die Argumente fuer f
     *
     * @return Ein neuer Term mit Toplevel-Symbol f und Subtermen args
     *
     * @exception ParseException
     **/
    private Term mkTerm (FunctionSymbol f, Vector args)
	throws ParseException
    {
	try {
	    return new Term (f,args);
	}
	catch (ArityMismatchException e){
	    throw new ParseException(scanner, "Wrong number of arguments to function symbol");
	}
    }

    /**
     * main-Methode fuer Testzwecke
     **/
    public static void main (String [] argv){
	try {
	    Parser parser = new Parser (new FileReader (argv[0]));
	    XSpec xspec = parser.parseXSpec();
	    System.out.println("" + xspec);
	}
	catch (Exception e){
	    System.err.println ("Exception: " + e);
	    e.printStackTrace();
	}
    }
}
