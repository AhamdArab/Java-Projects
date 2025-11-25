package de.uniwue.jpp.ook;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Interpreter {

    public static Interpreter create(Supplier<Integer> onRead, Consumer<Integer> onWrite){
        //TODO Diese Zeile muss mit dem Konstruktor der konkreten Implementierung ausgetauscht werden.
        return new OokInterpreter(onRead, onWrite);
    }

    public void loadInstruction(Instruction instruction);

    public void loadInstructions(List<Instruction> instructions);

    public List<Instruction> getInstructions();

    public List<Integer> getMemory();

    public int getMemoryPointer();

    public int getInstructionPointer();

    public boolean reachedEnd();

    public void update();

    public void increment();

    public void decrement();

    public void pointerIncrement();

    public void pointerDecrement();

    public void read();

    public void write();

    public void loop();

    public void end();
}
