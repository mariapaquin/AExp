package Constraint.Term;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

/**
 * A node in the Constraint graph.
 *
 *
 */
public class NodeLabel extends ConstraintTerm {
//	protected boolean isInitial;

	public NodeLabel(ASTNode node) {
		this.node = node;
	}

	public List<ExpressionLiteral> getAvailableExpressions() {
		return availableExpressions;
	}

	public void setAvailableExpressions(List<ExpressionLiteral> expressions) {
		availableExpressions = expressions;
	}

//	public boolean isInitial() {
//		return isInitial;
//	}
//
//	public void setInitial(boolean initial) {
//		isInitial = initial;
//	}

	public ASTNode getNode(){
		return node;
	}
}
