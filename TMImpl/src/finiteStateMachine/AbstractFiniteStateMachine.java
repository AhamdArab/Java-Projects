package finiteStateMachine;


import finiteStateMachine.state.State;
import finiteStateMachine.state.Transition;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFiniteStateMachine<I, O> {


    Map<String, State<I, O>> stateMap = new HashMap<>();
    State<I, O> currentState;

    public void addState(String state) {
        if (stateMap.containsKey(state))
            throw new IllegalStateException();
        stateMap.put(state, new State<>(this, state));
    }

    public State<I, O> getState(String state) {
        if (!stateMap.containsKey(state))
            throw new IllegalStateException("");
        return stateMap.get(state);
    }

    public void addTransition(String startState, I input, String targetState, O output) {
        if (!(stateMap.containsKey(startState) && stateMap.containsKey(targetState)))
            throw new IllegalStateException("");
        stateMap.get(startState).addTransition(input, targetState, output);
    }

    public void setCurrentState(String state) {
        if (!stateMap.containsKey(state))
            throw new IllegalStateException("");
        currentState = stateMap.get(state);
    }

    public State<I, O> getCurrentState() {
        return currentState;
    }

    public boolean isInAcceptedState() {
        if (currentState == null)
            return false;
        return currentState.isAccepted();
    }

    public void transit(I input) {

        if (input == null)
            throw new IllegalArgumentException("");
        if (currentState == null)
            throw new IllegalStateException("");
        O output = stateMap.get(currentState.toString()).transit(input);
        processOutput(output);

    }

    public Transition<O> getTransition(I input) {
        if (input == null)
            return null;
        if (currentState == null)
            throw new IllegalStateException("");
        return stateMap.get(currentState.toString()).getTransition(input);
    }

    protected abstract void processOutput(O output);
}
