package Constraint.Term;

import java.util.List;

/**
 * Subtracting expression from the entry term set
 */
public class SetDifference extends ConstraintTerm {

    private ConstraintTerm entryTerm;
    private ExpressionLiteral expr;
    private List<ExpressionLiteral> expressionList;

    public SetDifference(ConstraintTerm entryTerm, ExpressionLiteral expr) {
        this.entryTerm = entryTerm;
        this.expr = expr;
    }


    public ConstraintTerm getEntryTerm() {
        return entryTerm;
    }

    public ExpressionLiteral getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return entryTerm + "\\" + expr;
    }

}