package Constraint.Term;

import Solving.DefinitionSet;

/**
 * Subtracting definition from the entry term set
 */
public class SetDifference extends ConstraintTerm {

    private ConstraintTerm entryTerm;
    private DefinitionLiteral defWild;

    public SetDifference(ConstraintTerm entryTerm, DefinitionLiteral defWild) {
        this.entryTerm = entryTerm;
        this.defWild = defWild;
    }

    public void updateDefinitionSet(DefinitionSet ds2) {
        definitionSet = ds2;
        String var = defWild.getName();
        definitionSet.killDefinitions(var);
    }

    public ConstraintTerm getEntryTerm() {
        return entryTerm;
    }

    public DefinitionLiteral getDefWild() {
        return defWild;
    }

    @Override
    public String toString() {
        return entryTerm + "\\" + defWild;
    }

}

