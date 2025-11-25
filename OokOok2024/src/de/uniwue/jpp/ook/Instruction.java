package de.uniwue.jpp.ook;

/**
 * Diese Aufzählung bildet die Anweisungen bestehend aus jeweils zwei Symbolen ab.
 */
public enum Instruction {
    Inc(Symbol.Point, Symbol.Point),
    Dec(Symbol.Exclamation, Symbol.Exclamation),
    PtrInc(Symbol.Point, Symbol.Question),
    PtrDec(Symbol.Question, Symbol.Point),
    Loop(Symbol.Exclamation, Symbol.Question),
    End(Symbol.Question, Symbol.Exclamation),
    Write(Symbol.Exclamation, Symbol.Point),
    Read(Symbol.Point, Symbol.Exclamation);

    private final Symbol first, last;

    Instruction(Symbol first, Symbol last) {
        this.first = first;
        this.last = last;
    }

    public Symbol getFirstSymbol() {
        return first;
    }

    public Symbol getLastSymbol() {
        return last;
    }
}
