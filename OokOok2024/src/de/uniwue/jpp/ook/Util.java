package de.uniwue.jpp.ook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Util {

    public static Util create() {
        //TODO Diese Zeile muss mit dem Konstruktor der konkreten Implementierung ausgetauscht werden.
        return new OokUtil();
    }

    public Instruction forSymbols(Symbol first, Symbol last);

    public Symbol forToken(String token);

    public Supplier<Integer> buildPipe(InputStream input);

    public Consumer<Integer> buildPipe(OutputStream output);

    public String getInstructionCode(Instruction instruction);

    public Optional<Instruction> getInstruction(String code);
}
