package turingMachine.io;

import finiteStateMachine.state.State;
import finiteStateMachine.state.Transition;
import turingMachine.TuringMachine;
import turingMachine.TuringTransitionOutput;
import turingMachine.tape.*;

import java.io.*;
import java.util.List;

public class TuringMachineReader {


    public static TuringMachine<Character> read(InputStream input) {
        if (input == null)
            throw new IllegalArgumentException("");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        int cnt = 0;
        int tapeCount = 0;
        int index = 0;
        TuringMachine<Character> turingMachine = null;
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.isEmpty())
                    continue;
                if (line.trim().charAt(0) == '#')
                    continue;
                if (cnt == 0) {
                    tapeCount = Integer.parseInt(line);
                    cnt++;
                    turingMachine = new TuringMachine<>(tapeCount);
                    continue;
                }
                if (cnt == 1) {
                    int pos;
                    try {
                        pos = Integer.parseInt(line.split(":")[0]);
                    } catch (NumberFormatException exception) {
                        throw new IllegalArgumentException("");
                    }
                    String valuesTape = line.split(":")[1];

                    MultiTape<Character> multiTape = turingMachine.getTapes();

                    for (int i = 0; i < valuesTape.length(); i++) {
                        multiTape.getTapes().get(index).move(Direction.RIGHT);
                        multiTape.getTapes().get(index).write(valuesTape.charAt(i));
                    }
                    for (int i = 0; i < valuesTape.length(); i++)
                        multiTape.getTapes().get(index).move(Direction.LEFT);

                    for (int i = 0; i < pos; i++) {
                        multiTape.getTapes().get(index).move(Direction.RIGHT);
                    }
                    index++;
                    if (index == tapeCount)
                        cnt++;
                    continue;
                }
                if (cnt == 2) {
                    String[] states = line.split(",");
                    for (String state : states)
                        turingMachine.addState(state);
                    cnt++;
                    continue;
                }
                if (cnt == 3) {
                    try {
                        turingMachine.setCurrentState(line);
                    } catch (IllegalStateException exception) {
                        throw new IllegalArgumentException("");
                    }
                    cnt++;
                    continue;
                }
                if (cnt == 4) {
                    String[] states = line.split(",");
                    for (String state : states) {
                        try {
                            turingMachine.getState(state).setAccepted(true);
                        } catch (IllegalStateException exception) {
                            throw new IllegalArgumentException("");
                        }
                    }

                    cnt++;
                    continue;
                }
                try {

                    String start = line.split("->")[0].trim();
                    String startState = start.split(",")[0].trim();
                    String inputChar = start.split(",")[1].trim();
                    if (inputChar.length()!=tapeCount){
                        throw new IllegalArgumentException("");
                    }
                    MultiTapeReadWriteData<Character> inputMulti = new MultiTapeReadWriteData<>(tapeCount);
                    for (int i = 0; i < tapeCount; i++) {
                        if (inputChar.charAt(i) == '_')
                            inputMulti.set(i, null);
                        else
                            inputMulti.set(i, inputChar.charAt(i));
                    }
                    String target = line.split("->")[1].trim();
                    String targetState = target.split(",")[0].trim();
                    String outputChar = target.split(",")[1].trim();
                    if (outputChar.length()!=tapeCount){
                        throw new IllegalArgumentException("");
                    }
                    MultiTapeReadWriteData<Character> outputMulti = new MultiTapeReadWriteData<>(tapeCount);
                    for (int i = 0; i < tapeCount; i++) {
                        if (outputChar.charAt(i) == '_')
                            outputMulti.set(i, null);
                        else
                            outputMulti.set(i, outputChar.charAt(i));
                    }
                    String dir = target.split(",")[2].trim();
                    if (dir.length()!=tapeCount){
                        throw new IllegalArgumentException("");
                    }
                    Direction[] directions = new Direction[tapeCount];
                    for (int i = 0; i < tapeCount; i++) {
                        Direction direction = switch (dir.charAt(i)) {
                            case 'R' -> Direction.RIGHT;
                            case 'L' -> Direction.LEFT;
                            case 'N' -> Direction.NON;
                            default -> {
                                throw new IllegalArgumentException("");
                            }
                        };
                        directions[i] = direction;
                    }

                    TuringTransitionOutput<Character> turingTransitionOutput = new TuringTransitionOutput<>(outputMulti, directions);
                    turingMachine.addTransition(startState, inputMulti, targetState, turingTransitionOutput);
                } catch (Exception exception) {
                    throw new IllegalArgumentException("");
                }

            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new IllegalArgumentException("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(turingMachine);
        return turingMachine;
    }

    public static TuringMachine<Character> read(Reader input) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int data;
            while ((data = input.read()) != -1) {
                byteArrayOutputStream.write(data);
            }
            return read(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {


    }
}
