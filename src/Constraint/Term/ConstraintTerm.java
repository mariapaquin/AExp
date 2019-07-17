package Constraint.Term;

import Solving.AvailableExpressionSet;
import java.util.Set;

/**
 * Represents a node in the constraint graph.
 * 
 */
public abstract class ConstraintTerm {

    public AvailableExpressionSet availableExpressionSet;

    public interface TermProcessor {
        void processTerm(ConstraintTerm term);
    }

    public AvailableExpressionSet getAvailableExpressionSet() {
        return availableExpressionSet;
    }

    public void updateDefinitionSet(AvailableExpressionSet ds2) {
        availableExpressionSet = ds2;
    }

    public void initializeDefinitionSet(Set<String> variables){

    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }
}
