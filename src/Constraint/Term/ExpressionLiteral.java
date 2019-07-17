package Constraint.Term;

import Solving.AvailableExpressionSet;
import org.eclipse.jdt.core.dom.*;

import java.util.Set;

/**
 * (v,n) or (v,*)
 */
public class ExpressionLiteral extends ConstraintTerm {

    private Expression node;

    public ExpressionLiteral(Expression node) {
        this.node = node;
    }

    @Override
    public void initializeDefinitionSet(Set<String> variables) {
    }

    public String toString() {
        return "(" + node + ")";
    }

    public Expression getNode() {
        return node;
    }
}
