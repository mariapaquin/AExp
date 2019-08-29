package Constraint.Term;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

public abstract class ConstraintTerm {
    public List<ExpressionLiteral> availableExpressions;
    public boolean isInitial;
    protected ASTNode node;

    public abstract List<ExpressionLiteral> getAvailableExpressions();
    public abstract void setAvailableExpressions(List<ExpressionLiteral> expressions);
    public abstract ASTNode getNode();

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
