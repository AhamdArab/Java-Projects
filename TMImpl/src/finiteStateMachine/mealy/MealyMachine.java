package finiteStateMachine.mealy;

import finiteStateMachine.AbstractFiniteStateMachine;

public class MealyMachine<I, O> extends AbstractFiniteStateMachine<I, O> {
    @Override
    protected void processOutput(O output) {
        System.out.print(output);
    }

    public static void main(String[] args) {
        
    }
}
