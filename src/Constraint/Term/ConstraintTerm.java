package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.List;

/**
 * Represents a node in the constraint graph.
 * 
 */
public abstract class ConstraintTerm {
    public List<ExpressionLiteral> availableExpressions;
    public boolean isInitial;

    public abstract List<ExpressionLiteral> getAvailableExpressions();
    public abstract void setAvailableExpressions(List<ExpressionLiteral> expressions);

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean initial) {
        isInitial = initial;
    }

    public interface TermProcessor {
        void processTerm(ConstraintTerm term);
    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }

}
