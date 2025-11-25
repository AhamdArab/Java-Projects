package de.uniwue.jpp.ook;

import java.io.IOException;
import java.nio.file.Path;

public interface Executor {

    public static Executor create(Ook handle){
        //TODO Diese Zeile muss mit dem Konstruktor der konkreten Implementierung ausgetauscht werden.
        return new OokExecutor(handle);
    }

    public void execute(Path program, Path input, Path output) throws IOException;

    public void execute(String[] args) throws IOException;

}
