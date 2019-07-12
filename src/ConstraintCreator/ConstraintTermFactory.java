package ConstraintCreator;

import java.util.HashMap;
import java.util.LinkedHashMap;

import Constraint.Term.ConstraintTerm;
import Constraint.Term.DefinitionLiteral;
import Constraint.Term.EntryLabel;
import Constraint.Term.ExitLabel;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

public class ConstraintTermFactory {
    private HashMap<ASTNode, ConstraintTerm> termMapEntry;
    private HashMap<ASTNode, ConstraintTerm> termMapExit;
    private HashMap<SimpleName, HashMap<ASTNode, DefinitionLiteral>> varMap;

    public ConstraintTermFactory() {
        termMapEntry = new HashMap<>();
        termMapExit = new HashMap<>();
        varMap = new LinkedHashMap<>();
    }

    public ConstraintTerm createEntryLabel(ASTNode node) {
        ConstraintTerm t = termMapEntry.get(node);
        if (t == null) {
            t = new EntryLabel((node));
            termMapEntry.put(node, t);
        }

        return t;
    }

    public void setEntryLabel(ASTNode node, ConstraintTerm term) {
        termMapEntry.put(node, term);
    }

    public ConstraintTerm createExitLabel(ASTNode node) {
        ConstraintTerm t = termMapExit.get(node);
        if (t == null) {
            t = new ExitLabel(node);
            termMapExit.put(node, t);
        }
        return t;
    }

    public ConstraintTerm createDefinition(String name, ASTNode node) {
        DefinitionLiteral def = new DefinitionLiteral(name, node);
        return def;
    }

    public DefinitionLiteral createDefinitionWildcard(String name) {
        DefinitionLiteral defWild = new DefinitionLiteral(name);
        return defWild;
    }

    public ConstraintTerm getEntryLabel(ASTNode node) {
        return termMapEntry.get(node);
    }

    public ConstraintTerm getExitLabel(ASTNode node) {
        return termMapExit.get(node);
    }

}
