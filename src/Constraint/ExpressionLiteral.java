package Constraint;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;


public class ExpressionLiteral {

    private Expression node;
    private List<String> varsUsed;


    private String symbVarName;

    public ExpressionLiteral(Expression node, String symbVarName) {
        this.node = node;
        this.symbVarName = symbVarName;
        varsUsed = new ArrayList<>();
    }

    public void setVarsUsed(List<String> varsUsed) {
        this.varsUsed = varsUsed;
    }

    public boolean involves(String var) {
        return varsUsed.contains(var);
    }

    public String toString() {
        return "(" + node + ") -> " + symbVarName;
    }

    public Expression getNode() {
        return node;
    }

    public String getSymbVarName() {
        return symbVarName;
    }

    public void setSymbVarName(String symbVarName) {
        this.symbVarName = symbVarName;
    }

    @Override
    public boolean equals(Object obj) {
        return node.toString().equals(((ExpressionLiteral) obj).getNode().toString());
    }
}
