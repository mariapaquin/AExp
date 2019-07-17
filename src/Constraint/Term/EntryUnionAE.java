package Constraint.Term;

import Solving.AvailableExpressionSet;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;


public class EntryUnionAE extends ConstraintTerm {

    private EntryLabel entry;
    private ASTNode node;
    private List<ExpressionLiteral> expressionList;

    public EntryUnionAE(EntryLabel entryTerm) {
        entry = entryTerm;
        node = entry.getNode();
        expressionList = new ArrayList<>();
    }

    public void addExpression(ExpressionLiteral e) {
        expressionList.add(e);
    }

    public void updateDefinitionSet(AvailableExpressionSet ds2) {

    }


    public String toString() {
        String nodeExpr = node.toString();

        if (node instanceof VariableDeclarationStatement) {
            nodeExpr = ((VariableDeclarationStatement) node).fragments().get(0).toString();
        }

        if(node instanceof ExpressionStatement){
            nodeExpr = ((ExpressionStatement) node).getExpression().toString();
        }

        if (node instanceof IfStatement) {
            nodeExpr = ((IfStatement) node).getExpression().toString();
        }

        if (node instanceof WhileStatement) {
            nodeExpr = ((WhileStatement) node).getExpression().toString();
        }

        if (node instanceof ForStatement) {
            nodeExpr = ((ForStatement) node).getExpression() .toString();
        }

        String ret = "entry[" + nodeExpr + "] ";
        if (!expressionList.isEmpty()) {
            for (int i = 0; i < expressionList.size(); i++) {
                ret += " U " + expressionList.get(i);
            }
        }
        return ret;
    }

}

