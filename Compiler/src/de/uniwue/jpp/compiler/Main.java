package de.uniwue.jpp.compiler;
import de.uniwue.jpp.compiler.error.Error;
import de.uniwue.jpp.compiler.eval.Evaluator;
import de.uniwue.jpp.compiler.lowering.Lowerer;
import de.uniwue.jpp.compiler.syntax.AST;
import de.uniwue.jpp.compiler.syntax.Lexer;
import de.uniwue.jpp.compiler.syntax.Parser;
import de.uniwue.jpp.compiler.syntax.Token;
import de.uniwue.jpp.compiler.util.NameCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        compileResource("/simple.ml");
    }

    public static void compileResource(String path) throws IOException {
        compileFile(Main.class.getResource(path).getPath());
    }

    public static void compileFile(String path) throws IOException {
        var code = Files.readString(Paths.get(path));
        compileCode(code);
    }

    public static void compileCode(String code) {
        var nameCache = NameCache.create();

        var tokens = Lexer.lex(code, nameCache);

        var parser = new Parser(tokens);
        var maybeRoot = parser.parseAll();
        var ast = parser.getAST();

        if (printErrors(parser.getErrors(), tokens, code, nameCache))
            return;

        var root = maybeRoot.get();

        var builder = new StringBuilder();
        printExpression(root, ast, nameCache, builder);
        System.out.println(builder);

        var typer = new Lowerer(ast);
        var result = typer.lowerExpression(root);
        if (printErrors(typer.getErrors(), tokens, code, nameCache))
            return;

        var instructions = typer.getInstructions();
        var stackSize = typer.getStackSize().getMaxStackSize();

        var evaluator = new Evaluator(instructions, stackSize);
        evaluator.evaluate();
        var value = evaluator.getValue(result);
        System.out.println("Value: " + value);
    }

    public static boolean printErrors(List<Error> errors, List<Token> tokens, String code, NameCache nameCache) {
        if (!errors.isEmpty()) {
            for (var error : errors) {
                var location = error.getLocation();
                var firstPosition = tokens.get(location.getFirst()).getPosition();
                var lastPosition = tokens.get(location.getLast()).getPosition();
                var linesStart = countOccurrences(code, 0, firstPosition, '\n', new Result(0, 0));
                var linesEnd = countOccurrences(code, firstPosition, lastPosition, '\n', linesStart);

                var startRow = linesStart.count + 1;
                var startColumn = firstPosition - linesStart.lastIndex;
                var endRow = linesEnd.count + 1;
                var endColumn = lastPosition - linesEnd.lastIndex + tokens.get(location.getLast()).getLength(nameCache, code);

                System.out.println("Error (" + startRow + ", " + startColumn + "; " + endRow + ", " + endColumn + ") : " + error.getMessage(nameCache));
            }
            return true;
        }

        return false;
    }

    public static void printExpression(int expressionIndex, AST ast, NameCache names, StringBuilder builder) {
        printExpression(expressionIndex, ast, names, builder, 0, 0, "    ");
    }

    public static void printExpression(int expressionIndex, AST ast, NameCache names, StringBuilder builder, int precedence, int indent, String indentString) {
        var expression = ast.getExpression(expressionIndex);
        switch (expression.getKind()) {
            case Number -> {
                builder.append(expression.getValue());
            }
            case Identifier -> {
                builder.append(names.getName(expression.getValue()));
            }
            case Let -> {
                if (precedence > 0) {
                    indent += 1;
                    builder.append("(\n");
                    builder.append(indentString.repeat(indent));
                }
                builder
                    .append("let ")
                    .append(names.getName(expression.getValue()))
                    .append(" = ");
                printExpression(expression.getRight(), ast, names, builder, 1, indent, indentString);
                builder.append(" in\n");
                builder.append(indentString.repeat(indent));
                printExpression(expression.getRight() + 1, ast, names, builder, 0, indent, indentString);

                if (precedence > 0) {
                    builder.append("\n)");
                }
            }
            case Add -> {
                if (precedence > 1) {
                    builder.append("(");
                }
                printExpression(expression.getLeft(), ast, names, builder, 1, indent, indentString);
                builder.append(" + ");
                printExpression(expression.getRight(), ast, names, builder, 2, indent, indentString);

                if (precedence > 1) {
                    builder.append(")");
                }
            }
            case Sub -> {
                if (precedence > 1) {
                    builder.append("(");
                }
                printExpression(expression.getLeft(), ast, names, builder, 1, indent, indentString);
                builder.append(" - ");
                printExpression(expression.getRight(), ast, names, builder, 2, indent, indentString);

                if (precedence > 1) {
                    builder.append(")");
                }
            }
            case Mul -> {
                if (precedence > 2) {
                    builder.append("(");
                }
                printExpression(expression.getLeft(), ast, names, builder, 2, indent, indentString);
                builder.append(" * ");
                printExpression(expression.getRight(), ast, names, builder, 3, indent, indentString);

                if (precedence > 2) {
                    builder.append(")");
                }
            }
            case Div -> {
                if (precedence > 2) {
                    builder.append("(");
                }
                printExpression(expression.getLeft(), ast, names, builder, 2, indent, indentString);
                builder.append(" / ");
                printExpression(expression.getRight(), ast, names, builder, 3, indent, indentString);

                if (precedence > 2) {
                    builder.append(")");
                }
            }
            case Mod -> {
                if (precedence > 2) {
                    builder.append("(");
                }
                printExpression(expression.getLeft(), ast, names, builder, 2, indent, indentString);
                builder.append(" % ");
                printExpression(expression.getRight(), ast, names, builder, 3, indent, indentString);

                if (precedence > 2) {
                    builder.append(")");
                }
            }
        }
    }

    private record Result(int count, int lastIndex) {}
    private static Result countOccurrences(String text, int start, int end, char character, Result result) {
        var count = result.count;
        var last = result.lastIndex;

        for (var i = start; i < end; i++) {
            if (text.charAt(i) == character) {
                count += 1;
                last = i;
            }
        }

        return new Result(count, last);
    }
}