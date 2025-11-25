package de.uniwue.jpp.compiler.lowering;

public class Operand {
    boolean isLocal;
    int value;

    public Operand(boolean isLocal, int value) {
        this.isLocal = isLocal;
        this.value = value;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public int getValue() {
        return value;
    }
}
