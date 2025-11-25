package de.uniwue.jpp.ook;

import java.util.List;

public interface Parser {

    public static Parser create(Ook handle) {
        //TODO Diese Zeile muss mit dem Konstruktor der konkreten Implementierung ausgetauscht werden.
        return new OokParser(handle);
    }

    public List<Instruction> parse(String program);

    public boolean isValid(String program);

    public List<Symbol> asSymbols(String program);

    public List<Instruction> asInstructions(List<Symbol> symbols);

}
