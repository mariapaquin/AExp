package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.List;

/**
 * [1] x = a + 1;
 *
 * ([1] entry \ { ( x + y ) } ) U { ( a + 1 ) }
 */
public class SetUnion extends ConstraintTerm {

    private SetDifference setDifference;
    private ConstraintTerm entryTerm;
    private ExpressionLiteral expr;
    private List<ExpressionLiteral> expressionList;

    public SetUnion(SetDifference setDifference, ExpressionLiteral expr) {
        this.setDifference = setDifference;
        this.expr = expr;
        this.entryTerm = null;
    }

    public SetUnion(ConstraintTerm entryTerm, ExpressionLiteral expr) {
        this.entryTerm = entryTerm;
        this.expr = expr;
        this.setDifference = null;
    }


    public SetDifference getSetDifference() {
        return setDifference;
    }

    public ExpressionLiteral getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return setDifference + " U " + expr;
    }

}