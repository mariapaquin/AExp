package Constraint.Term;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class NodeLabel extends ConstraintTerm {

	public NodeLabel(ASTNode node) {
		this.node = node;
	}

	@Override
	public List<ExpressionLiteral> getAvailableExpressions() {
		return availableExpressions;
	}

	@Override
	public List<String> getAvailableExpressionsAsString() {
		List<String> ret = new ArrayList<>();
		for (ExpressionLiteral e : availableExpressions) {
			ret.add(e.getNode().toString());
		}
		return ret;
	}

	@Override
	public void setAvailableExpressions(List<ExpressionLiteral> expressions) {
		availableExpressions = expressions;
	}

	@Override
	public ASTNode getNode(){
		return node;
	}
}
