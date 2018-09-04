/*
 * Datei: Protocol.java
 * Autor: Kindergarten
 * Aenderungen:
 *      27.5.97: Schnittstellenfestlegung
 *               und erste (lauffähige?) Implementierung
 */

package basis;



import java.io.*;
import java.lang.*;
import basis.*;



/** 
 * Die Klasse Protocol dient zum mitprotokollieren der wichtigsten
 * Systemereignisse, sowie der Zeitmessung, wie lange bestimmte Prozesse
 * waehrend des Systemlaufes dauern.
 * Wichtige Systemereignisse sind dabei das Erzeugen, Aendern und Loeschen
 * von Regeln, Gleichungen und Kritischen Paaren; die Entscheidung, ob ein
 * Kritisches Paar konfluent ist oder entwickelt werden muss; ob ein
 * Reduktionsversuch gescheitert ist, oder ob es sich um eine erfolgreiche
 * Reduktion einer Regel, Gleichung oder eines Kritischen Paares handelt.
 * Ferner sind die Laufzeiten folgender Prozesse interessant:
 * + Reduktionen (inkl. Reduktionsversuche)
 * + Unifikationen (inkl. Unifikationsversuche)
 * + Orientationen
 * und natuerlich die Laufzeit des Systems selbst.
 * @author Kindergarten
 * @version 1.0, 27.5.97
 **/



public class Protocol
{

  // public class methods

  /**
   * Initialisiert die Protokollklasse und gibt an, an welche Stelle das
   * Protokoll geschrieben werden soll.
   *
   * @param oStream OutputStream, auf den das Protokoll geschrieben wird
   **/
  public static void initProtocol(OutputStream oStream)
  {
    Protocol.numberOfRules = 0;
    Protocol.numberOfEquations = 0;
    Protocol.numberOfUnifications = 0;
    Protocol.numberOfCriticalPairs = 0;
    Protocol.numberOfConfluentCriticalPairs = 0;
    Protocol.numberOfDevelopedCriticalPairs = 0;
    Protocol.numberOfInvalidCriticalPairs = 0;
    Protocol.numberOfReductionAttempts = 0;
    Protocol.numberOfRuleReductions = 0;
    Protocol.numberOfEquationReductions = 0;
    Protocol.numberOfCriticalPairReductions = 0;
    Protocol.timeForReduction = 0;
    Protocol.timeForUnification = 0;
    Protocol.timeForOrientation = 0;
    Protocol.globalTimer = new Timer();  
    Protocol.currentTimer = new Timer();
    Protocol.destinationStream = oStream;
    //    Protocol.logStream = new PrintWriter(Protocol.destinationStream);
    Protocol.logStream = (PrintStream) oStream;
  }

  /**
   * Meldet die Erzeugung einer neuen Regel und protokolliert diese Regel
   * im logStream.
   *
   * @param rule Regel, die erzeugt wurde.
   **/
  public static void notifyCreation(Rule rule) 
  {
    Protocol.numberOfRules++;
    Protocol.logStream.println("neue regel:\t\t"+numberOfRules+"\t"+rule.toString());
    // Protocol.logStream.println("Aktive Fakten: " + Oberfoerster.getActiveFacts());
  }

  /**
   * Meldet die Erzeugung einer neuen Gleichung und protokolliert diese 
   * Gleichung im logStream.
   *
   * @param equation Gleichung, die erzeugt wurde.
   **/
  public static void notifyCreation(Equation equation) 
  {
    Protocol.numberOfEquations++;
    Protocol.logStream.println("neue gleichung:\t\t"+numberOfEquations+"\t"+equation.toString());
    //  Protocol.logStream.println("Aktive Fakten: " + Oberfoerster.getActiveFacts());
  }

  /**
   * Meldet die Erzeugung eines neuen Kritischen Paares und protokolliert
   * dieses Kritische Paar im logStream. 
   *
   * @param criticalPair Gleichung, die erzeugt wurde.
   **/
  public static void notifyCreation(CriticalPair criticalPair)
  {
      Protocol.numberOfCriticalPairs++;
      //Protocol.logStream.println("neues kritisches paar:\t"+numberOfCriticalPairs+"\t"+criticalPair.toString());
      //Protocol.logStream.println("       entstanden aus: "+criticalPair.getMom().toString()+ " und "+ criticalPair.getDad().toString());
      //Protocol.logStream.println(Oberfoerster.getPassiveFacts());
  }



  /**
   * Meldet die Modifikation einer Regel und protokolliert beide Regeln
   * (die urspruengliche und die veraenderte) im logStream.
   *
   * @param oldRule Gleichung, die modifiziert wurde.
   * @param newRule Gleichung, die modifiziert wurde.
   **/
  public static void notifyModification(Rule oldRule, Rule newRule)
  {
    Protocol.numberOfRules++;
    Protocol.logStream.println("====================");
    Protocol.logStream.println("M - R");
    Protocol.logStream.println(oldRule.toString());
    Protocol.logStream.println("modified to");
    Protocol.logStream.println(newRule.toString());
    Protocol.logStream.println("====================");
  }



  /**
   * Meldet die Modifikation einer Gleichung und protokolliert beide 
   * Gleichungen (die urspruengliche und die veraenderte) im logStream.
   * Falls es sich bei der urspruenglichen Gleichung um eine Zielgleichung
   * handelt, wird dies ebenfalls protokolliert.
   *
   * @param equation Gleichung, die modifiziert wurde.
   * @param isGoal Flag, ob es sich bei der obigen Gleichung um eine
   * Zielgleichung handelt, oder nicht.
   **/
  public static void notifyModification(Equation oldEquation,
					Equation newEquation, boolean isGoal)
  {
    Protocol.numberOfEquations++;
    Protocol.logStream.println("====================");
    Protocol.logStream.println("M - E");
    if (isGoal)
      Protocol.logStream.println("******* GOAL *******");
    Protocol.logStream.println(oldEquation.toString());
    Protocol.logStream.println("modificated to");
    Protocol.logStream.println(newEquation.toString());
    Protocol.logStream.println("====================");
  }



  /**
   * Meldet das Loeschen einer Regel und protokolliert diese Regel im
   * logStream.
   *
   * @param rule Regel, die geloescht wurde.
   **/
  public static void notifyDeletion(Rule rule)
  {
    Protocol.logStream.println("entferne regel:\t\t\t"+rule.toString());
  }
  


  /**
   * Meldet das Loeschen einer Gleichung und protokolliert diese Gleichung
   * im logStream.
   * Falls es sich bei der Gleichung um eine Zielgleichung handelt, wird dies
   * ebenfalls protokolliert.
   *
   * @param equation Regel, die geloescht wurde.
   * @param isGoal Flag, ob es sich bei der obigen Gleichung um eine
   * Zielgleichung handelt, oder nicht.   
   **/
  public static void notifyDeletion(Equation equation)
  {
      Protocol.logStream.println("entferne gleichung:\t\t\t"+equation.toString());
  }

    public static void notifyDeletion(TermPair goal)
    {
	Protocol.logStream.println("entferne zielgleichung:\t\t"+goal.toString());
    }

  /**
   * Meldet den Start eines Unifikationsprozesses, wodurch die Zeitmessung
   * fuer diesen Vorgang getriggert wird.
   **/
  public static void notifyStartUnification()
  {
      Protocol.currentTimer.start();
  }
  


  /**
   * Meldet das Ende eines Unifikationsprozesses, wodurch die Zeitmessung
   * fuer diesen Vorgang gestoppt wird. Die gemessene Zeit wird zu der
   * Gesamtzeit aller Unifikationen addiert.
   **/
  public static void notifyStopUnification()
  {
    Protocol.currentTimer.stop();
    Protocol.timeForUnification += Protocol.currentTimer.getLastDuration();
    Protocol.numberOfUnifications++;
  }


  /**
   * Meldet das Auftreten eines konfluenten Kritischen Paares. Deren Anzahl
   * fliesst in die Statistik ein.
   **/
  public static void notifyConfluentCriticalPair()
  {
    Protocol.numberOfConfluentCriticalPairs++;
  }
  
  /**
   * Meldet das Auftreten eines zu entwickelnden Kritischen Paares. Deren
   * Anzahl fliesst in die Statistik ein.
   **/
  public static void notifyDevelopedCriticalPair()
  {
    Protocol.numberOfDevelopedCriticalPairs++;
  }
  
  public static void notifyInvalidCriticalPair()
  {
    Protocol.numberOfInvalidCriticalPairs++;
  }
  


  /**
   * Meldet den Start eines Reduktionsprozesses, wodurch die Zeitmessung
   * fuer diesen Vorgang getriggert wird.
   **/
  public static void notifyStartReduction()
  {
    Protocol.currentTimer.start();
  }
  


  /**
   * Meldet den Misserfolg eines Reduktionsprozesses, wodurch die Zeitmessung
   * fuer den Vorgang der Reduktion gestoppt wird und die Dauer des Versuchs
   * zu der Gesamtzeit aller Reduktionen addiert wird.
   * Ferner wird die Anzahl der Reduktionsversuche inkrementiert.
   **/
  public static void notifyFailedReduction()
  {
    Protocol.currentTimer.stop();
    Protocol.timeForReduction += Protocol.currentTimer.getLastDuration();
    Protocol.numberOfReductionAttempts++;
  }
  


  /**
   * Meldet den Erfolg eines Reduktionsprozesses auf einer Regel. Die
   * Zeitmessung fuer den Vorgang der Reduktion wird gestoppt und die Dauer
   * zu der Gesamtzeit aller Reduktionen addiert wird.
   * Ferner wird die Anzahl der Regel-Reduktionen inkrementiert.
   **/
  public static void notifyStopRuleReduction()
  {
    Protocol.currentTimer.stop();
    Protocol.timeForReduction += Protocol.currentTimer.getLastDuration();
    Protocol.numberOfRuleReductions++;
  }
  


  /**
   * Meldet den Erfolg eines Reduktionsprozesses auf einer Gleichung. Die
   * Zeitmessung fuer den Vorgang der Reduktion wird gestoppt und die Dauer
   * zu der Gesamtzeit aller Reduktionen addiert wird.
   * Ferner wird die Anzahl der Gleichungs-Reduktionen inkrementiert.
   **/
  public static void notifyStopEquationReduction()
  {
    Protocol.currentTimer.stop();
    Protocol.timeForReduction += Protocol.currentTimer.getLastDuration();
    Protocol.numberOfEquationReductions++;
  }
  


  /**
   * Meldet den Erfolg eines Reduktionsprozesses auf einem Kritischen Paar.
   * Die Zeitmessung fuer den Vorgang der Reduktion wird gestoppt und die
   * Dauer zu der Gesamtzeit aller Reduktionen addiert wird.
   * Ferner wird die Anzahl der Kritischen Paar-Reduktionen inkrementiert.
   **/
  public static void notifyStopCriticalPairReduction()
  {
    Protocol.currentTimer.stop();
    Protocol.timeForReduction += Protocol.currentTimer.getLastDuration();
    Protocol.numberOfCriticalPairReductions++;
  }
  


  /**
   * Meldet den Start eines Orientierungsprozesses, wodurch die Zeitmessung
   * fuer diesen Vorgang getriggert wird.
   **/
  public static void notifyStartOrientation()
  {
    Protocol.currentTimer.start();
  }
  


  /**
   * Meldet das Ende eines Orientierungsprozesses, wodurch die Zeitmessung
   * fuer diesen Vorgang gestoppt wird. Die gemessene Zeit wird zu der
   * Gesamtzeit aller Orientierungen addiert.
   **/
  public static void notifyStopOrientation()
  {
    Protocol.currentTimer.stop();
    Protocol.timeForOrientation += Protocol.currentTimer.getLastDuration();
  }
  


  /**
   * Meldet den Start des Systems, wodurch die Zeitmessung fuer diesen
   * Vorgang getriggert wird.
   **/
  public static void notifyStartSystem()
  {
    Protocol.globalTimer.start();
  }


  
  /**
   * Meldet das Ende des Systemslaufes, wodurch die Zeitmessung fuer diesen
   * Vorgang gestoppt wird.
   **/
  public static void notifyStopSystem()
  {
    Protocol.globalTimer.stop();
    Protocol.dumpStatistics();
  }


  /**
   * Fragt die momentane Laufzeit des Systems ab.
   * @return Laufzeit des Systems in Millisekunden
   **/
  public static long getGlobalRunTime()
  {
    return Protocol.globalTimer.getLastDuration();
  }
  

  
  // private class variables
  private static int numberOfRules;
  private static int numberOfEquations;
  private static int numberOfUnifications;
  private static int numberOfCriticalPairs;
  private static int numberOfConfluentCriticalPairs;
  private static int numberOfDevelopedCriticalPairs;
  private static int numberOfInvalidCriticalPairs;
  private static int numberOfReductionAttempts;
  private static int numberOfRuleReductions;
  private static int numberOfEquationReductions;
  private static int numberOfCriticalPairReductions;
  private static long timeForReduction;
  private static long timeForUnification;
  private static long timeForOrientation;
  private static Timer globalTimer;  
  private static Timer currentTimer;
  //  private static PrintWriter logStream;
  private static OutputStream destinationStream;  
  private static PrintStream logStream;  
  

  // private class methods
  private static void dumpStatistics()
  {
    Protocol.logStream.println("System Statistics");
    Protocol.logStream.println("==============================");
    Protocol.logStream.print("Anzahl erzeugter Regeln : ");
    Protocol.logStream.println(Protocol.numberOfRules);
    Protocol.logStream.print("Anzahl erzeugter Gleichung : ");
    Protocol.logStream.println(Protocol.numberOfEquations);
    Protocol.logStream.print("Anzahl der Unifikationsversuche : ");
    Protocol.logStream.println(Protocol.numberOfUnifications);
    Protocol.logStream.print("Gesamtzeit fuer Unifikationsversuche : ");
    Protocol.logStream.println(Protocol.timeForUnification);
    Protocol.logStream.print("Anzahl aller erzeugten kritischen Paare : ");
    Protocol.logStream.println(Protocol.numberOfCriticalPairs);
    Protocol.logStream.print("  darunter konfluente kritische Paare : ");
    Protocol.logStream.println(Protocol.numberOfConfluentCriticalPairs);
    Protocol.logStream.print("Anzahl gescheiterter Reduktionsversuche : ");
    Protocol.logStream.println(Protocol.numberOfReductionAttempts);

    Protocol.logStream.print("Gesamte Laufzeit des Systems : ");
    Protocol.logStream.println(Protocol.globalTimer.getLastDuration());  
  }
}




class Timer {
  // public part

  /**
   * Konstruktormethode: Initialisiert den Timer
   **/
  public Timer() 
  {
    startTime = 0;
    stopTime = 0;
    lastDuration = 0;
    globalDuration = 0;
  }

  /**
   * Startet den Timer: setzt Startzeit
   **/
  public void start()
  {
    startTime = System.currentTimeMillis();
  }   
  
  /**
   * Haelt den Timer an: setzt Stopzeit und letzten Zeitraum, aktualisiert
   * Gesamtlaufzeit
   **/
  public void stop()
  {
    stopTime = System.currentTimeMillis();
    lastDuration = stopTime - startTime;
    globalDuration += lastDuration;
  }
  
  /**
   * Liefert letzten Zeitraum
   *
   * @return Letzter Zeitraum in Millisekunden
   **/
  public long getLastDuration()
  {
    return lastDuration;
  }
  
  /**
   * Liefert gesamten Zeitraum
   *
   * @return gesamter Zeitraum in Millisekunden
   **/
  public long getGlobalDuration()
  {
    return globalDuration;
  }
  
  /**
   * Liefert letzten Startzeitpunkt
   *
   * @return letzter Startzeitpunkt, in Millisekunden seit 1.1.1970, 0.00 UTC
   **/
  public long getStartTime()
  {
    return startTime;
  }
  
  /**
   * Liefert letzten Stopzeitpunkt
   *
   * @return letzter Stopzeitpunkt, in Millisekunden seit 1.1.1970, 0.00 UTC
   **/
  public long getStopTime()
  {
    return stopTime;
  }
 
  // protected part
  protected long startTime;
  protected long stopTime;
  protected long lastDuration;
  protected long globalDuration;
}






