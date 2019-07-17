package Constraint.Term;

import org.eclipse.jdt.core.dom.ASTNode;
/**
 * A node in the Constraint graph.
 *
 *
 */
public class NodeLabel extends ConstraintTerm {
	protected ASTNode node;
	
	public NodeLabel(ASTNode node) {
		this.node = node;
	}

	public ASTNode getNode(){
		return node;
	}
}
