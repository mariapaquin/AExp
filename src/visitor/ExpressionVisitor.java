package visitor;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.SimpleName;

import java.util.ArrayList;
import java.util.List;

public class ExpressionVisitor extends ASTVisitor {
    private List<ExpressionLiteral> availableExpressions;
    private int count;

    public ExpressionVisitor() {
        availableExpressions = new ArrayList<>();
        count = 0;
    }


    public List<ExpressionLiteral> getAvailableExpressions() {
        return availableExpressions;
    }

    @Override
    public boolean visit(InfixExpression node) {
        Expression lhs = node.getLeftOperand();
        Expression rhs = node.getRightOperand();
        InfixExpression.Operator op = node.getOperator();

        if (!(lhs instanceof SimpleName) || !(rhs instanceof SimpleName)) {
            return true;
        }

        if((op != InfixExpression.Operator.TIMES) &&
                (op != InfixExpression.Operator.DIVIDE) &&
                (op != InfixExpression.Operator.REMAINDER)){
            return true;
        }

        ExpressionLiteral expressionLiteral = new ExpressionLiteral(node, count++);

        List<String> varsUsed = getVarsUsed(node);
        expressionLiteral.setVarsUsed(varsUsed);

        boolean existingExpr = false;
        for (ExpressionLiteral e : availableExpressions) {
            if (e.getNode().toString().equals(node.toString())) {
                existingExpr = true;
            }
        }
        if(!existingExpr) {
            availableExpressions.add(expressionLiteral);
        }
        return true;
    }

    private List<String> getVarsUsed(InfixExpression node) {
        List<String> vars = new ArrayList<>();
        ASTVisitor visitor= new ASTVisitor() {
            @Override
            public boolean visit(SimpleName name) {
                vars.add(name.getIdentifier());
                return false;
            }
        };
        node.accept(visitor);
        return vars;
    }
}
