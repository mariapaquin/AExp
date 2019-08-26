package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.ArrayList;
import java.util.List;

/**
 * [1] x = a + 1;
 *
 * [1] entry \ { ( x + y ) }
 */
public class SetDifference extends ConstraintTerm {

    private EntryLabel entryTerm;
    private List<ExpressionLiteral> expressionsToSubtract;

    public SetDifference(EntryLabel entryTerm, List<ExpressionLiteral> expressionsToSubtract) {
        this.entryTerm = entryTerm;
        this.expressionsToSubtract = expressionsToSubtract;
    }

    public void setAvailableExpressions(List<ExpressionLiteral> expressions) {
        entryTerm.setAvailableExpressions(expressions);
    }

    public List<ExpressionLiteral>  getAvailableExpressions() {
        return getListSubtracting(expressionsToSubtract);
    }

    private List<ExpressionLiteral> getListSubtracting(List<ExpressionLiteral> exprsToSubtract) {
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e : entryTerm.getAvailableExpressions()) {
            if (!exprsToSubtract.contains(e)) {
                expressions.add(e);
            }
        }
        return expressions;
    }

    @Override
    public String toString() {
        return entryTerm + " \\ " + expressionsToSubtract;
    }
}