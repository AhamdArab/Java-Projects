package de.uniwue.jpp.compiler.lowering;

public enum Operation {
    Op(1),
    Add(2),
    Sub(2),
    Mul(2),
    Div(2),
    Mod(2);

    private final int arity;

    Operation(int arity) {
        this.arity = arity;
    }

    public int getArity() {
        return arity;
    }
}
