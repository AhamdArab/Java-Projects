package de.uniwue.jpp.compiler.lowering;

public class LocalInfo {
    int name;
    int offset;

    public LocalInfo(int name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    public int getName() {
        return name;
    }

    public int getOffset() {
        return offset;
    }
}
