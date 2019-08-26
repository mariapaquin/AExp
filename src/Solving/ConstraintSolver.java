package Solving;

import Constraint.Constraint;
import Constraint.Term.*;
import Constraint.ExpressionLiteral;

import java.util.*;

public class ConstraintSolver {

    private boolean change;
    public ConstraintGraph graph;
    private ArrayList<Constraint> constraints;
    private List<ExpressionLiteral> expressionList;

    public ConstraintSolver(ArrayList<Constraint> constraints, List<ExpressionLiteral> expressionList) {
        this.constraints = constraints;
        this.expressionList = expressionList;
    }

    public void buildConstraintGraph() {
        graph = new ConstraintGraph(constraints);
        graph.initialize();
    }

    public void initializeAESet() {
        for (ConstraintTerm term : graph.getAllTerms()) {
            if (term instanceof NodeLabel) {
                if (((NodeLabel) term).isInitial()) {
                    ((NodeLabel) term).initializeAE(new ArrayList<ExpressionLiteral>());
                } else {
                    ((NodeLabel) term).initializeAE(expressionList);
                }
            }
        }
    }

    public void processWorkList() {
        List<ConstraintTerm> workList = graph.getAllTerms();
        int iteration = 0;
        change = true;
        while (change) {
            change = false;
            System.out.println("Starting iteration " + ++iteration
                    + "\n--------------------");
            for (int j = 0; j < workList.size(); j++) {
                ConstraintTerm t = workList.get(j);
                System.out.println("Constraint.Term:" + t);
                int h = 0;
                for (Constraint c : graph.getConstraintsInvolving(t)) {
                     System.out.println(++h + ": checking Constraint " + c + "...");
                     satisfyConstraint(c);
                }
                System.out.println("\n");
            }
        }

        System.out.println();

//        for (int j = 0; j < workList.size(); j++) {
//            ConstraintTerm t = workList.get(j);
//            System.out.println(t + "\n--------------\n" + t.getAvailableExpressionSet());
//            System.out.println();
//        }
    }

    private void satisfyConstraint(Constraint constraint) {
        ConstraintTerm lhs = constraint.getLhs();
        ConstraintTerm rhs = constraint.getRhs();

        List<ExpressionLiteral> lhsAE = lhs.getAvailableExpressions();
        List<ExpressionLiteral> rhsAE = rhs.getAvailableExpressions();

        if (!rhsAE.containsAll(lhsAE)) {
            System.out.println(lhsAE + " is not in " + rhsAE);
            System.out.println("Performing intersection operation...");

//          copy the previous expression list for change detection
            AvailableExpressionSet prev = new AvailableExpressionSet();
            for (ExpressionLiteral e: rhsAE) {
                prev.add(e);
            }

            List<ExpressionLiteral> intersection = intersect(lhsAE, rhsAE);

            lhs.updateAE(intersection);

//////            if (changed(prev, rhs.getAvailableExpressionSet().getVarMap())) {
//////                System.out.println("set was changed");
//////                change = true;
//////            }

            System.out.println(lhs.getAvailableExpressions()
                    + " is now in " + rhs.getAvailableExpressions());

        } else {
            System.out.println(lhsAE + " is already in " + rhsAE);
        }
    }

    private List<ExpressionLiteral> intersect(List<ExpressionLiteral> lhsAE, List<ExpressionLiteral> rhsAE) {
        List<ExpressionLiteral> expressions = new ArrayList();

        for (ExpressionLiteral e: lhsAE) {
            boolean inIntersection = false;
            for (ExpressionLiteral e2: rhsAE) {
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

    private boolean changed(HashMap<String, List<ExpressionLiteral>> prevMap, HashMap<String, List<ExpressionLiteral>> newMap) {

//        for (String var : newMap.keySet()) {
//            List<ExpressionLiteral> l1 = prevMap.get(var);
//            List<ExpressionLiteral> l2 = newMap.get(var);
//
//            if (l1 == null || l2 == null) {
//                return true;
//            }
//            for (ExpressionLiteral def : l2) {
//                if (!l1.contains(def)) {
//                    return true;
//                }
//            }
//
//            for (ExpressionLiteral def : l1) {
//                if (!l2.contains(def)) {
//                    return true;
//                }
//            }
//        }

        return false;
    }

}
