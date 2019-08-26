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
        super();
        this.entryTerm = entryTerm;
        this.expressionsToSubtract = expressionsToSubtract;
    }

    public void updateAE(List<ExpressionLiteral> expressions) {
        entryTerm.updateAE(expressions);
    }


    public List<ExpressionLiteral>  getAvailableExpressions() {
        return getListSubtracting(expressionsToSubtract);
    }

    @Override
    public String toString() {
        return entryTerm + " \\ " + expressionsToSubtract;
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
}