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
            term.initializeDefinitionSet(expressionList);
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

        for (int j = 0; j < workList.size(); j++) {
            ConstraintTerm t = workList.get(j);
            System.out.println(t + "\n--------------\n" + t.getAvailableExpressionSet());
            System.out.println();
        }
    }

    private void satisfyConstraint(Constraint constraint) {
        ConstraintTerm lhs = constraint.getLhs();
        ConstraintTerm rhs = constraint.getRhs();

        AvailableExpressionSet lhsEst = lhs.getAvailableExpressionSet();
        AvailableExpressionSet rhsEst = rhs.getAvailableExpressionSet();

//        if (!rhsEst.containsAll(lhsEst)) {
//            System.out.println(lhs.getAvailableExpressionSet() + " is not in " + rhs.getAvailableExpressionSet());
//            System.out.println("Performing union operation...");
//
//            // copy the previous definition set for change detection
//            HashMap<String, List<ExpressionLiteral>> prev = new HashMap<>();
//            for (String var : rhsEst.getVariables()) {
//                prev.put(var, rhsEst.get(var));
//            }
//
//            AvailableExpressionSet union = rhsEst.unionWith(lhsEst);
//
//            rhs.updateDefinitionSet(union);
//
//            if (changed(prev, rhs.getAvailableExpressionSet().getVarMap())) {
//                System.out.println("set was changed");
//                change = true;
//            }
//            System.out.println(lhs.getAvailableExpressionSet() + " is now in " + rhs.getAvailableExpressionSet());
//
//        } else {
//            System.out.println(lhsEst + " is in " + rhsEst);
//            System.out.println("Constraint already satisfied.");
//        }
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
