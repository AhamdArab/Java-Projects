package de.uniwue.jpp.ook;

/**
 * Eine einfache RuntimeException für außergewöhnliche Fälle beim Ausführen des Ook!-Programms.
 */
public class ExecutionException extends RuntimeException {

    public ExecutionException(String s) {
        super(s);
    }
}
