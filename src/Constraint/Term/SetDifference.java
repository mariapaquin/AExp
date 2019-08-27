package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.ArrayList;
import java.util.List;

/**
 * [1] x = a + 1;
 *
 * [1] entry U { ( a + 1 ) } \ { ( x + y ) } )
 */
public class SetDifference extends ConstraintTerm {

    private List<ExpressionLiteral> expressionsToSubtract;
    private SetUnion setUnion;

    public SetDifference(SetUnion setUnion, List<ExpressionLiteral> expressionsToSubtract) {
        this.setUnion = setUnion;
        this.expressionsToSubtract = expressionsToSubtract;
    }

    public void setAvailableExpressions(List<ExpressionLiteral> expressions) {
        setUnion.setAvailableExpressions(expressions);
    }

    public List<ExpressionLiteral>  getAvailableExpressions() {
        return getListSubtracting(expressionsToSubtract);
    }

    private List<ExpressionLiteral> getListSubtracting(List<ExpressionLiteral> exprsToSubtract) {
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e : setUnion.getAvailableExpressions()) {
            if (!exprsToSubtract.contains(e)) {
                expressions.add(e);
            }
        }
        return expressions;
    }

    @Override
    public String toString() {
        return setUnion + " \\ " + expressionsToSubtract;
    }
}