package Solving;

import Constraint.Constraint;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.NodeLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConstraintGraph {
    private ArrayList<Constraint> constraints;
    private ArrayList<ConstraintTerm> allTerms;
    private HashMap<ConstraintTerm, List<Constraint>> edgeMap;

    public ConstraintGraph(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
        allTerms = new ArrayList<>();
        edgeMap = new HashMap<ConstraintTerm, List<Constraint>>();
    }

    public void initialize() {
        TermDecorator decorator = new TermDecorator();
        for (Constraint c : constraints) {
            ConstraintTerm lhs = c.getLhs();
            ConstraintTerm rhs = c.getRhs();

            decorator.setConstraint(c);
            lhs.processTerms(decorator);
            rhs.processTerms(decorator);
        }
    }

    public List<Constraint> getConstraintsInvolving(ConstraintTerm term) {
        return edgeMap.get(term);
    }

    public List<Constraint> getConstraintsWithLHS(ConstraintTerm term) {
        List<Constraint> allConstraintsWithTerm = edgeMap.get(term);
        List<Constraint> constraintsWithTermOnLHS = new ArrayList<Constraint>();

        for (Constraint c : allConstraintsWithTerm) {
            if (c.getLhs().equals(term)) {
                constraintsWithTermOnLHS.add(c);
            }
        }
        return constraintsWithTermOnLHS;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public HashMap<ConstraintTerm, List<Constraint>> getEdgeMap() {
        return edgeMap;
    }

    public ArrayList<ConstraintTerm> getAllTerms() {
        return allTerms;
    }

    private class TermDecorator implements ConstraintTerm.TermProcessor {
        private Constraint constraint;

        public void setConstraint(Constraint constraint) {
            this.constraint = constraint;
        }

        public void processTerm(ConstraintTerm term) {
            List<Constraint> c;
            if (edgeMap.containsKey(term)) {
                c = edgeMap.get(term);
            } else {
                c = new ArrayList<Constraint>();
            }
            c.add(constraint);
            edgeMap.put(term, c);

            if (!allTerms.contains(term)) {
                allTerms.add(term);
            }
        }

    }
}
