package Constraint.Term;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * RD_entry[n]
 *
 */
public class EntryLabel extends NodeLabel {
	private List<ExpressionLiteral> expressionList;

	public EntryLabel(ASTNode node) {
		super(node);
		expressionList = new ArrayList<>();
	}

	public void addExpression(ExpressionLiteral e) {
		expressionList.add(e);
	}


	public String toString() {
		String nodeExpr = node.toString();

		if (node instanceof VariableDeclarationStatement) {
			nodeExpr = ((VariableDeclarationStatement) node).fragments().get(0).toString();
		}

		if(node instanceof ExpressionStatement){
			nodeExpr = ((ExpressionStatement) node).getExpression().toString();
		}

		if (node instanceof IfStatement) {
			nodeExpr = ((IfStatement) node).getExpression().toString();
		}

		if (node instanceof WhileStatement) {
			nodeExpr = ((WhileStatement) node).getExpression().toString();
		}

		if (node instanceof ForStatement) {
			nodeExpr = "for( " + ((ForStatement) node).getExpression() .toString() + ")";

		}

		String ret = "entry[" + nodeExpr + "]";
		if (!expressionList.isEmpty()) {
			for (int i = 0; i < expressionList.size(); i++) {
				ret += " U " + expressionList.get(i);
			}
		}
		return ret;
	}
}
