package visitor;

import Expression.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.SimpleName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpressionVisitor extends ASTVisitor {
    private List<ExpressionLiteral> nonlinearVarExpr;
    private HashMap<String, Integer> exprMap;


    private int varCount;


    public ExpressionVisitor() {
        nonlinearVarExpr = new ArrayList<>();
        exprMap = new HashMap<>();
        varCount = 0;
    }

    public List<ExpressionLiteral> getNonlinearVarExpr() {
        return nonlinearVarExpr;
    }

    public int getVarCount() {
        return varCount;
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

        ExpressionLiteral expressionLiteral = new ExpressionLiteral(node);

        List<String> varsUsed = getVarsUsed(node);
        expressionLiteral.setVarsUsed(varsUsed);

        boolean existingExpr = false;
        for (ExpressionLiteral e : nonlinearVarExpr) {
            if (e.getNode().toString().equals(node.toString())) {
                existingExpr = true;
            }
        }
        if(!existingExpr) {
            nonlinearVarExpr.add(expressionLiteral);
            exprMap.put(node.toString(), varCount++);
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

    public HashMap<String, Integer> getExprMap() {
        return exprMap;
    }
}
