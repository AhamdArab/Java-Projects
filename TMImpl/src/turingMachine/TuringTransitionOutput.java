package turingMachine;

import turingMachine.tape.Direction;
import turingMachine.tape.MultiTapeReadWriteData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TuringTransitionOutput<T> {
    private MultiTapeReadWriteData<T> toWrite;
    private Direction[] directions;

    public TuringTransitionOutput(MultiTapeReadWriteData<T> toWrite, Direction... directions) {
        if (toWrite.getLength() != directions.length)
            throw new IllegalArgumentException();

        this.toWrite = toWrite;
        this.directions = directions;
    }

    public MultiTapeReadWriteData<T> getToWrite() {
        return toWrite;
    }

    public Direction[] getDirections() {
        return directions;
    }
}
