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
	public boolean visit(Assignment node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public  void endVisit(Block node) {
		// adding Constraint RD_exit[S] \sub RD_entry[S']
		//
		// for S' following S in the block
		// (after we have already generated the terms RD_exit[S] and RD_entry[S']
		constraints.addAll(creator.create(node));
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(ForStatement node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(IfStatement node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(PostfixExpression node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		constraints.addAll(creator.create(node));
		return true;
	}



	@Override
	public boolean visit(SwitchStatement node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	@Override
	public boolean visit(WhileStatement node) {
		constraints.addAll(creator.create(node));
		return true;
	}

	public HashSet getConstraints() {
		return constraints;
	}
}