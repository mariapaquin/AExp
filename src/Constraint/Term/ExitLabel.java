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
		if(node instanceof ExpressionStatement){
			return "RD@exit[" + ((ExpressionStatement) node).getExpression() + "]";
		}

		if(node instanceof VariableDeclarationStatement){
			return "RD@exit[" + ((VariableDeclarationStatement) node).fragments().get(0) + "]";
		}

		if (node instanceof IfStatement) {
			return "RD@exit[if(" + ((IfStatement) node).getExpression() + ")]";
		}

		if (node instanceof WhileStatement) {
			return "RD@exit[while(" + ((WhileStatement) node).getExpression() + ")]";
		}

		if (node instanceof ForStatement) {
			return "RD@exit[for(" + ((ForStatement) node).getExpression() + ")]";
		}

		return "RD@exit[" + node + "]";
	}
	
}
