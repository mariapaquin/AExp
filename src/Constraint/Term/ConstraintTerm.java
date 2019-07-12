package Constraint.Term;

import Solving.DefinitionSet;
import java.util.Set;

/**
 * Represents a node in the constraint graph.
 * 
 */
public abstract class ConstraintTerm {

    public DefinitionSet definitionSet;

    public interface TermProcessor {
        void processTerm(ConstraintTerm term);
    }

    public DefinitionSet getDefinitionSet() {
        return definitionSet;
    }

    public void updateDefinitionSet(DefinitionSet ds2) {
        definitionSet = ds2;
    }

    public void initializeDefinitionSet(Set<String> variables){
        definitionSet = new DefinitionSet(variables);
    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }
}
