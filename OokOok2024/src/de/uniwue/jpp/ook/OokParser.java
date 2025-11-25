package de.uniwue.jpp.ook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class OokParser implements Parser{
    Util util;

    public OokParser(Ook handle) {
        this.util = handle.createUtil();
    }

    @Override
    public List<Instruction> parse(String program) {
        if (!isValid(program)) {
            throw new IllegalArgumentException("Program is not valid!");
        }
        List<Symbol> symbols = asSymbols(program);
        return asInstructions(symbols);
    }

    @Override
    public boolean isValid(String program) {
        if (program == null)
            throw new NullPointerException();

        String[] tokens = program.trim().split("\\s+");
        if (tokens.length % 2 != 0) return false;

        return IntStream.range(0, tokens.length)
                .filter(i -> i % 2 == 0)
                .allMatch(i -> {
                    try {
                        Instruction instruction = util.forSymbols(util.forToken(tokens[i]), util.forToken(tokens[i + 1]));
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                });
    }

    @Override
    public List<Symbol> asSymbols(String program) {
        if (program == null)
            throw new NullPointerException();
        if (!isValid(program)) {
            throw new IllegalArgumentException("Program is not valid!");
        }
        List<Symbol> symbols = new ArrayList<>();
        String[] tokens = program.split("\\s+");
        for (String token : tokens) {
            symbols.add(util.forToken(token));
        }
        return symbols;
    }

    @Override
    public List<Instruction> asInstructions(List<Symbol> symbols) {
        if (symbols == null)
            throw new NullPointerException();
        List<Instruction> instructions = new ArrayList<>();
        for (int i = 0; i < symbols.size(); i += 2) {
            if (i + 1 >= symbols.size()) {
                throw new IllegalArgumentException("List of symbols is malformed!");
            }
            Instruction instruction = util.forSymbols(symbols.get(i), symbols.get(i + 1));
            instructions.add(instruction);
        }
        return instructions;
    }
}

