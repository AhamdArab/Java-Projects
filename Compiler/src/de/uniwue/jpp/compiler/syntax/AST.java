package de.uniwue.jpp.compiler.syntax;

import java.util.ArrayList;
import java.util.List;

public interface AST {
    public static AST create() {
        return new AST() {
            final List<Expression> expressions = new ArrayList<>();

            @Override
            public int addExpression(Expression expression) {
                expressions.add(expression);
                return expressions.size() - 1;
            }

            @Override
            public Expression getExpression(int expression) {
                return expressions.get(expression);
            }
        };
    }

    public int addExpression(Expression expression);
    public Expression getExpression(int expression);
}
