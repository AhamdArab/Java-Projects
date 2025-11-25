package turingMachine.tape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Tape<T> {

    int position = 0;
    List<T> memory = new ArrayList<>();
    List<TapeChangeListener<T>> listeners = new ArrayList<>();

    public Tape() {
        memory.add(null);
    }

    public void move(Direction direction) {

        if (direction.equals(Direction.RIGHT)) {
            position++;

            if (position >= memory.size()) {
                memory.add(null);
                for (TapeChangeListener<T> listener : listeners) listener.onExpand(direction);
            }

        } else if (direction.equals(Direction.LEFT)) {
            position--;

            if (position < 0) {
                memory.add(0, null);
                position = 0;
                for (TapeChangeListener<T> listener : listeners) listener.onExpand(direction);
            }
        }

        for (TapeChangeListener<T> listener : listeners) {
            listener.onMove(direction);
        }

    }

    public T read() {
        return memory.get(position);
    }

    public List<T> getContents() {
        return memory;
    }

    public int getPosition() {
        return position;
    }

    public void write(T content) {

        memory.set(position, content);
        for (TapeChangeListener<T> listener : listeners) {
            listener.onWrite(content);
        }
    }

    public void addListener(TapeChangeListener<T> listener) {
        listeners.add(listener);
    }

    public String getCurrent() {
        if (memory.get(position) == null)
            return "_";
        return memory.get(position).toString();
    }

    public String getLeft() { ///
        List<T> leftSublist = memory.subList(0, position);
        int firstNonNullIndex = IntStream.range(0, leftSublist.size())
                .filter(i -> leftSublist.get(i) != null)
                .findFirst()
                .orElse(position);

        if(firstNonNullIndex == position) {
            return "";
        }

        return leftSublist.subList(firstNonNullIndex, position)
                .stream()
                .map(e -> e == null ? "_" : e.toString())
                .collect(Collectors.joining());
        /*StringBuilder left = new StringBuilder();
        for (int i = 0; i < position; i++) {
            T content = memory.get(i);
            if (content != null || left.length() > 0) {
                left.append(content == null ? "_" : content.toString());
            }
        }
        return left.toString();*/
    }

    public String getRight() { ///
        List<T> rightSublist = memory.subList(position + 1, memory.size());
        int lastNonNullIndex = IntStream.range(0, rightSublist.size())
                .filter(i -> rightSublist.get(rightSublist.size() - 1 - i) != null)
                .findFirst()
                .orElse(-1);

        if (lastNonNullIndex == -1) {
            return "";
        }

        return rightSublist.subList(0, rightSublist.size() - lastNonNullIndex)
                .stream()
                .map(e -> e == null ? "_" : e.toString())
                .collect(Collectors.joining());
        /*StringBuilder right = new StringBuilder();
        for (int i = memory.size() - 1; i > position; i--) {
            T content = memory.get(i);
            if (content != null || right.length() > 0) {
                right.insert(0, content == null ? "_" : content.toString());
            }
        }
        return right.toString();*/
    }

    @Override
    public String toString() { ///
        String left = getLeft();
        String right = getRight();
        String current = getCurrent() == null ? "_" : getCurrent();

        String tapeStr = String.join("", left, current, right);

        return tapeStr + "\n" + " ".repeat(left.length()) + "^";
    }

}
