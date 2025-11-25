package de.uniwue.jpp.compiler.eval;

import java.util.List;

import de.uniwue.jpp.compiler.lowering.Instruction;
import de.uniwue.jpp.compiler.lowering.Operand;

public class Evaluator {
    List<Instruction> instructions;
    int[] memory;

    public Evaluator(List<Instruction> instructions, int stackSize) {
        this(instructions, new int[stackSize]);
    }

    public Evaluator(List<Instruction> instructions, int[] memory) {
        this.instructions = instructions;
        this.memory = memory;
    }

    public int[] getMemory() {
        return memory;
    }

    public int getValue(Operand operand) {
        if (operand.isLocal()) {
            return memory[operand.getValue()];
        } else {
            return operand.getValue();
        }
    }

    public void evaluateInstruction(Instruction instruction) {
        int result = instruction.getResult();
        Operand left = instruction.getLeft();
        Operand right = instruction.getRight();

        switch (instruction.getOperation()) {
            case Op -> memory[result] = getValue(left);
            case Add -> memory[result] = getValue(left) + getValue(right);
            case Sub -> memory[result] = getValue(left) - getValue(right);
            case Mul -> memory[result] = getValue(left) * getValue(right);
            case Div -> memory[result] = getValue(left) / getValue(right);
            case Mod -> memory[result] = getValue(left) % getValue(right);
            default -> throw new IllegalArgumentException("Unknown operation: " + instruction.getOperation());
        }
    }

    public void evaluate() {
        for (Instruction instruction : instructions) {
            evaluateInstruction(instruction);
        }
    }
}
