package visitor;

import Constraint.Constraint;
import Constraint.ExpressionLiteral;
import Constraint.SubsetOperator;
import Constraint.Term.*;
import ConstraintCreator.ConstraintTermFactory;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class MethodVisitor extends ASTVisitor {
    private ArrayList<Constraint> constraints;
    private ConstraintTermFactory variableFactory;
    private List<ExpressionLiteral> availableExpressions;

    public MethodVisitor(List<ExpressionLiteral> availableExpressions) {
        this.availableExpressions = availableExpressions;
        variableFactory = new ConstraintTermFactory();
        constraints  = new ArrayList<>();
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        ConstraintTerm exit = variableFactory.createExitLabel(node);
        ((ExitLabel) exit).setInitial(true);
        List<ASTNode> exitStmts = new ArrayList<>();
        exitStmts.add(node);
        BlockVisitor visitor = new BlockVisitor(node, exitStmts);
        node.accept(visitor);
        return false;
    }

    public class BlockVisitor extends ASTVisitor {
        private List<ASTNode> exitStmts;

        public BlockVisitor(ASTNode node, List<ASTNode> blockPrev) {
            exitStmts = new ArrayList<>();
            for (ASTNode p : blockPrev) {
                exitStmts.add(p);
            }
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
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            exitStmts.clear();
            exitStmts.add(node);

            String lhs = node.getLeftHandSide().toString();
            List<ExpressionLiteral>  exprToSubtract = getExpressionsInvolving(lhs);
            SetDifference setDifference = getSetDifference((EntryLabel) entry, exprToSubtract);

            if (node.getRightHandSide() instanceof InfixExpression) {
                ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(node.getRightHandSide());
                List<ExpressionLiteral> exprList = new ArrayList();
                exprList.add(newExpr);
                ConstraintTerm setUnion = getSetUnion(setDifference, exprList);
                variableFactory.setEntryLabel(node, setUnion);
                result.add(newSubsetConstraint(exit, setUnion));
            } else {
                variableFactory.setEntryLabel(node, setDifference);
                result.add(newSubsetConstraint(exit, setDifference));
            }

            constraints.addAll(result);
        }

        @Override
        public boolean visit(DoStatement node){
            ConstraintTerm entry = variableFactory.createEntryLabel(node);
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            List<Constraint> result = new ArrayList<Constraint>();

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            result.add(newSubsetConstraint(exit, entry));

            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            exitStmts.clear();
            exitStmts.add(node);

            Expression whileExpr = node.getExpression();

            ConstraintTerm whileExprEntry = variableFactory.createEntryLabel(whileExpr);
            ConstraintTerm whileExprExit = variableFactory.createExitLabel(whileExpr);


            exitStmts.add(whileExpr);

            Statement body = node.getBody();
            List<ASTNode> bodyExitStmts = new ArrayList<>();

            if(body instanceof Block && ((Block) body).statements().size() == 0) {
                result.add(newSubsetConstraint(whileExprEntry, exit));

            } else {
                BlockVisitor visitor = new BlockVisitor(body, exitStmts);
                body.accept(visitor);
                bodyExitStmts = visitor.getExitStmts();

                if (bodyExitStmts.equals((exitStmts))) {
                    bodyExitStmts.clear();
                }
            }


            exitStmts.clear();

            for (ASTNode stmt : bodyExitStmts) {
                exitStmts.add(stmt);
            }

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(whileExprEntry, prevExit));
                }
            }

            result.add(newSubsetConstraint(whileExprExit, whileExprEntry));

            exitStmts.clear();
            exitStmts.add(whileExpr);

            constraints.addAll(result);

            return false;
        }

        @Override
        public boolean visit(EnhancedForStatement node) {
            ConstraintTerm entry = variableFactory.createEntryLabel(node);
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            List<Constraint> result = new ArrayList<Constraint>();

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            result.add(newSubsetConstraint(exit, entry));

            constraints.addAll(result);

            exitStmts.clear();
            exitStmts.add(node);

            Statement body = node.getBody();
            List<ASTNode> bodyExitStmts = new ArrayList<>();

            // could body ever be null?
            if(body != null ) {
                BlockVisitor visitor = new BlockVisitor(body, exitStmts);
                body.accept(visitor);
                bodyExitStmts = visitor.getExitStmts();

                if (bodyExitStmts.equals((exitStmts))) {
                    // empty while body
                    bodyExitStmts.clear();
                }
            }

            for (ASTNode stmt : bodyExitStmts) {
                exitStmts.add(stmt);
            }

            return false;
        }


        @Override
        public boolean visit(ForStatement node){

            // (0) generate constraints and set prev for loop
            ConstraintTerm entry = variableFactory.createEntryLabel(node);
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            List<Constraint> result = new ArrayList<Constraint>();

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            result.add(newSubsetConstraint(exit, entry));

            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            exitStmts.clear();
            exitStmts.add(node);

            // (1) generate constraints and set prev for initialization
            Expression init = (Expression) node.initializers().get(0);
            ConstraintTerm initEntry = variableFactory.createEntryLabel(init);
            ConstraintTerm initExit = variableFactory.createExitLabel(init);

            for (ASTNode stmt : exitStmts) {
                ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(initEntry, prevExit));
            }

            result.add(newSubsetConstraint(initExit, initEntry));


            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            exitStmts.clear();
            exitStmts.add(init);

            // (2) generate constraints and set prev for condition
            // SAVE prev, this will be what we propagate at the end
            Expression cond = node.getExpression();
            ConstraintTerm condEntry = variableFactory.createEntryLabel(cond);
            ConstraintTerm condExit = variableFactory.createExitLabel(cond);

            if (cond instanceof InfixExpression) {
                if (((InfixExpression) cond).getLeftOperand() instanceof InfixExpression) {
                    ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(
                            ((InfixExpression) cond).getLeftOperand());
                    List<ExpressionLiteral> exprList = new ArrayList<ExpressionLiteral>();
                    exprList.add(newExpr);
                    ConstraintTerm setUnion = getSetUnion((EntryLabel) condEntry, exprList);
                    variableFactory.setEntryLabel(cond, setUnion);
                    // TODO: Can only be lhs or rhs? We are replacing init entry both, not adding to.
                } else if (((InfixExpression) cond).getRightOperand() instanceof InfixExpression) {
                    ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(
                            ((InfixExpression) cond).getRightOperand());
                    List<ExpressionLiteral> exprList = new ArrayList<ExpressionLiteral>();
                    exprList.add(newExpr);
                    ConstraintTerm setUnion = getSetUnion((EntryLabel) condEntry, exprList);
                    variableFactory.setEntryLabel(cond, setUnion);
                }
            }
            for (ASTNode stmt : exitStmts) {
                ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(condEntry, prevExit));
            }

            result.add(newSubsetConstraint(condExit, variableFactory.createEntryLabel(cond)));

            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            exitStmts.clear();
            exitStmts.add(cond);

            // (3) create body visitor. pass in and return prev
            Statement body = node.getBody();

            BlockVisitor visitor = new BlockVisitor(body, exitStmts);
            body.accept(visitor);

            List<ASTNode> bodyExitStmts = visitor.getExitStmts();
            exitStmts.clear();

            for (ASTNode stmt : bodyExitStmts) {
                exitStmts.add(stmt);
            }

            // (4) generate constraints and set prev for update
            // generate constraint for (condition subset update)

            Expression update = (Expression) node.updaters().get(0);

            ConstraintTerm updateEntry = variableFactory.createEntryLabel(update);
            ConstraintTerm updateExit = variableFactory.createExitLabel(update);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(updateEntry, prevExit));
                }
            }

            result.add(newSubsetConstraint(updateExit, updateEntry));

            result.add(newSubsetConstraint(variableFactory.createEntryLabel(cond), updateExit));

            // (5) Before returning, set prev to prev list of condition
            exitStmts.clear();
            exitStmts.add(cond);

            constraints.addAll(result);

            return false;
        }

        @Override
        public boolean visit(IfStatement node) {
            ConstraintTerm entry = variableFactory.createEntryLabel(node);
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            List<Constraint> result = new ArrayList<Constraint>();

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            result.add(newSubsetConstraint(exit, entry));

            constraints.addAll(result);

            exitStmts.clear();
            exitStmts.add(node);

            Statement thenBlock = node.getThenStatement();
            List<ASTNode> thenBlockExit = new ArrayList<>();

            Statement elseBlock = node.getElseStatement();
            List<ASTNode> elseBlockExit = new ArrayList<>();

            BlockVisitor visitor = new BlockVisitor(thenBlock, exitStmts);
            thenBlock.accept(visitor);

            thenBlockExit = visitor.getExitStmts();
            if (thenBlockExit.equals(exitStmts)) {
                // then block was empty, nothing new added or deleted
                exitStmts.clear();
            }

            if(elseBlock != null){
                BlockVisitor elseVisitor = new BlockVisitor(elseBlock, exitStmts);
                elseBlock.accept(elseVisitor);

                elseBlockExit = elseVisitor.getExitStmts();
                exitStmts.clear();
            }

            for (ASTNode stmt : thenBlockExit) {
                exitStmts.add(stmt);
            }

            for (ASTNode stmt : elseBlockExit) {
                exitStmts.add(stmt);
            }

            return false;
        }

        @Override
        public boolean visit(MethodInvocation node) {
            if (!(node.getParent() instanceof ExpressionStatement)) {
                return false;
            }

            variableFactory.createEntryLabel(node);
            variableFactory.createExitLabel(node);
            return true;
        }

        @Override
        public void endVisit(MethodInvocation node) {
            List<Constraint> result = new ArrayList<Constraint>();

            ConstraintTerm entry = variableFactory.createEntryLabel(node);
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            result.add(newSubsetConstraint(exit, entry));


            exitStmts.clear();
            exitStmts.add(node);

            constraints.addAll(result);
        }

        @Override
        public boolean visit(PostfixExpression node) {
            if (!(node.getParent() instanceof ExpressionStatement)) {
                return false;
            }
            variableFactory.createEntryLabel(node);
            variableFactory.createExitLabel(node);
            return true;
        }

        @Override
        public void endVisit(PostfixExpression node) {
            List<Constraint> result = new ArrayList<Constraint>();

            ConstraintTerm entry = variableFactory.createEntryLabel(node);
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            result.add(newSubsetConstraint(exit, entry));

            exitStmts.clear();
            exitStmts.add(node);

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
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));

                }
            }

            VariableDeclarationFragment fragment = ((List<VariableDeclarationFragment>) node.fragments()).get(0);
            String lhs = fragment.getName().getIdentifier();
            List<ExpressionLiteral>  exprToSubtract = getExpressionsInvolving(lhs);
            SetDifference setDifference = getSetDifference((EntryLabel)entry, exprToSubtract);

            if (fragment.getInitializer() instanceof InfixExpression) {
                ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(fragment.getInitializer());
                List<ExpressionLiteral> exprList = new ArrayList<ExpressionLiteral>();
                exprList.add(newExpr);
                ConstraintTerm setUnion = getSetUnion(setDifference, exprList);
                variableFactory.setEntryLabel(node, setUnion);
                result.add(newSubsetConstraint(exit, setUnion));
            } else {
                variableFactory.setEntryLabel(node, setDifference);
                result.add(newSubsetConstraint(exit, setDifference));
            }

            exitStmts.clear();
            exitStmts.add(node);

            constraints.addAll(result);
        }

        @Override
        public boolean visit(WhileStatement node) {

            ConstraintTerm entry = variableFactory.createEntryLabel(node);
            ConstraintTerm exit = variableFactory.createExitLabel(node);

            List<Constraint> result = new ArrayList<Constraint>();

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            result.add(newSubsetConstraint(exit, entry));
            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            exitStmts.clear();
            exitStmts.add(node);

            Statement body = node.getBody();
            List<ASTNode> bodyExit = new ArrayList<>();

            BlockVisitor visitor = new BlockVisitor(body, exitStmts);
            body.accept(visitor);

            bodyExit = visitor.getExitStmts();

            if (bodyExit.equals((exitStmts))) {
                // empty while body
                bodyExit.clear();
            }

            if(!bodyExit.isEmpty()){
                for (ASTNode stmt : bodyExit) {
                    ConstraintTerm prevStmtExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevStmtExit));
                }
            }

            for (ASTNode stmt : bodyExit) {
                exitStmts.add(stmt);
            }

            constraints.addAll(result);

//            System.out.println("exit statements for while " + node.getExpression() + ": " + exitStmts);
            return false;
        }


        public Constraint newSubsetConstraint(ConstraintTerm l, ConstraintTerm r) {
            return new Constraint(l, new SubsetOperator(), r);
        }

        public SetUnion getSetUnion(SetDifference t1, List<ExpressionLiteral> t2) {
            return new SetUnion(t1, t2);
        }

        public SetUnion getSetUnion(EntryLabel t1, List<ExpressionLiteral>  t2) {
            return new SetUnion(t1, t2);
        }

        public SetDifference getSetDifference(EntryLabel t1,  List<ExpressionLiteral>  t2) {
            return new SetDifference(t1, t2);
        }

        public List<ASTNode> getExitStmts() {
            return exitStmts;
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

        public ArrayList<Constraint> getConstraints() {
            return constraints;
        }
}
