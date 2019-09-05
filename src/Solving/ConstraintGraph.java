package Solving;

import Constraint.Constraint;
import Constraint.Term.NodeLabel;

import java.util.*;

public class ConstraintGraph {
    private ArrayList<Constraint> constraints;
    private ArrayList<NodeLabel> allTerms;
    private HashMap<NodeLabel, List<Constraint>> edgeMap;

    public ConstraintGraph(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
        allTerms = new ArrayList<>();
        edgeMap = new HashMap<NodeLabel, List<Constraint>>();
    }

    public void initialize() {
        TermDecorator decorator = new TermDecorator();
        for (Constraint c : constraints) {
            NodeLabel lhs = c.getLhs();
            NodeLabel rhs = c.getRhs();

            decorator.setConstraint(c);
            lhs.processTerms(decorator);
            rhs.processTerms(decorator);
        }
    }

    public List<Constraint> getConstraintsInvolving(NodeLabel term) {
        return edgeMap.get(term);
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public HashMap<NodeLabel, List<Constraint>> getEdgeMap() {
        return edgeMap;
    }

    public ArrayList<NodeLabel> getAllTerms() {
        return allTerms;
    }

    private class TermDecorator implements NodeLabel.TermProcessor {
        private Constraint constraint;

        public void setConstraint(Constraint constraint) {
            this.constraint = constraint;
        }

        public void processTerm(NodeLabel term) {
//			System.out.println(term + " " + term.hashCode());
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
