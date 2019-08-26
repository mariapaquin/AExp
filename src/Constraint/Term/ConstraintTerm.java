package Constraint.Term;

import Constraint.ExpressionLiteral;
import Solving.AvailableExpressionSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the constraint graph.
 * 
 */
public abstract class ConstraintTerm {
    protected List<ExpressionLiteral> availableExpressions;

    public abstract List<ExpressionLiteral> getAvailableExpressions();

    public abstract void updateAE(List<ExpressionLiteral> expressions);

    public interface TermProcessor {
        void processTerm(ConstraintTerm term);
    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }

}
