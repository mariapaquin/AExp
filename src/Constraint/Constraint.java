package Constraint;

import Constraint.Term.ConstraintTerm;
import Constraint.Term.NodeLabel;

/**
 * An edge in the constraint graph.
 */
public class Constraint {

    private ConstraintTerm lhs;
    private ConstraintTerm rhs;
    private RelOperator op;

    public Constraint(ConstraintTerm lhs, RelOperator op, ConstraintTerm rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public ConstraintTerm getLhs() {
        return lhs;
    }

    public ConstraintTerm getRhs() {
        return rhs;
    }

    public RelOperator getOp() {
        return op;
    }

    public String toString() {
        return lhs + " " + op + " " + rhs;
    }

}
