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
    private int symbVarCount;


    public ConstraintSolver(ArrayList<Constraint> constraints, int symbVarCount) {
        this.constraints = constraints;
        this.entryMap = new HashMap<>();
        this.symbVarCount = symbVarCount;
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
            System.out.println("Starting iteration " + ++iteration
                    + "\n--------------------");
            for (int j = 0; j < workList.size(); j++) {
                NodeLabel t = workList.get(j);
                System.out.println("Constraint.Term:" + t);
                int h = 0;
                for (Constraint c : graph.getConstraintsInvolving(t)) {
                     System.out.println(++h + ": checking Constraint " + c + "...");
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

        List<ExpressionLiteral> prev = new ArrayList<>();

        System.out.println("lhs is " + lhsAE + ", rhs is " + rhsAE);

        for (ExpressionLiteral e: lhsAE) {
            ExpressionLiteral newExpr = new ExpressionLiteral(e.getNode(), e.getSymbVarNum());
            prev.add(newExpr);
        }


        for (ExpressionLiteral lhs_e : lhsAE) {
            for (ExpressionLiteral rhs_e : rhsAE) {
                if (lhs_e.equals(rhs_e)) {
                    if (lhs_e.getSymbVarNum() < rhs_e.getSymbVarNum()) {
                        System.out.println(lhs_e.getSymbVarNum() + " does not equal " + rhs_e.getSymbVarNum());
                        lhs.setSymbVarNum(lhs_e, symbVarCount++);
                        System.out.println("setting lhs name to " + lhs_e.getSymbVarNum());
                    }
                }
            }
        }

        if (changed(prev, lhs.getExprList())) {
            change = true;
       }
    }


    private boolean changed(List<ExpressionLiteral> prevList, List<ExpressionLiteral> newList) {
        for (ExpressionLiteral e : prevList) {
            for (ExpressionLiteral e2 : newList) {
                if (e.equals(e2)) {
                    if (e.getSymbVarNum() < e2.getSymbVarNum()) {
                        return true;
                    }
                }
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
