package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.List;

/**
 * Represents a node in the constraint graph.
 * 
 */
public abstract class ConstraintTerm {
    public List<ExpressionLiteral> availableExpressions;

    public abstract List<ExpressionLiteral> getAvailableExpressions();
    public abstract void setAvailableExpressions(List<ExpressionLiteral> expressions);

    public interface TermProcessor {
        void processTerm(ConstraintTerm term);
    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }

}
