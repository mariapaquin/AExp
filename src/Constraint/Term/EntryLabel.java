package Constraint.Term;

import org.eclipse.jdt.core.dom.*;

/**
 * RD_entry[n]
 *
 */
public class EntryLabel extends NodeLabel {

	public EntryLabel(ASTNode node) {
		super(node);
	}
	
	public String toString() {
		if(node instanceof ExpressionStatement){
			return "RD@entry[" + ((ExpressionStatement) node).getExpression() + "]";
		}

		if(node instanceof VariableDeclarationStatement){
			return "RD@entry[" + ((VariableDeclarationStatement) node).fragments().get(0) + "]";
		}

		if (node instanceof IfStatement) {
			return "RD@entry[if(" + ((IfStatement) node).getExpression() + ")]";
		}

		if (node instanceof WhileStatement) {
			return "RD@entry[while(" + ((WhileStatement) node).getExpression() + ")]";
		}

		if (node instanceof ForStatement) {
			return "RD@entry[for(" + ((ForStatement) node).getExpression() + ")]";
		}


		return "RD@entry[" + node + "]";
	}
}
