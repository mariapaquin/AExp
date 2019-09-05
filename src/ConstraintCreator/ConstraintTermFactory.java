package ConstraintCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Constraint.ExpressionLiteral;
import Constraint.Term.*;
import org.eclipse.jdt.core.dom.ASTNode;

public class ConstraintTermFactory {

    private HashMap<ASTNode, NodeLabel> termMapEntry;
    private HashMap<ASTNode, NodeLabel> termMapExit;
    private List<ExpressionLiteral> exprList;

    public ConstraintTermFactory() {
        termMapEntry = new HashMap<>();
        termMapExit = new HashMap<>();
    }

    public NodeLabel createEntryLabel(ASTNode node) {
        NodeLabel t = termMapEntry.get(node);
        if (t == null) {
            List<ExpressionLiteral> newExprList = new ArrayList<>();
            for (ExpressionLiteral e : exprList) {
                ExpressionLiteral newExpr = new ExpressionLiteral(e.getNode(), e.getSymbVarName());
                newExprList.add(newExpr);
            }
            t = new EntryLabel(node, newExprList);
            termMapEntry.put(node, t);
        }

        return t;
    }

    public NodeLabel createExitLabel(ASTNode node) {
        NodeLabel t = termMapExit.get(node);
        if (t == null) {
            List<ExpressionLiteral> newExprList = new ArrayList<>();
            for (ExpressionLiteral e : exprList) {
                ExpressionLiteral newExpr = new ExpressionLiteral(e.getNode(), e.getSymbVarName());
                newExprList.add(newExpr);
            }
            t = new ExitLabel(node, newExprList);
            termMapExit.put(node, t);
        }
        return t;
    }


    public void setEntryLabel(ASTNode node, NodeLabel term) {
        termMapEntry.put(node, term);
    }

    public HashMap<ASTNode, NodeLabel> getTermMapEntry() {
        return termMapEntry;
    }

    public void setExprList(List<ExpressionLiteral> exprList) {
        this.exprList = exprList;
    }
}
