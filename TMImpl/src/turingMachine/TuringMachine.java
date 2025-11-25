package turingMachine;

import finiteStateMachine.AbstractFiniteStateMachine;
import finiteStateMachine.state.Transition;
import turingMachine.tape.Direction;
import turingMachine.tape.MultiTape;
import turingMachine.tape.MultiTapeReadWriteData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TuringMachine<T> extends AbstractFiniteStateMachine<MultiTapeReadWriteData<T>, TuringTransitionOutput<T>> {

    MultiTape<T> multiTape;

    private int tapeCount;

    public TuringMachine(int tapeCount) {
        multiTape = new MultiTape<>(tapeCount);
        this.tapeCount = tapeCount;
    }

    public void run() {
        try {
            while (!isInAcceptedState()) {
                MultiTapeReadWriteData<T> input = getInput();
                Transition<TuringTransitionOutput<T>> transition = getTransition(input);
                transit();
            }
        } catch (RuntimeException ignored) {

        }


    }

    public void transit() {
        if (isInAcceptedState())
            throw new IllegalStateException("");
        super.transit(getInput());
    }

    public int getTapeCount() {
        return multiTape.getTapeCount();
    }

    public MultiTape<T> getTapes() {
        return multiTape;
    }

    public MultiTapeReadWriteData<T> getInput() {
        List<T> read = new ArrayList<>();
        for (int i = 0; i < tapeCount; i++)
            read.add(multiTape.getTapes().get(i).read());

        MultiTapeReadWriteData<T> input = new MultiTapeReadWriteData<>(read.size());
        for (int i = 0; i < tapeCount; i++)
            input.set(i, read.get(i));
        return input;
    }


    public void processOutput(TuringTransitionOutput<T> output) {
        MultiTapeReadWriteData<T> toWrite = output.getToWrite();
        Direction[] directions = output.getDirections();
        multiTape.write(toWrite);
        multiTape.move(directions);
//        System.out.println("directions: " + Arrays.toString(directions));
    }

    public static void main(String[] args) {

    }
}
