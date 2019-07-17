package Constraint.Term;

import org.eclipse.jdt.core.dom.*;

/**
 * RD_exit[n]
 *
 */
public class ExitLabel extends NodeLabel {

	public ExitLabel(ASTNode node) {
		super(node);
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
			nodeExpr = ((ForStatement) node).getExpression() .toString();
		}

		return "exit[" + nodeExpr + "]";
	}
	
}
