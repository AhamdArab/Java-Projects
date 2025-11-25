package de.uniwue.jpp.ook;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;

public class OokShell implements Shell {
    Ook handle;
    InputStream input;
    OutputStream output;
    InputStream readInput;
    PrintStream printStream;

    public OokShell(Ook handle, InputStream input, OutputStream output, InputStream readInput) {
        this.handle = handle;
        this.input = input;
        this.output = output;
        this.readInput = readInput;
        this.printStream = new PrintStream(output,true);
    }

    @Override
    public Optional<Instruction> parseLine(String line) {
        Util util = handle.createUtil();
        return util.getInstruction(line);
    }

    @Override
    public void prepareUpdate(Instruction instruction) {
        switch (instruction) {
            case Read -> printStream.print("Reading: ");
            case Write -> printStream.print("Writing: ");
            default -> printStream.println(instruction);
        }
    }

    @Override
    public void completeUpdate(Instruction instruction) {
        switch (instruction) {
            case Write -> printStream.println();
            case Read -> {
                try {
                    if (readInput.read() != '\n') {
                        printStream.println("Illegal Input: Insert only one character!");
                    }
                } catch (Exception e) {
                    printStream.println("Error reading input");
                }
            }
        }
    }

    @Override
    public void run() {
        printStream.println("--------------------------");
        printStream.println("  Interactive Ook! Shell  ");
        printStream.println("--------------------------");

        Scanner scanner = new Scanner(input);
        Interpreter interpreter = handle.createInterpreter(
                () -> {
                    try {
                        return readInput.read();
                    } catch (Exception e) {
                        printStream.println("Error reading input");
                        return -1;
                    }
                },
                this::writeOutput);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ("Bananas".equals(line)) {
                printStream.println("Yippee!");
                continue;
            }
            if ("Monkey".equals(line)) {
                printStream.println("Ouch!");
                break;
            }

            Optional<Instruction> instruction = parseLine(line);
            if (instruction.isPresent()) {
                prepareUpdate(instruction.get());
                interpreter.loadInstruction(instruction.get());
                interpreter.update();
                completeUpdate(instruction.get());
            } else {
                printStream.println("Invalid instruction: " + line);
            }
        }
    }
    private void writeOutput(int value) {
        printStream.print((char) value);
    }
}
