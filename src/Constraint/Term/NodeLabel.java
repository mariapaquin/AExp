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
	protected ASTNode node;
	protected boolean isInitial;

	public NodeLabel(ASTNode node) {
		super();
		this.node = node;
	}

	public void updateAE(List<ExpressionLiteral> expressions) {
		availableExpressions = expressions;
	}

	public void initializeAE(List<ExpressionLiteral> expressions){
		availableExpressions = expressions;
	}

	public List<ExpressionLiteral> getAvailableExpressions() {
		return availableExpressions;
	}

	public void setAvailableExpressions(List<ExpressionLiteral> availableExpressions) {
		this.availableExpressions = availableExpressions;
	}

	public boolean isInitial() {
		return isInitial;
	}

	public void setInitial(boolean initial) {
		isInitial = initial;
	}

	public ASTNode getNode(){
		return node;
	}
}
