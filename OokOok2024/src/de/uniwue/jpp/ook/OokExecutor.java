package de.uniwue.jpp.ook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OokExecutor implements Executor {
    Ook handle;

    public OokExecutor(Ook handle) {
        this.handle = handle;
    }

    @Override
    public void execute(Path programPath, Path inputPath, Path outputPath) throws IOException {
        Parser parser = handle.createParser();
        List<Instruction> instructions = parser.parse(Files.readString(programPath));

        try (InputStream inputStream = new FileInputStream(inputPath.toFile());
             OutputStream outputStream = new FileOutputStream(outputPath.toFile())) {
            Interpreter interpreter = handle.createInterpreter(() -> {
                try {
                    return inputStream.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, i -> {
                try {
                    outputStream.write(i);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            interpreter.loadInstructions(instructions);
            while (!interpreter.reachedEnd()) {
                interpreter.update();
            }
        }
    }

    @Override
    public void execute(String[] args) throws IOException {
        if (args.length != 3) throw new IllegalArgumentException();

        execute(Path.of(args[0]), Path.of(args[1]), Path.of(args[2]));
    }
}
