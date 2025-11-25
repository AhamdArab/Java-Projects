package jpp.battleship.logic.strategy;

import jpp.battleship.logic.BoardState;
import jpp.battleship.model.Coordinate;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

public class UserChoiceStrategy implements TargetStrategy{
    Scanner scanner;
    PrintStream output;

    public UserChoiceStrategy(InputStream inputStream, OutputStream outputStream) {
        this.scanner = new Scanner(inputStream);
        this.output = new PrintStream(outputStream);
    }

    @Override
    public Coordinate next(BoardState state) {
        while (true) {
            output.print("Enter a coordinate: ");
            String input = scanner.nextLine().trim();
            Optional<Coordinate> coordinate = Coordinate.fromBattleshipString(input);
            if (coordinate.isPresent() && state.availableTargets().contains(coordinate.get())) {
                return coordinate.get();
            }
        }
    }

    @Override
    public String name() {
        return "UserChoice";

    }
}
