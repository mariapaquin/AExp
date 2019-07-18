package visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Constraint.Constraint;
import Constraint.Operator.SubsetOperator;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.EntryUnionAE;
import Constraint.Term.ExpressionLiteral;
import ConstraintCreator.ConstraintCreator;
import ConstraintCreator.ConstraintTermFactory;
import org.eclipse.jdt.core.dom.*;

/**
 * Traverse the AST and generate subset constraints 
 * for each language construct. 
 *
 */
public class ConstraintVisitor extends ASTVisitor {
	private ConstraintCreator creator;
	private HashSet constraints = new HashSet();
	private ConstraintTermFactory variableFactory;
	private List<ASTNode> prev;

	public ConstraintVisitor(ConstraintCreator creator) {
		this.creator = creator;
		variableFactory = new ConstraintTermFactory();
		prev = new ArrayList<>();
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
	public boolean visit(IfStatement node) {
		List<Constraint> result = new ArrayList<Constraint>();

		ConstraintTerm entry = variableFactory.createEntryLabel(node);
		ConstraintTerm exit = variableFactory.createExitLabel(node);

		if (!prev.isEmpty()) {
			for (ASTNode p : prev) {
				result.add(newSubsetConstraint(entry, variableFactory.createExitLabel(p)));
			}
		}

		constraints.addAll(result);

		prev.clear();
		prev.add(node);

		return true;
	}

	@Override
	public void endVisit(IfStatement node) {
		List<Constraint> result = new ArrayList<Constraint>();

		ConstraintTerm entryUnionAE = variableFactory.createEntryUnionLabel(node);
		ConstraintTerm entry = variableFactory.createEntryLabel(node);
		ConstraintTerm exit = variableFactory.createExitLabel(node);

		if (entryUnionAE != null) {
			// the statement created at least one expression
			result.add(newSubsetConstraint(exit, entryUnionAE));
		} else {
			result.add(newSubsetConstraint(exit, entry));
		}

		constraints.addAll(result);

		Statement thenStmt = node.getThenStatement();
		// TODO
		// Statement elseStmt = node.getElseStatement();

//		prev.clear();
//
//		if(thenStmt instanceof Block){
//			List<Statement> blockStmts = ((Block) thenStmt).statements();
//			if(!blockStmts.isEmpty()){
//				prev.add(blockStmts.get(blockStmts.size() - 1));
//			}
//		} else {
//			prev.add(thenStmt);
//		}

		prev.add(node);

	}

	@Override
	public boolean visit(InfixExpression node) {
		ExpressionLiteral expr = variableFactory.createExpressionLiteral(node);

		ASTNode parentStmt = node.getParent();

		while(!(parentStmt instanceof Statement)){
			parentStmt = parentStmt.getParent();
		}

		EntryUnionAE parentStmtEntryUnion = variableFactory.createEntryUnionLabel(parentStmt);
		parentStmtEntryUnion.addExpression(expr);
		return true;
	}


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

			ConstraintTerm entryUnionAE = variableFactory.createEntryUnionLabel(node);
			ConstraintTerm entry = variableFactory.createEntryLabel(node);
			ConstraintTerm exit = variableFactory.createExitLabel(node);

			if (entryUnionAE != null) {
				// the statement created at least one expression
				result.add(newSubsetConstraint(exit, entryUnionAE));
			} else {
				result.add(newSubsetConstraint(exit, entry));
			}
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

		ConstraintTerm entryUnionAE = variableFactory.createEntryUnionLabel(node);
		ConstraintTerm entry = variableFactory.createEntryLabel(node);
		ConstraintTerm exit = variableFactory.createExitLabel(node);

		if (entryUnionAE != null) {
			// the statement created at least one expression
			result.add(newSubsetConstraint(exit, entryUnionAE));
		} else {
			result.add(newSubsetConstraint(exit, entry));
		}

		if (!prev.isEmpty()) {
			for (ASTNode p : prev) {
				result.add(newSubsetConstraint(entry, variableFactory.createExitLabel(p)));
			}
		}

		prev.clear();
		prev.add(node);

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
}