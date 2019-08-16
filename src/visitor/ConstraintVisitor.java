package visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Constraint.Constraint;
import Constraint.Operator.SubsetOperator;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.ExpressionLiteral;
import Constraint.Term.SetDifference;
import ConstraintCreator.ConstraintTermFactory;
import org.eclipse.jdt.core.dom.*;

/**
 * Traverse the AST and generate subset constraints 
 * for each language construct. 
 *
 */
public class ConstraintVisitor extends ASTVisitor {
	private HashSet constraints = new HashSet();
	private ConstraintTermFactory variableFactory;

	public ConstraintVisitor() {
		variableFactory = new ConstraintTermFactory();
	}

	@Override
	public boolean visit(Assignment node) {
		variableFactory.createEntryLabel(node);
		variableFactory.createExitLabel(node);
		return true;
	}

	@Override
	public void endVisit(Assignment node) {
		// TODO: Before subtracting the expression from AE,
		// (1) Check that the lhs is a symbolic variable,
		// (2) and that rhs is a constant.

		List<Constraint> result = new ArrayList<Constraint>();

		ConstraintTerm entry = variableFactory.createEntryLabel(node);
		ExpressionLiteral expr = variableFactory.createExpressionLiteral(node.getRightHandSide());
		ConstraintTerm setDiff = getSetDiff(entry, expr);
		variableFactory.setEntryLabel(node, setDiff);

		ConstraintTerm exit = variableFactory.createExitLabel(node);

		result.add(newSubsetConstraint(exit, setDiff));

		constraints.addAll(result);
	}

//	@Override
//	public boolean visit(EnhancedForStatement node) {
//		return true;
//	}
//
//	@Override
//	public void endVisit(EnhancedForStatement node) {
//		List<Constraint> result = new ArrayList<Constraint>();
//		constraints.addAll(result);
//	}
//
//	@Override
//	public boolean visit(ForStatement node) {
//		return true;
//	}
//
//	@Override
//	public void endVisit(ForStatement node) {
//		List<Constraint> result = new ArrayList();
//		constraints.addAll(result);
//	}



	@Override
	public boolean visit(MethodInvocation node) {
		if (node.getParent() instanceof ExpressionStatement) {
			variableFactory.createEntryLabel(node.getParent());
			variableFactory.createExitLabel(node.getParent());
		}
		return true;
	}

	@Override
	public void endVisit(MethodInvocation node) {
		List<Constraint> result = new ArrayList<Constraint>();

		if (node.getParent() instanceof ExpressionStatement) {

			ConstraintTerm entry = variableFactory.createEntryLabel(node);
			ConstraintTerm exit = variableFactory.createExitLabel(node);

			result.add(newSubsetConstraint(exit, entry));
		}
		constraints.addAll(result);
	}


//	@Override
//	public void endVisit(SwitchStatement node) {
//		List<Constraint> result = new ArrayList<Constraint>();
//		constraints.addAll(result);
//	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		variableFactory.createEntryLabel(node);
		variableFactory.createExitLabel(node);
		return true;
	}

	@Override
	public void endVisit(VariableDeclarationStatement node) {
		List<Constraint> result = new ArrayList<Constraint>();

		ConstraintTerm entry = variableFactory.createEntryLabel(node);
		ConstraintTerm exit = variableFactory.createExitLabel(node);

		result.add(newSubsetConstraint(exit, entry));


		constraints.addAll(result);
	}

//	@Override
//	public void endVisit(WhileStatement node) {
//		List<Constraint> result = new ArrayList<Constraint>();
//		constraints.addAll(result);
//	}

	public HashSet getConstraints() {
		return constraints;
	}


	public Constraint newSubsetConstraint(ConstraintTerm l, ConstraintTerm r) {
		return new Constraint(l, new SubsetOperator(), r);
	}

	public ConstraintTerm getSetDiff(ConstraintTerm t1, ExpressionLiteral t2) {
		return new SetDifference(t1, t2); // temporary
	}
}