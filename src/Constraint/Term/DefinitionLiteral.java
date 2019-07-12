package Constraint.Term;

import Solving.DefinitionSet;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import java.util.Set;

/**
 * (v,n) or (v,*)
 */
public class DefinitionLiteral extends ConstraintTerm {

    private String name;
    private ASTNode node;

    public DefinitionLiteral(String name){
        this.name = name;
    }

    public DefinitionLiteral(String name, ASTNode node) {
        this.name = name;
        this.node = node;
    }

    @Override
    public void initializeDefinitionSet(Set<String> variables){
        definitionSet = new DefinitionSet(variables, this);
    }

    public String toString() {
        if (node == null) {
            return "(" + name + ", *)";
        } else {

            if (node instanceof ExpressionStatement) {
                return "(" + name + ", [" + ((ExpressionStatement) node).getExpression() + "])";
            }

            if (node instanceof VariableDeclarationStatement) {
                return "(" + name + ", [" + ((VariableDeclarationStatement) node).fragments().get(0) + "])";
            }

            if (node instanceof VariableDeclarationExpression) {
                return "(" + name + ", [" + ((VariableDeclarationExpression) node).fragments().get(0) + "])";
            }

            return "(" + name + ", [" + node + "])";
        }


    }


    public String getName() {
        return name;
    }

    public ASTNode getNode() {
        return node;
    }
}
