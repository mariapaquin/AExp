package visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Constraint.Constraint;
import Constraint.Operator.SubsetOperator;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.ExpressionLiteral;
import Constraint.Term.SetDifference;
import Constraint.Term.SetUnion;
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
	private List<ExpressionLiteral> availableExpressions;
	private List<Statement> prev;

	public ConstraintVisitor(List<ExpressionLiteral> availableExpressions) {
		this.availableExpressions = availableExpressions;
		variableFactory = new ConstraintTermFactory();
		prev = new ArrayList<>();
	}

	@Override
	public boolean visit(Assignment node) {
		variableFactory.createEntryLabel(node);
		variableFactory.createExitLabel(node);
		return true;
	}

	@Override
	public void endVisit(Assignment node) {
		List<Constraint> result = new ArrayList<Constraint>();

		ConstraintTerm entry = variableFactory.createEntryLabel(node);
		String lhs = node.getLeftHandSide().toString();
		List<ExpressionLiteral>  exprToSubtract = getExpressionsInvolving(lhs);
		SetDifference setDifference = getSetDifference(entry, exprToSubtract);

		ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(node.getRightHandSide());

		ConstraintTerm setUnion = getSetUnion(setDifference, newExpr);

		variableFactory.setEntryLabel(node, setUnion);

		ConstraintTerm exit = variableFactory.createExitLabel(node);

		result.add(newSubsetConstraint(exit, setUnion));

		constraints.addAll(result);
	}



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
		VariableDeclarationFragment fragment = ((List<VariableDeclarationFragment>) node.fragments()).get(0);
		String lhs = fragment.getName().getIdentifier();
		List<ExpressionLiteral>  exprToSubtract = getExpressionsInvolving(lhs);
		SetDifference setDifference = getSetDifference(entry, exprToSubtract);

		ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(fragment.getInitializer());

		ConstraintTerm setUnion = getSetUnion(setDifference, newExpr);

		variableFactory.setEntryLabel(node, setUnion);

		ConstraintTerm exit = variableFactory.createExitLabel(node);

		result.add(newSubsetConstraint(exit, setUnion));

		constraints.addAll(result);
	}

	private List<ExpressionLiteral> getExpressionsInvolving(String lhs) {
		List<ExpressionLiteral> exprsInvolvingLhs = new ArrayList<>();
		for (ExpressionLiteral expr : availableExpressions) {
			if (expr.involves(lhs)) {
				exprsInvolvingLhs.add(expr);
			}
		}
		return exprsInvolvingLhs;
	}


	public HashSet getConstraints() {
		return constraints;
	}


	public Constraint newSubsetConstraint(ConstraintTerm l, ConstraintTerm r) {
		return new Constraint(l, new SubsetOperator(), r);
	}

	public SetUnion getSetUnion(SetDifference t1, ExpressionLiteral t2) {
		return new SetUnion(t1, t2); // temporary
	}

	public SetDifference getSetDifference(ConstraintTerm t1,  List<ExpressionLiteral>  t2) {
		return new SetDifference(t1, t2); // temporary
	}
}