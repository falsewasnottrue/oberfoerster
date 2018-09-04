/*
 * Datei: ParseException.java
 */

package basis;

public class ParseException extends Exception
{
    Scanner scanner;
    String message;
    public ParseException(Scanner s, String msg)
    {
	scanner = s;
	message = msg;
    }
    public String toString()
    {
	return message + "\n" + scanner.scannerState();
    }
}
