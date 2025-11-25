package de.uniwue.jpp.ook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OokUtil implements Util{
    @Override
    public Instruction forSymbols(Symbol first, Symbol last) {
        if (first == null || last == null)
            throw new NullPointerException();
        return Arrays.stream(Instruction.values())
                .filter(instruction -> instruction.getFirstSymbol() == first && instruction.getLastSymbol() == last)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No instruction is for symbol " + first + " and " + last + " present!"));
    }

    @Override
    public Symbol forToken(String token) {
        if (token == null)
            throw new NullPointerException();
        return Arrays.stream(Symbol.values())
                .filter(symbol -> symbol.getToken().equals(token))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No symbol is for token " + token + " present!"));
    }

    @Override
    public Supplier<Integer> buildPipe(InputStream input) {
        if (input == null)
            throw new NullPointerException();
        return () -> {
            try {
                return input.read();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Consumer<Integer> buildPipe(OutputStream output) {
        if (output == null)
            throw new NullPointerException();
        return (data) -> {
            try {
                output.write(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public String getInstructionCode(Instruction instruction) {

        return instruction.getFirstSymbol().getToken() + instruction.getLastSymbol().getToken();
    }

    @Override
    public Optional<Instruction> getInstruction(String code) {
        if (code == null)
            throw new NullPointerException();
        return Arrays.stream(Instruction.values())
                .filter(instruction -> (instruction.getFirstSymbol().getToken() + instruction.getLastSymbol().getToken()).equals(code))
                .findFirst();
    }
}
