package de.uniwue.jpp.compiler.lowering;

public class Instruction {
    Operation operation;
    int result;
    Operand left;
    Operand right;

    /**
     * Erstellt eine neue Instruction mit zwei Operanden.
     *
     * @param operation Die Art der Anweisung.
     * @param result    Der Speicherort, an dem das Ergebnis gespeichert wird.
     * @param left      Der linke Operand.
     * @param right     Der rechte Operand.
     */
    public Instruction(Operation operation, int result, Operand left, Operand right) {
        this.operation = operation;
        this.result = result;
        this.left = left;
        this.right = right;
    }

    /**
     * Erstellt eine neue Instruction mit einem Operanden.
     * Der Operand wird als linker Operand gespeichert.
     *
     * @param operation Die Art der Anweisung.
     * @param result    Der Speicherort, an dem das Ergebnis gespeichert wird.
     * @param value     Der einzige Operand.
     */
    public Instruction(Operation operation, int result, Operand value) {
        this(operation, result, value, null);
    }

    /**
     * Gibt die Operation der Instruction zurück.
     *
     * @return Die Operation.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Gibt den Speicherort zurück, an dem das Ergebnis gespeichert wird.
     *
     * @return Der Ergebnisort.
     */
    public int getResult() {
        return result;
    }

    /**
     * Gibt den linken Operanden zurück.
     *
     * @return Der linke Operand.
     */
    public Operand getLeft() {
        return left;
    }

    /**
     * Gibt den rechten Operanden zurück.
     *
     * @return Der rechte Operand.
     */
    public Operand getRight() {
        return right;
    }

    /**
     * Gibt den einzigen Operanden zurück.
     * Diese Methode ist nützlich, wenn die Instruction nur einen Operanden hat.
     *
     * @return Der einzige Operand.
     */
    public Operand getValue() {
        return left;
    }
}
