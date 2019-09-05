package Constraint;

import Constraint.Term.NodeLabel;

/**
 * An edge in the constraint graph.
 */
public class Constraint {

    private NodeLabel lhs;
    private NodeLabel rhs;
    private SubsetOperator op;

    public Constraint(NodeLabel lhs, SubsetOperator op, NodeLabel rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public NodeLabel getLhs() {
        return lhs;
    }

    public NodeLabel getRhs() {
        return rhs;
    }

    public SubsetOperator getOp() {
        return op;
    }

    public String toString() {
        return lhs + " " + op + " " + rhs;
    }

}
