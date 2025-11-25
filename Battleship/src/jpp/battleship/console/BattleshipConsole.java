package jpp.battleship.console;

import jpp.battleship.board.*;
import jpp.battleship.game.Battleship;
import jpp.battleship.game.Difficulty;
import jpp.battleship.game.Player;
import jpp.battleship.logic.BoardState;
import jpp.battleship.logic.strategy.TargetStrategy;
import jpp.battleship.model.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jpp.battleship.model.Alignment;

public class BattleshipConsole {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Scanner scanner;
    private final PrintStream printer;

    private static final Alignment HORIZONTAL = Alignment.valueOf("HORIZONTAL");
    private static final Alignment VERTICAL = Alignment.valueOf("VERTICAL");

    public BattleshipConsole(InputStream in, OutputStream out) {
        this.inputStream = in;
        this.outputStream = out;
        this.scanner = new Scanner(Objects.requireNonNull(in));
        this.printer = new PrintStream(Objects.requireNonNull(out));
    }

    private static String visualizeHiddenBoard(Board board){
        BoardState state = BoardState.create(board);
        return  "  "+IntStream.range(0,board.getWidth()).mapToObj(i -> String.valueOf(i+1)).collect(Collectors.joining(" "))+"\n"+IntStream.range(0, board.getHeight())
                .mapToObj(y -> String.valueOf((char)(y+65))+" "+ IntStream.range(0, board.getWidth())
                        .mapToObj(x -> {
                            Coordinate c = Coordinate.of(x, y);
                            if(state.getDamaged().contains(c))
                                return "+";
                            if(state.getHits().contains(c))
                                return "X";
                            if(state.getShots().contains(c))
                                return "~";
                            return "#";
                        })
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
    }

    private static String visualizeBoard(Board board){
        return "  "+IntStream.range(0, board.getWidth()).mapToObj(i -> String.valueOf(i+1)).collect(Collectors.joining(" "))+"\n"+IntStream.range(0, board.getHeight())
                .mapToObj(y -> String.valueOf((char)(y+65))+" "+IntStream.range(0, board.getWidth())
                        .mapToObj(x -> {
                            Coordinate coordinate = Coordinate.of(x, y);
                            return board.getShip(coordinate).map(s -> s.getHits().contains(coordinate)?"X":s.getShipClass().toString()).orElse("~");
                        })
                        .collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));

    }

    private static String toBattleShipString(Coordinate coordinate) {
        String y = String.valueOf((char) (coordinate.getY() + 65));
        String x = String.valueOf(coordinate.getX() + 1);
        return y + x;
    }

    private static void tryPlaceShip(BoardBuilder builder, ShipClass shipClass, Supplier<Optional<Coordinate>> referenceSup, Supplier<Optional<Alignment>> alignmentSup, Consumer<String> notifier) {
        while (true) {
            try{
                Optional<Coordinate> coordinateReference = referenceSup.get();
                if(coordinateReference.isEmpty()){
                    notifier.accept("Not a coordinate! Try again.");
                    continue;
                }
                Optional<Alignment> alignment = alignmentSup.get();
                if(alignment.isEmpty()) {
                    notifier.accept("Not a valid alignment! Try again.");
                    continue;
                }
                builder.addShip(Ship.of(shipClass, coordinateReference.get(), alignment.get()));
                return;
            }catch (IllegalShipPlacementException e){
                notifier.accept("Error: " + e.getMessage());
            }
        }
    }

    private static Board createRandomBoard(int width, int height) {
        Random random = new Random();
        BoardBuilder builder = BoardFactory.empty(width,height);
        Arrays.stream(ShipClass.values()).forEach(shipClass -> tryPlaceShip(builder,shipClass,
                () -> Optional.of(Coordinate.of(random.nextInt(width),random.nextInt(height) )),
                () -> Optional.of(random.nextBoolean()? HORIZONTAL: VERTICAL),
                (str) -> {}
                ));
        return builder.build();
    }

    private static Board createRandomBoard() {
        return createRandomBoard(10, 10);
    }

    private static Optional<Alignment> parseAlignment(String s){
        return switch (s) {
            case "-" -> Optional.of(HORIZONTAL);
            case "|" -> Optional.of(VERTICAL);
            default -> Optional.empty();
        };
    }

    private void placeShipDialog(ShipClass shipClass, BoardBuilder builder) {
        tryPlaceShip(builder,shipClass,() -> {
            printer.print(visualizeBoard(builder.build()) + "\nChoose reference coordinate for your " + shipClass.name() + ":");
            return Coordinate.fromBattleshipString(scanner.nextLine());
        }, () -> {
            printer.print("Choose alignment (\"-\" or \"|\"): ");
            return parseAlignment(scanner.nextLine());
        }, printer::println);
    }

    private void run(Player p1, Player p2, String playerName){
        Battleship game = new Battleship(p1, p2);
        while(game.getWinner().isEmpty()){
            Player currentPlayer = game.getCurrentPlayer();
            if(currentPlayer.name().equals(playerName)) {
                printer.println("Own field: ");
                printer.println(visualizeBoard(currentPlayer.board()));
                printer.println("\nYour move:");
                Player opponent = game.getP1().equals(currentPlayer)?game.getP2():game.getP1();
                printer.println(visualizeHiddenBoard(opponent.board()));
            }
            Coordinate played = game.move();
            printer.println(currentPlayer.name() + " played " + toBattleShipString(played));
            printer.println();
        }
        printer.println(game.getP1().name()+":");
        printer.println(visualizeBoard(game.getP1().board()));
        printer.println();
        printer.println(game.getP2().name()+":");
        printer.println(visualizeBoard(game.getP2().board()));
        printer.println();
        printer.println("Winner: " + game.getWinner().get().name());
    }

    public void computerGame() {
        Player dummy = new Player("Dummy", createRandomBoard(), TargetStrategy.RandomAndHuntStrategy());
        Player pro = new Player("AI", createRandomBoard(), TargetStrategy.ProbabilityAndHuntStrategy());
        run(dummy, pro, null);
    }

    public void run(Difficulty difficulty) {
        printer.println("Welcome to Battleship!");
        printer.print("Enter your name: ");
        String name = scanner.nextLine();
        BoardBuilder builder = BoardFactory.empty(10,10);
        for(ShipClass shipClass : ShipClass.values()) {
            placeShipDialog(shipClass,builder);
        }
        Player humanPlayer = new Player(name, builder.build(), TargetStrategy.UserChoiceStrategy(inputStream, outputStream));
        Player computerPlayer = new Player("AI", createRandomBoard(), difficulty.strategy());
        run(humanPlayer, computerPlayer, name);
    }

    public static void main(String[] args) {
        BattleshipConsole console = new BattleshipConsole(System.in, System.out);
        Difficulty difficulty = Difficulty.valueOf("MEDIUM");
        console.run(difficulty);
    }
}
