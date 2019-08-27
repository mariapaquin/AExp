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

            List<ExpressionLiteral> exprList = new ArrayList<>();

            if (node.getRightHandSide() instanceof InfixExpression) {
                ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(node.getRightHandSide());
                exprList.add(newExpr);
            }

            SetUnion setUnion = getSetUnion((EntryLabel) entry, exprList);
            variableFactory.setEntryLabel(node, setUnion);

            String lhs = node.getLeftHandSide().toString();
            List<ExpressionLiteral>  exprToSubtract = getExpressionsInvolving(lhs);
            SetDifference setDifference = getSetDifference(setUnion, exprToSubtract);

            variableFactory.setEntryLabel(node, setDifference);
            result.add(newSubsetConstraint(exit, setDifference));

            constraints.addAll(result);
        }

        @Override
        public boolean visit(DoStatement node){
            //*****************//
            //   do statement  //
            //*****************//
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

            // so the constraints are added in the right order
            constraints.addAll(result);
            result = new ArrayList<Constraint>();


            //***************//
            //   condition   //
            //***************//
            Expression cond = node.getExpression();

            ConstraintTerm condEntry = variableFactory.createEntryLabel(cond);
            ConstraintTerm condExit = variableFactory.createExitLabel(cond);

            exitStmts.add(cond);

            //************//
            //    body    //
            //************//
            Statement body = node.getBody();
            List<ASTNode> bodyExitStmts = new ArrayList<>();

            if(body instanceof Block && ((Block) body).statements().size() == 0) {
                result.add(newSubsetConstraint(condEntry, exit));

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
                    result.add(newSubsetConstraint(condEntry, prevExit));
                }
            }

            result.add(newSubsetConstraint(condExit, condEntry));

            exitStmts.clear();
            exitStmts.add(cond);

            constraints.addAll(result);

            return false;
        }

        @Override
        public boolean visit(EnhancedForStatement node) {
            //*******************//
            // foreach statement //
            //*******************//
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

            exitStmts.clear();
            exitStmts.add(node);

            // so the constraints are added in the right order
            constraints.addAll(result);

            //************//
            //    body    //
            //************//
            Statement body = node.getBody();

            BlockVisitor visitor = new BlockVisitor(body, exitStmts);
            body.accept(visitor);

            List<ASTNode>  bodyExitStmts = visitor.getExitStmts();

            // empty while body
            if (bodyExitStmts.equals((exitStmts))) {
                bodyExitStmts.clear();
            }

            for (ASTNode stmt : bodyExitStmts) {
                exitStmts.add(stmt);
            }

            return false;
        }


        @Override
        public boolean visit(ForStatement node){
            //*******************//
            //   for statement   //
            //*******************//
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

            exitStmts.clear();
            exitStmts.add(node);

            // so the constraints are added in the right order
            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            //*******************//
            //   initialization  //
            //*******************//
            Expression init = (Expression) node.initializers().get(0);

            ConstraintTerm initEntry = variableFactory.createEntryLabel(init);
            ConstraintTerm initExit = variableFactory.createExitLabel(init);

            for (ASTNode stmt : exitStmts) {
                ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(initEntry, prevExit));
            }

            result.add(newSubsetConstraint(initExit, initEntry));

            exitStmts.clear();
            exitStmts.add(init);

            // so the constraints are added in the right order
            constraints.addAll(result);
            result = new ArrayList<Constraint>();


            //****************//
            //   condition    //
            //****************//
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

            exitStmts.clear();
            exitStmts.add(cond);

            // so the constraints are added in the right order
            constraints.addAll(result);
            result = new ArrayList<Constraint>();


            //***********//
            //   body    //
            //***********//
            Statement body = node.getBody();

            BlockVisitor visitor = new BlockVisitor(body, exitStmts);
            body.accept(visitor);

            List<ASTNode> bodyExitStmts = visitor.getExitStmts();
            exitStmts.clear();

            for (ASTNode stmt : bodyExitStmts) {
                exitStmts.add(stmt);
            }

            //*************//
            //   update    //
            //*************//
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

            // condition will flow to the statement after the for loop
            exitStmts.clear();
            exitStmts.add(cond);

            constraints.addAll(result);

            return false;
        }

        @Override
        public boolean visit(IfStatement node) {
            List<Constraint> result = new ArrayList<Constraint>();

            //**************//
            // if statement //
            //**************//
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

            //**************//
            //  condition   //
            //**************//
            Expression cond = node.getExpression();

            ConstraintTerm condEntry = variableFactory.createEntryLabel(cond);
            ConstraintTerm condExit = variableFactory.createExitLabel(cond);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(condEntry, prevExit));
                }
            }

            result.add(newSubsetConstraint(condExit, condEntry));

            exitStmts.clear();
            exitStmts.add(cond);

            // so the constraints get added in order.
            constraints.addAll(result);

            //**************//
            //    body      //
            //**************//
            Statement thenBlock = node.getThenStatement();
            Statement elseBlock = node.getElseStatement();

            BlockVisitor visitor = new BlockVisitor(thenBlock, exitStmts);
            thenBlock.accept(visitor);

            List<ASTNode> thenBlockExit = visitor.getExitStmts();
            List<ASTNode> elseBlockExit = new ArrayList<>();

            // then block was empty, nothing new added or deleted
            if (thenBlockExit.equals(exitStmts)) {
                thenBlockExit.clear();
            }

            if(elseBlock != null){
                BlockVisitor elseVisitor = new BlockVisitor(elseBlock, exitStmts);
                elseBlock.accept(elseVisitor);

                elseBlockExit = elseVisitor.getExitStmts();

                // control flow will go to 'then' or 'else' - we can remove the conditional
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

            List<ExpressionLiteral> exprList = new ArrayList<ExpressionLiteral>();

            if (fragment.getInitializer() instanceof InfixExpression) {
                ExpressionLiteral newExpr = variableFactory.createExpressionLiteral(fragment.getInitializer());
                exprList.add(newExpr);
            }

            SetUnion setUnion = getSetUnion((EntryLabel) entry, exprList);

            String lhs = fragment.getName().getIdentifier();
            List<ExpressionLiteral>  exprToSubtract = getExpressionsInvolving(lhs);
            SetDifference setDifference = getSetDifference(setUnion, exprToSubtract);

            variableFactory.setEntryLabel(node, setDifference);
            result.add(newSubsetConstraint(exit, setDifference));

            exitStmts.clear();
            exitStmts.add(node);

            constraints.addAll(result);
        }

        @Override
        public boolean visit(WhileStatement node) {
            List<Constraint> result = new ArrayList<Constraint>();

            //*******************//
            //  while statement  //
            //*******************//
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

            //***************//
            //  condition    //
            //***************//
            Expression cond = node.getExpression();

            ConstraintTerm condEntry = variableFactory.createEntryLabel(cond);
            ConstraintTerm condExit = variableFactory.createExitLabel(cond);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    ConstraintTerm prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(condEntry, prevExit));
                }
            }

            result.add(newSubsetConstraint(condExit, condEntry));

            exitStmts.clear();
            exitStmts.add(cond);

            // so the constraints get added in order.
            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            //**************//
            //    body      //
            //**************//
            Statement body = node.getBody();

            BlockVisitor visitor = new BlockVisitor(body, exitStmts);
            body.accept(visitor);

            List<ASTNode> bodyExit = visitor.getExitStmts();

            // empty while body
            if (bodyExit.equals((exitStmts))) {
                bodyExit.clear();
            }

            for (ASTNode stmt : bodyExit) {
                ConstraintTerm exitStmt = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(condEntry, exitStmt));
            }

            constraints.addAll(result);

            return false;
        }


        public Constraint newSubsetConstraint(ConstraintTerm l, ConstraintTerm r) {
            return new Constraint(l, new SubsetOperator(), r);
        }

        public SetUnion getSetUnion(EntryLabel t1, List<ExpressionLiteral>  t2) {
            return new SetUnion(t1, t2);
        }

        public SetDifference getSetDifference(SetUnion t1,  List<ExpressionLiteral>  t2) {
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
