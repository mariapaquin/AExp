package Constraint;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionLiteral {

    private Expression node;
    private List<String> varsUsed;
    private int symbVarNum;

    public ExpressionLiteral(Expression node, int symbVarNum) {
        this.node = node;
        this.symbVarNum = symbVarNum;
        varsUsed = new ArrayList<>();
    }

    public void setVarsUsed(List<String> varsUsed) {
        this.varsUsed = varsUsed;
    }

    public boolean involves(String var) {
        return varsUsed.contains(var);
    }

    public String toString() {
        return "(" + node + ")";
    }

    public Expression getNode() {
        return node;
    }

    public int getSymbVarNum() {
        return symbVarNum;
    }

    public void setSymbVarNum(int symbVarNum) {
        this.symbVarNum = symbVarNum;
    }

    @Override
    public boolean equals(Object obj) {
        return node.toString().equals(((ExpressionLiteral) obj).getNode().toString());
    }
}
