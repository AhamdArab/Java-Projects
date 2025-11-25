package de.uniwue.jpp.ook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OokInterpreter implements Interpreter{
    List<Instruction> instructions = new ArrayList<>();
    List<Integer> memory = new ArrayList<>();
    Supplier<Integer> onRead;
    Consumer<Integer> onWrite ;
    int instructionPointer = 0;
    int memoryPointer = 0;
    public OokInterpreter(Supplier<Integer> onRead, Consumer<Integer> onWrite) {
        if (onRead == null || onWrite == null)
            throw new NullPointerException();
        this.onRead = onRead;
        this.onWrite = onWrite;
        memory.add(0);
    }

    @Override
    public void loadInstruction(Instruction instruction) {
        if (instruction == null)
            throw new NullPointerException();
        instructions.add(instruction);
    }

    @Override
    public void loadInstructions(List<Instruction> instructions) {
        if (instructions.contains(null))
            throw new NullPointerException();
        this.instructions.addAll(instructions);
    }

    @Override
    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    @Override
    public List<Integer> getMemory() {
        return Collections.unmodifiableList(memory);
    }

    @Override
    public int getMemoryPointer() {
        return memoryPointer;
    }

    @Override
    public int getInstructionPointer() {
        return instructionPointer;
    }

    @Override
    public boolean reachedEnd() {
        return instructionPointer >= instructions.size();
    }

    @Override
    public void update() {
        if (reachedEnd()) throw new ExecutionException("Reached end of instruction list!");
        Instruction instruction = instructions.get(instructionPointer);
        switch (instruction) {
            case Inc -> increment();
            case Dec -> decrement();
            case PtrInc -> pointerIncrement();
            case PtrDec -> pointerDecrement();
            case Read -> read();
            case Write -> write();
            case Loop -> loop();
            case End -> end();
        }
    }

    @Override
    public void increment() {
        memory.set(memoryPointer, memory.get(memoryPointer) + 1);
        instructionPointer++;
    }

    @Override
    public void decrement() {
        memory.set(memoryPointer, memory.get(memoryPointer) - 1);
        instructionPointer++;
    }

    @Override
    public void pointerIncrement() {
        memoryPointer++;
        if (memoryPointer == memory.size()) {
            memory.add(0);
        }
        instructionPointer++;
    }

    @Override
    public void pointerDecrement() {
        if (memoryPointer == 0)
            throw new ExecutionException("Requested invalid memory address!");
        memoryPointer--;
        instructionPointer++;
    }

    @Override
    public void read() {
        int value = onRead.get();
        if (value != -1) {
            memory.set(memoryPointer, value);
        }
        instructionPointer++;
    }

    @Override
    public void write() {
        int value = memory.get(memoryPointer);
        char character = (char) value;
        onWrite.accept((int) character);
        instructionPointer++;
    }

    @Override
    public void loop() {
        instructionPointer++;
    }

    @Override
    public void end() {
        if (memory.get(memoryPointer) != 0) {
            int loopDepth = 1;
            for (int i = instructionPointer - 1; i >= 0; i--) {
                if (instructions.get(i) == Instruction.End)
                    loopDepth++;
                else if (instructions.get(i) == Instruction.Loop)
                    loopDepth--;
                if (loopDepth == 0) {
                    instructionPointer = i;
                    return;
                }
            }
            throw new ExecutionException("Closing loop without beginning!");
        }
        instructionPointer++;
    }
}
