package Solving;

import Constraint.Constraint;
import Constraint.Term.*;
import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.*;

public class ConstraintSolver {

    private boolean change;
    private ConstraintGraph graph;
    private ArrayList<Constraint> constraints;
    private HashMap<ASTNode, List<String>> entryMap;


    public ConstraintSolver(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
        this.entryMap = new HashMap<>();

    }

    public void buildConstraintGraph() {
        graph = new ConstraintGraph(constraints);
        graph.initialize();
    }


    public void processWorkList() {
        List<NodeLabel> workList = graph.getAllTerms();
        int iteration = 0;
        change = true;
        while (change) {
            change = false;
//            System.out.println("Starting iteration " + ++iteration
//                    + "\n--------------------");
            for (int j = 0; j < workList.size(); j++) {
                NodeLabel t = workList.get(j);
//                System.out.println("Constraint.Term:" + t);
                int h = 0;
                for (Constraint c : graph.getConstraintsInvolving(t)) {
//                     System.out.println(++h + ": checking Constraint " + c + "...");
                     satisfyConstraint(c);
                }
//                System.out.println("\n");
            }
        }

        System.out.println();

        for (int j = 0; j < workList.size(); j++) {
            NodeLabel t = workList.get(j);
            System.out.println(t + "\n--------------\n" + t.getExprList());
            System.out.println();
        }
    }

    private void satisfyConstraint(Constraint constraint) {
        NodeLabel lhs = constraint.getLhs();
        NodeLabel rhs = constraint.getRhs();

        List<ExpressionLiteral> lhsAE = lhs.getExprList();
        List<ExpressionLiteral> rhsAE = rhs.getExprList();

        if (!rhsAE.containsAll(lhsAE)) {
//            System.out.println(lhsAE + " is not in " + rhsAE);
//            System.out.println("Performing intersection operation...");

//          copy the previous expression list for change detection
            List<ExpressionLiteral> prev = new ArrayList<>();
            for (ExpressionLiteral e: rhsAE) {
                prev.add(e);
            }

            List<ExpressionLiteral> intersection = intersect(lhsAE, rhsAE);

            lhs.setExprList(intersection);

            if (changed(prev, lhs.getExprList())) {
//                System.out.println("LHS was changed");
                change = true;
            }

//            System.out.println(lhs.getExprList()
//                    + " is now in " + rhs.getExprList());

        } else {
//            System.out.println(lhsAE + " is already in " + rhsAE);
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

    private boolean changed(List<ExpressionLiteral> prevList, List<ExpressionLiteral> newList) {
        List<String> prevListString = new ArrayList<>();
        List<String> newListString = new ArrayList<>();

        for (ExpressionLiteral e : prevList) {
            prevListString.add(e.toString());
        }

        for (ExpressionLiteral e2 : newList) {
            newListString.add(e2.toString());
        }

        for (String s: prevListString) {
            if (!newListString.contains(s)) {
                return true;
            }
        }

        return false;
    }

    public void buildEntryMap() {
        List<NodeLabel> workList = graph.getAllTerms();

        for (int j = 0; j < workList.size(); j++) {
            NodeLabel t = workList.get(j);
            ASTNode node = t.getNode();
            entryMap.put(node, t.getAvailableExpressionsAsString());
        }
    }

    public HashMap<ASTNode, List<String>> getEntryMap() {
        return entryMap;
    }

}
