package Solving;

import Constraint.ExpressionLiteral;
import Constraint.Term.ExitLabel;

import java.util.ArrayList;
import java.util.List;

public class AvailableExpressionSet {

    List<ExpressionLiteral> availableExpressions;

    public AvailableExpressionSet() {
        availableExpressions = new ArrayList<>();
    }

    public AvailableExpressionSet(List<ExpressionLiteral> expressionList) {
        availableExpressions = new ArrayList<>();
        for (ExpressionLiteral e : expressionList) {
            availableExpressions.add(e);
        }
    }

    public List<ExpressionLiteral> intersect(AvailableExpressionSet lhsEst) {
        System.out.println(lhsEst.getAvailableExpressions().size());
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e : availableExpressions) {
            System.out.println(e);
            boolean inIntersection = false;
            for (ExpressionLiteral e2 : lhsEst.getAvailableExpressions()) {
                System.out.println(e2);
                if(e.equals(e2)){
                    inIntersection = true;
                }
            }
            if (inIntersection) {
                expressions.add(e);
            }
        }

        return expressions;
    }

    public boolean containsAll(AvailableExpressionSet lhsEst) {
        return availableExpressions.containsAll(lhsEst.getAvailableExpressions());
    }

//    public void add(ExpressionLiteral expr) {
//        availableExpressions.add(expr);
//    }

    public List<ExpressionLiteral> getListContaining(List<ExpressionLiteral> exprsToAdd) {
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e : availableExpressions) {
                expressions.add(e);
        }

        for (ExpressionLiteral e : exprsToAdd) {
            expressions.add(e);
        }
        return expressions;
    }

    public List<ExpressionLiteral> getListSubtracting(List<ExpressionLiteral> exprsToSubtract) {
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e : availableExpressions) {
            if (!exprsToSubtract.contains(e)) {
                expressions.add(e);
            }
        }
        return expressions;
    }

    public List<ExpressionLiteral> getAvailableExpressions() {
        return availableExpressions;
    }

    public void setAvailableExpressions(List<ExpressionLiteral> availableExpressions) {
        this.availableExpressions = availableExpressions;
    }

    public void add(ExpressionLiteral e) {
        availableExpressions.add(e);
    }
}

