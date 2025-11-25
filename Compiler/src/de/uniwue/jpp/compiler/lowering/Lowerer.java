package de.uniwue.jpp.compiler.lowering;

import de.uniwue.jpp.compiler.error.CannotFindNameError;
import de.uniwue.jpp.compiler.syntax.AST;
import de.uniwue.jpp.compiler.syntax.Expression;

import java.util.ArrayList;
import java.util.List;

import de.uniwue.jpp.compiler.error.Error;

public class Lowerer {
    AST ast;
    StackSize stackSize;
    List<LocalInfo> nameStack;
    List<Instruction> instructions;
    List<Error> errors;

    public Lowerer(AST ast) {
        this(ast, StackSize.create());
    }

    public Lowerer(AST ast, StackSize stackSize) {
        this.ast = ast;
        this.stackSize = stackSize;
        this.nameStack = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public StackSize getStackSize() {
        return stackSize;
    }

    public List<LocalInfo> getNameStack() {
        return nameStack;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void pushNamedLocal(int name, int offset) {
        nameStack.add(new LocalInfo(name, offset));
    }

    public void popNamedLocal() {
        if (!nameStack.isEmpty()) {
            nameStack.remove(nameStack.size() - 1);
        }
    }

    public int makeLocal() {
        stackSize.allocate(1);
        return stackSize.getCurrentStackSize() - 1;
    }

    public Operand lowerNumber(Expression expression) {
        int value = expression.getValue();
        return new Operand(false, value);
    }

    public Operand lowerIdentifier(Expression expression) {
        int name = expression.getValue();
        for (int i = nameStack.size() - 1; i >= 0; i--) {
            LocalInfo localInfo = nameStack.get(i);
            if (localInfo.getName() == name) {
                return new Operand(true, localInfo.getOffset());
            }
        }

        errors.add(new CannotFindNameError(name, expression.getLocation(ast)));
        return new Operand(true, 0);
    }

    public Operand lowerLet(Expression expression) {
        int localOffset = makeLocal();
        int savedStackSize = stackSize.getCurrentStackSize();

        Operand assignedValue = lowerExpression(expression.getRight());


        instructions.add(new Instruction(Operation.Op, localOffset, assignedValue));

        stackSize.truncate(savedStackSize);


        pushNamedLocal(expression.getLeft(), localOffset);


        Operand innerValue = lowerExpression(expression.getRight() + 1);


        popNamedLocal();

        return innerValue;
    }

    public Operand lowerBinary(Expression expression, Operation operation) {
        int localOffset = makeLocal();
        int savedStackSize = stackSize.getCurrentStackSize();


        Operand leftOperand = lowerExpression(expression.getLeft());
        Operand rightOperand = lowerExpression(expression.getRight());


        instructions.add(new Instruction(operation, localOffset, leftOperand, rightOperand));


        stackSize.truncate(savedStackSize);

        return new Operand(true, localOffset);
    }

    public Operand lowerExpression(int expressionIndex) {
        Expression expression = ast.getExpression(expressionIndex);
        return switch (expression.getKind()) {
            case Number -> lowerNumber(expression);
            case Identifier -> lowerIdentifier(expression);
            case Let -> lowerLet(expression);
            case Add -> lowerBinary(expression, Operation.Add);
            case Sub -> lowerBinary(expression, Operation.Sub);
            case Mul -> lowerBinary(expression, Operation.Mul);
            case Div -> lowerBinary(expression, Operation.Div);
            case Mod -> lowerBinary(expression, Operation.Mod);
        };
    }
}
