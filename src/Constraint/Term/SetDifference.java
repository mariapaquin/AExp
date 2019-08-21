package Constraint.Term;

import java.util.List;

/**
 * [1] x = a + 1;
 *
 * [1] entry \ { ( x + y ) }
 */
public class SetDifference extends ConstraintTerm {

    private ConstraintTerm entryTerm;
    private List<ExpressionLiteral> expressionList;

    public SetDifference(ConstraintTerm entryTerm, List<ExpressionLiteral> expressionList) {
        this.entryTerm = entryTerm;
        this.expressionList = expressionList;
    }

    public ConstraintTerm getEntryTerm() {
        return entryTerm;
    }

    public List<ExpressionLiteral> getExpr() {
        return expressionList;
    }

    @Override
    public String toString() {
        return entryTerm + " \\ " + expressionList;
    }

}