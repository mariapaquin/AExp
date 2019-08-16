package visitor;

import Constraint.Term.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTVisitor;
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
        ExpressionLiteral expressionLiteral = new ExpressionLiteral(node);

        List<String> varsUsed = getVarsUsed(node);
        expressionLiteral.setVarsUsed(varsUsed);

        availableExpressions.add(expressionLiteral);
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
