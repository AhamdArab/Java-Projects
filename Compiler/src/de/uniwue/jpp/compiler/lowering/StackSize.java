package de.uniwue.jpp.compiler.lowering;

public interface StackSize {
    public static StackSize create() {
        return new StackSize() {
            int currentStackSize;
            int maxStackSize;

            @Override
            public int getCurrentStackSize() {
                return currentStackSize;
            }

            @Override
            public int getMaxStackSize() {
                return maxStackSize;
            }

            @Override
            public int allocate(int amount) {
                int previousSize = currentStackSize;
                currentStackSize += amount;

                if (currentStackSize > maxStackSize) {
                    maxStackSize = currentStackSize;
                }

                return previousSize;
            }

            @Override
            public void truncate(int size) {
                if (size > currentStackSize) {
                    throw new IllegalArgumentException("Size cannot be greater than the current stack size.");
                }
                currentStackSize = size;
            }
        };
    }

    public int getCurrentStackSize();
    public int getMaxStackSize();

    public int allocate(int amount);
    public void truncate(int size);
}
