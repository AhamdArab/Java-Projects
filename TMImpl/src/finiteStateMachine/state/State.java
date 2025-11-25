package finiteStateMachine.state;

import finiteStateMachine.AbstractFiniteStateMachine;

import java.util.HashMap;
import java.util.Map;

public class State<I, O> {
    private AbstractFiniteStateMachine<I, O> machine;
    private String name;
    private boolean accepted;

    private Map<I, Transition<O>> transitions = new HashMap<>();


    public State(AbstractFiniteStateMachine<I, O> machine, String name) {
        this.machine = machine;
        this.name = name;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void addTransition(I input, String targetState, O output) {
        transitions.put(input, new Transition<O>(targetState, output));
    }

    public Transition<O> getTransition(I input) {
//        System.out.println("++++++++++++++++++++++++++");
//        System.out.println(input);
//        System.out.println(transitions.containsKey(input));
//        System.out.println(transitions.get(input));
//        System.out.println(transitions);
//        System.out.println("++++++++++++++++++++++++++");

        if (!transitions.containsKey(input))
            return null;
        return transitions.get(input);
    }

    public O transit(I input) {
        if (getTransition(input) == null)
            throw new IllegalArgumentException("");
        Transition<O> transition = getTransition(input);
        machine.setCurrentState(transition.getNextState());
        return transition.getOutput();
    }

    public AbstractFiniteStateMachine<I, O> getMachine() {
        return machine;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
