package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.ArrayList;
import java.util.List;

/**
 * [1] x = a + 1;
 *
 * ([1] entry \ { ( x + y ) } ) U { ( a + 1 ) }
 */
public class SetUnion extends ConstraintTerm {

    private SetDifference setDifference;
    private EntryLabel entryTerm;
    private List<ExpressionLiteral> exprToAdd;

    public SetUnion(SetDifference setDifference, List<ExpressionLiteral> exprToAdd) {
        super();
        this.setDifference = setDifference;
        this.exprToAdd = exprToAdd;
        this.entryTerm = null;
    }

    public SetUnion(EntryLabel entryTerm, List<ExpressionLiteral> exprToAdd) {
        super();
        this.entryTerm = entryTerm;
        this.exprToAdd = exprToAdd;
        this.setDifference = null;
    }

    public void updateAE(List<ExpressionLiteral> expressions) {
        if (entryTerm != null) {
            entryTerm.updateAE(expressions);
        } else{
            setDifference.updateAE(expressions);
        }
    }


    public List<ExpressionLiteral> getAvailableExpressions() {
        if (entryTerm != null) {
            return getListContaining(entryTerm, exprToAdd);
        }
        return getListContaining(setDifference, exprToAdd);
    }

    private List<ExpressionLiteral> getListContaining(ConstraintTerm term, List<ExpressionLiteral> exprsToAdd) {
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e: term.getAvailableExpressions()) {
            expressions.add(e);
        }

        for (ExpressionLiteral e: exprsToAdd) {
            if (!expressions.contains(e)) {
                expressions.add(e);
            }
        }
        return expressions;
    }
    @Override
    public String toString() {
        return setDifference + " U " + exprToAdd;
    }

}