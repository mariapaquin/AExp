package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.ArrayList;
import java.util.List;

public class ConstraintTerm {
    public List<ExpressionLiteral> exprList;

    public ConstraintTerm(List<ExpressionLiteral> exprList){
        this.exprList = exprList;
    }

    public void setSymbVarNum(ExpressionLiteral expr, int varNum){
        for (ExpressionLiteral e : exprList) {
            if (e.equals(expr)) {
                e.setSymbVarNum(varNum);
            }
        }
    }

    public List<ExpressionLiteral> getExprList() {
        return exprList;
    }

    public List<String> getAvailableExpressionsAsString() {
        List<String> ret = new ArrayList<>();
        for (ExpressionLiteral e : exprList) {
            ret.add(e.getNode().toString());
        }
        return ret;
    }

    public interface TermProcessor {
        void processTerm(ConstraintTerm term);
    }

    public void processTerms(TermProcessor processor) {
        processor.processTerm(this);
    }
}
