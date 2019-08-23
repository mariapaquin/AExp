package Constraint.Term;

import Constraint.ExpressionLiteral;
import Solving.AvailableExpressionSet;

import java.util.List;

/**
 * Represents a node in the constraint graph.
 * 
 */
public abstract class ConstraintTerm {

    public AvailableExpressionSet availableExpressionSet;

    private boolean isInitial;

    public interface TermProcessor {
        void processTerm(ConstraintTerm term);
    }

    public AvailableExpressionSet getAvailableExpressionSet() {
        return availableExpressionSet;
    }

    public void updateAESet(AvailableExpressionSet ds2) {
        availableExpressionSet = ds2;
    }

    public void initializeAESet(List<ExpressionLiteral> variables){

    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }
}
