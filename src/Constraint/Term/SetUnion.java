package Constraint.Term;

import java.util.List;

/**
 * [1] x = a + 1;
 *
 * ([1] entry \ { ( x + y ) } ) U { ( a + 1 ) }
 */
public class SetUnion extends ConstraintTerm {

    private SetDifference setDifference;
    private ExpressionLiteral expr;
    private List<ExpressionLiteral> expressionList;

    public SetUnion(SetDifference setDifference, ExpressionLiteral expr) {
        this.setDifference = setDifference;
        this.expr = expr;
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