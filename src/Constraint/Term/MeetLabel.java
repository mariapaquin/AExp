package Constraint.Term;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.*;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.List;

public class MeetLabel extends ConstraintTerm {
    List<NodeLabel> nodes;

    public MeetLabel(List<ExpressionLiteral> exprList) {
        super(exprList);
        nodes = new ArrayList<>();
    }

    public void addNodeLabel(NodeLabel prevExit) {
        nodes.add(prevExit);
    }

    public String toString() {
        String str = "";

        for (int i = 0; i < nodes.size() - 1; i++) {
            str += nodes.get(i).toString() + " /\\ ";
        }

        str += nodes.get(nodes.size() - 1);
        return str;
    }
}
