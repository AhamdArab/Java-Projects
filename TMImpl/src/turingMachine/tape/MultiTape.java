package turingMachine.tape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiTape<T> {

    private List<Tape<T>> tapes = new ArrayList<>();
    private int tapeCount;

    public MultiTape(int tapeCount) {
        this.tapeCount = tapeCount;
        for (int i = 0; i < tapeCount; i++)
            tapes.add(new Tape<T>());
    }

    public MultiTapeReadWriteData<T> read() {
        MultiTapeReadWriteData<T> multiTapeReadWriteData = new MultiTapeReadWriteData<>(tapeCount);
        for (int i = 0; i < tapeCount; i++) {
            multiTapeReadWriteData.set(i, tapes.get(i).read());
        }
        return multiTapeReadWriteData;
    }

    public void write(MultiTapeReadWriteData<T> values) {
        if (values.getLength() != tapeCount)
            throw new IllegalArgumentException("");
        for (int i = 0; i < tapeCount; i++) {
            tapes.get(i).write(values.get(i));
        }
    }

    public void move(Direction[] directions) {
        if (directions.length != tapeCount)
            throw new IllegalArgumentException("");
        for (int i = 0; i < tapeCount; i++) {
            tapes.get(i).move(directions[i]);
        }
    }

    public List<Tape<T>> getTapes() {
        return tapes;
    }



    public int getTapeCount() {
        return tapeCount;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < tapeCount; i++) {
            result += tapes.get(i).toString() + "\n";
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiTape<?> multiTape = (MultiTape<?>) o;
        return tapeCount == multiTape.tapeCount && Objects.equals(tapes, multiTape.tapes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tapes, tapeCount);
    }

}
