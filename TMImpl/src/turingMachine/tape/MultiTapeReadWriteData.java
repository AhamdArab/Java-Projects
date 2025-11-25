package turingMachine.tape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiTapeReadWriteData<T> {

    private int length;
    List<T> memory = new ArrayList<>();

    public MultiTapeReadWriteData(int length) {
        this.length = length;
        for (int i = 0; i < length; i++) {
            memory.add(null);
        }

    }

    public T get(int i) {
        if (i >= length)
            throw new IndexOutOfBoundsException("");
        return memory.get(i);
    }

    public void set(int i, T value) {
        if (i >= length)
            throw new IndexOutOfBoundsException("");
        memory.set(i, value);
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        String result = "";
        for (T item : memory) {
            if (item == null)
                result += "_";
            else
                result += item.toString();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiTapeReadWriteData<?> that = (MultiTapeReadWriteData<?>) o;
        return Objects.equals(memory, that.memory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memory);
    }
}
