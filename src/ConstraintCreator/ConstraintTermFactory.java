package ConstraintCreator;

import java.util.HashMap;

import Constraint.Term.*;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

public class ConstraintTermFactory {
    private HashMap<ASTNode, ConstraintTerm> termMapEntry;
    private HashMap<ASTNode, ConstraintTerm> termMapExit;

    public ConstraintTermFactory() {
        termMapEntry = new HashMap<>();
        termMapExit = new HashMap<>();
    }

    public ConstraintTerm createEntryLabel(ASTNode node) {
        ConstraintTerm t = termMapEntry.get(node);
        if (t == null) {
            t = new EntryLabel((node));
            termMapEntry.put(node, t);
        }

        return t;
    }

    public ConstraintTerm createExitLabel(ASTNode node) {
        ConstraintTerm t = termMapExit.get(node);
        if (t == null) {
            t = new ExitLabel(node);
            termMapExit.put(node, t);
        }
        return t;
    }

    public ExpressionLiteral createExpressionLiteral(Expression expr) {
        ExpressionLiteral def = new ExpressionLiteral(expr);
        return def;
    }

    public void setEntryLabel(ASTNode node, ConstraintTerm term) {
        termMapEntry.put(node, term);
    }

}
