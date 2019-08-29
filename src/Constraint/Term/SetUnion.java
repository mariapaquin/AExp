package Constraint.Term;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * [1] x = a + 1;
 *
 * ([1] entry U { ( a + 1 ) }
 */
public class SetUnion extends ConstraintTerm {


    private EntryLabel entryTerm;
    private List<ExpressionLiteral> exprToAdd;

    public SetUnion(EntryLabel entryTerm, List<ExpressionLiteral> exprToAdd) {
        super();
        this.entryTerm = entryTerm;
        this.exprToAdd = exprToAdd;
    }

    public void setAvailableExpressions(List<ExpressionLiteral> expressions) {
        entryTerm.setAvailableExpressions(expressions);

    }

    @Override
    public ASTNode getNode() {
        return entryTerm.getNode();
    }

    public List<ExpressionLiteral> getAvailableExpressions() {
        return getListContaining(exprToAdd);

    }

    private List<ExpressionLiteral> getListContaining(List<ExpressionLiteral> exprsToAdd) {
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e: entryTerm.getAvailableExpressions()) {
            expressions.add(e);
        }

        for (ExpressionLiteral e: exprsToAdd) {
            if (!expressions.contains(e)) {
                expressions.add(e);
            }
        }
        return expressions;
    }

    public EntryLabel getEntryTerm() {
        return entryTerm;
    }

    @Override
    public String toString() {
        return entryTerm + " U " + exprToAdd;
    }

}
