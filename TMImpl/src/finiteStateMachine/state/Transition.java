package finiteStateMachine.state;

public class Transition<O> {
    private String nextState;
    private O output;

    public Transition(String nextState, O output) {
        this.nextState = nextState;
        this.output = output;
    }

    public String getNextState() {
        return nextState;
    }

    public O getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return getNextState() + "," + getOutput();
    }
}
