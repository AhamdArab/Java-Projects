package de.uniwue.jpp.ook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public interface Shell {

    public static Shell create(Ook handle, InputStream input, OutputStream output, InputStream readInput) {
        //TODO Diese Zeile muss mit dem Konstruktor der konkreten Implementierung ausgetauscht werden.

        return new OokShell(handle, input, output, readInput);
    }

    public Optional<Instruction> parseLine(String line);

    public void prepareUpdate(Instruction instruction);

    public void completeUpdate(Instruction instruction);

    public void run();
}
