package ConstraintCreator;

import java.util.HashMap;

import Constraint.Term.*;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

public class ConstraintTermFactory {
    private HashMap<ASTNode, EntryLabel> termMapEntry;
    private HashMap<ASTNode, EntryUnionAE> termMapUnion;
    private HashMap<ASTNode, ExitLabel> termMapExit;

    public ConstraintTermFactory() {
        termMapEntry = new HashMap<>();
        termMapExit = new HashMap<>();
        termMapUnion = new HashMap<>();
    }

    public EntryLabel createEntryLabel(ASTNode node) {
        EntryLabel t = termMapEntry.get(node);
        if (t == null) {
            t = new EntryLabel((node));
            termMapEntry.put(node, t);
        }

        return t;
    }

    public ExitLabel createExitLabel(ASTNode node) {
        ExitLabel t = termMapExit.get(node);
        if (t == null) {
            t = new ExitLabel(node);
            termMapExit.put(node, t);
        }
        return t;
    }


    public EntryUnionAE createEntryUnionLabel(ASTNode node) {
        EntryUnionAE t = termMapUnion.get(node);
        if (t == null) {
            EntryLabel entry = termMapEntry.get(node);
            t = new EntryUnionAE(entry);
            termMapUnion.put(node, t);
        }
        return t;
    }

    public ExpressionLiteral createExpressionLiteral(Expression expr) {
        ExpressionLiteral def = new ExpressionLiteral(expr);
        return def;
    }


    public EntryLabel getEntryLabel(ASTNode node) {
        return termMapEntry.get(node);
    }

    public ExitLabel getExitLabel(ASTNode node) {
        return termMapExit.get(node);
    }

}
