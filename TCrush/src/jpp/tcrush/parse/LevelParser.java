package jpp.tcrush.parse;

import jpp.tcrush.gamelogic.Level;
import jpp.tcrush.gamelogic.Shape;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

public class LevelParser {

    public static Level parseStreamToLevel(InputStream inputStream)  {
        try {
            String text = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            if(!text.contains("TCrush-LevelDefinition")){
                throw new InputMismatchException();
            }
            if(!text.contains("Shapes")){
                throw new InputMismatchException();
            }

            text = text.replace("TCrush-LevelDefinition:\n", "");

            String[] jeweilsNachLeerzeichen = text.split("\n\nShapes:\n");
            String[] splitenBeiShapes = jeweilsNachLeerzeichen[1].split("\n");
            List<Shape> shapeList = new ArrayList<>();
            for (String splitenBeiShape : splitenBeiShapes) {
                shapeList.add(ParserUtils.parseStringToShape(splitenBeiShape));
            }

            return new Level(ParserUtils.parseStringToFieldMap(jeweilsNachLeerzeichen[0]), shapeList);
        }catch (Exception exception){
            throw new InputMismatchException();
        }
    }


    public static void writeLevelToStream(Level level, OutputStream outputStream) throws IOException {
        outputStream.write(level.toString().getBytes(StandardCharsets.UTF_8));
    }

}
