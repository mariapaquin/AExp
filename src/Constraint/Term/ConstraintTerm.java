package Constraint.Term;

import Constraint.ExpressionLiteral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConstraintTerm {
//    public List<ExpressionLiteral> exprList;
    public HashMap<ExpressionLiteral, Integer> exprMap;

    public ConstraintTerm(List<ExpressionLiteral> exprList){
//        this.exprList = exprList;
        exprMap = new HashMap<>();
        for (ExpressionLiteral expr : exprList) {
            exprMap.put(expr, expr.getSymbVarNum());
        }
    }

    public void setSymbVarNum(ExpressionLiteral expr, int varNum){
        for (ExpressionLiteral e : exprMap.keySet()) {
            if (e.equals(expr)) {
                e.setSymbVarNum(varNum);
                exprMap.put(e, varNum);
            }
        }
/*        for (ExpressionLiteral e : exprList) {
            if (e.equals(expr)) {
                e.setSymbVarNum(varNum);
            }
        }*/
    }

    public ExpressionLiteral getExpr(ExpressionLiteral expr) {
        for (ExpressionLiteral e : exprMap.keySet()) {
            if (e.equals(expr)) {
                return e;
            }
        }

/*        for (ExpressionLiteral e : exprList) {
            if (e.equals(expr)) {
                return e;
            }
        }*/

        return null;
    }

    public HashMap<ExpressionLiteral, Integer> getExprMap() {
        return exprMap;
    }

    public List<String> getAvailableExpressionsAsString() {
        List<String> ret = new ArrayList<>();
        for (ExpressionLiteral e : exprMap.keySet()) {
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
