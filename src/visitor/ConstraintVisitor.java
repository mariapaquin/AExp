package visitor;

import java.util.HashSet;

import ConstraintCreator.ConstraintCreator;
import org.eclipse.jdt.core.dom.*;

/**
 * Traverse the AST and generate subset constraints 
 * for each language construct. 
 *
 */
public class ConstraintVisitor extends ASTVisitor {
	private ConstraintCreator creator;


	private HashSet constraints = new HashSet();

	public ConstraintVisitor(ConstraintCreator creator) {
		this.creator = creator;
	}

	@Override
	public  void endVisit(Block node) {
		// adding Constraint RD_exit[S] \sub RD_entry[S']
		//
		// for S' following S in the block
		// (after we have already generated the terms RD_exit[S] and RD_entry[S']
		constraints.addAll(creator.createConstraints(node));
	}

	@Override
	public void endVisit(EnhancedForStatement node) {
		constraints.addAll(creator.createConstraints(node));
	}

	@Override
	public void endVisit(ForStatement node) {
		constraints.addAll(creator.createConstraints(node));
	}

	@Override
	public void endVisit(IfStatement node) {
		constraints.addAll(creator.createConstraints(node));
	}

	@Override
	public boolean visit(InfixExpression node) {
		creator.createTerms(node);
		return true;
	}


	@Override
	public boolean visit(MethodInvocation node) {
		creator.createTerms(node);
		return true;
	}

	@Override
	public void endVisit(MethodInvocation node) {
		constraints.addAll(creator.createConstraints(node));
	}

	@Override
	public boolean visit(PostfixExpression node) {
		creator.createTerms(node);
		return true;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		creator.createTerms(node);
		return true;
	}


	@Override
	public void endVisit(SwitchStatement node) {
		constraints.addAll(creator.createConstraints(node));
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		constraints.addAll(creator.createConstraints(node));
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		creator.createTerms(node);
		return true;
	}

	@Override
	public void endVisit(VariableDeclarationStatement node) {
		constraints.addAll(creator.createConstraints(node));
	}

	@Override
	public void endVisit(WhileStatement node) {
		constraints.addAll(creator.createConstraints(node));
	}

	public HashSet getConstraints() {
		return constraints;
	}
}