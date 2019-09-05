package Constraint.Term;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class NodeLabel {
    public List<ExpressionLiteral> exprList;
    protected ASTNode node;

    public NodeLabel(ASTNode node, List<ExpressionLiteral> exprList) {
        this.node = node;
        this.exprList = exprList;
    }

    public List<ExpressionLiteral> getExprList() {
        return exprList;
    }


    public List<String> getAvailableExpressionsAsString() {
        List<String> ret = new ArrayList<>();
        for (ExpressionLiteral e : exprList) {
            ret.add(e.getNode().toString());
        }
        return ret;
    }

    public void setExprList(List<ExpressionLiteral> expressions) {
        exprList = expressions;
    }

    public ASTNode getNode(){
        return node;
    }

    public void reassignExpr(ExpressionLiteral expr, String newVarName){
        for (ExpressionLiteral e : exprList) {
            if (e.equals(expr)) {
                e.setSymbVarName(newVarName);
            }
        }
    }

    public interface TermProcessor {
        void processTerm(NodeLabel term);
    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }

}
