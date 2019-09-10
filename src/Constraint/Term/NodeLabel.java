package Constraint.Term;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class NodeLabel extends ConstraintTerm {
    public List<ExpressionLiteral> exprList;
    protected ASTNode node;

    public NodeLabel(ASTNode node, List<ExpressionLiteral> exprList) {
        super(exprList);
        this.node = node;
    }
    public ASTNode getNode(){
        return node;
    }


}
