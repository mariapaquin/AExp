package visitor;

import Constraint.Constraint;
import Constraint.Operator.SubsetOperator;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.ExpressionLiteral;
import Constraint.Term.SetDifference;
import Constraint.Term.SetUnion;
import ConstraintCreator.ConstraintTermFactory;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BlockVisitor extends ASTVisitor {

    private List<ASTNode> prev;
    private HashSet constraints;
    private ConstraintTermFactory variableFactory;
    private List<ExpressionLiteral> availableExpressions;
    private boolean innerBlock;

    public BlockVisitor(List<ASTNode> blockPrev, HashSet constraints,
                        ConstraintTermFactory variableFactory, List<ExpressionLiteral> availableExpressions) {
        prev = new ArrayList<>();
        for (ASTNode p : blockPrev) {
            prev.add(p);
        }
        this.constraints = constraints;
        this.variableFactory = variableFactory;
        this.availableExpressions = availableExpressions;
        innerBlock = false;
    }

    @Override
    public boolean visit(Assignment node) {
        variableFactory.createEntryLabel(node);
        variableFactory.createExitLabel(node);
        innerBlock = true;
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

        if(!prev.isEmpty()){
            for (ASTNode stmt : prev) {
                ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(entry, prevExit));

            }
        }

        prev.clear();
        prev.add(node);

        constraints.addAll(result);
    }

    @Override
    public boolean visit(Block node) {

        if (node.getParent() instanceof MethodDeclaration || !innerBlock) {
            return true;
        }

        BlockVisitor visitor = new BlockVisitor(prev, constraints, variableFactory, availableExpressions);
        node.accept(visitor);

        List<ASTNode> blockPrev = visitor.getPrev();
        prev.clear();

        for (ASTNode p : blockPrev) {
            prev.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        variableFactory.createEntryLabel(node);
        variableFactory.createExitLabel(node);
        innerBlock = true;
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

        if(!prev.isEmpty()){
            for (ASTNode stmt : prev) {
                ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(entry, prevExit));

            }
        }

        prev.clear();
        prev.add(node);

        constraints.addAll(result);
    }

    public Constraint newSubsetConstraint(ConstraintTerm l, ConstraintTerm r) {
        return new Constraint(l, new SubsetOperator(), r);
    }

    public SetUnion getSetUnion(SetDifference t1, ExpressionLiteral t2) {
        return new SetUnion(t1, t2);
    }

    public SetDifference getSetDifference(ConstraintTerm t1,  List<ExpressionLiteral>  t2) {
        return new SetDifference(t1, t2);
    }

    public List<ASTNode> getPrev() {
        return prev;
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

}
