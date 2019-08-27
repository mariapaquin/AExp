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

    public ExpressionVisitor() {
        availableExpressions = new ArrayList<>();
    }


    public List<ExpressionLiteral> getAvailableExpressions() {
        return availableExpressions;
    }

    @Override
    public boolean visit(InfixExpression node) {
        Expression lhs = node.getLeftOperand();
        Expression rhs = node.getRightOperand();

        if (!(lhs instanceof SimpleName) || !(rhs instanceof SimpleName)) {
            return false;
        }
        ExpressionLiteral expressionLiteral = new ExpressionLiteral(node);

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
        return false;
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
