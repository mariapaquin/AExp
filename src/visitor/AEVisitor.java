package visitor;

import Constraint.Constraint;
import Constraint.ExpressionLiteral;
import Constraint.SubsetOperator;
import Constraint.Term.*;
import ConstraintCreator.ConstraintTermFactory;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This visitor creates a map from statement nodes to entry labels
 * (contain the information of what expressions are available
 * upon entry to the statement).
 */
public class AEVisitor extends ASTVisitor {
    private ArrayList<Constraint> constraints;
    private ConstraintTermFactory variableFactory;
    private List<ExpressionLiteral> availableExpressions;
    private int symbVarCount;

    public AEVisitor() {
        availableExpressions = new ArrayList<>();
        variableFactory = new ConstraintTermFactory();
        constraints  = new ArrayList<>();
        symbVarCount = 1;
    }


    @Override
    public boolean visit(MethodDeclaration node) {
        ExpressionVisitor exprVisitor = new ExpressionVisitor();
        node.accept(exprVisitor);

        variableFactory.setExprList(availableExpressions);

        NodeLabel exit = variableFactory.createExitLabel(node);

        List<ASTNode> exitStmts = new ArrayList<>();
        exitStmts.add(node);
        BlockVisitor blockVisitor = new BlockVisitor(exitStmts);
        node.accept(blockVisitor);
        return false;
    }

    public class ExpressionVisitor extends ASTVisitor {

        @Override
        public boolean visit(InfixExpression node) {
            Expression lhs = node.getLeftOperand();
            Expression rhs = node.getRightOperand();
            InfixExpression.Operator op = node.getOperator();

            if (!(lhs instanceof SimpleName) || !(rhs instanceof SimpleName)) {
                return true;
            }

            if((op != InfixExpression.Operator.TIMES) &&
                    (op != InfixExpression.Operator.DIVIDE) &&
                    (op != InfixExpression.Operator.REMAINDER)){
                return true;
            }

            ExpressionLiteral expressionLiteral = new ExpressionLiteral(node, ("S" + symbVarCount++));

            List<String> varsUsed = getVarsUsed(node);
            expressionLiteral.setVarsUsed(varsUsed);

            boolean existingExpr = false;
            for (ExpressionLiteral e : availableExpressions) {
                if (e.getNode().toString().equals(node.toString())) {
                    existingExpr = true;
                }
            }
            if(!existingExpr) {
                availableExpressions.add(expressionLiteral);
            }
            return true;
        }

        private List<String> getVarsUsed(InfixExpression node) {
            List<String> vars = new ArrayList<>();
            ASTVisitor visitor= new ASTVisitor() {
                @Override
                public boolean visit(SimpleName name) {
                    vars.add(name.getIdentifier());
                    return false;
                }
            };
            node.accept(visitor);
            return vars;
        }
    }

    public List<ExpressionLiteral> getAvailableExpressions() {
        return availableExpressions;
    }


    public class BlockVisitor extends ASTVisitor {
        private List<ASTNode> exitStmts;

        public BlockVisitor(List<ASTNode> blockPrev) {
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

            ASTNode parent = node.getParent();

            NodeLabel entry = variableFactory.createEntryLabel(parent);
            NodeLabel exit = variableFactory.createExitLabel(parent);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            exitStmts.clear();
            exitStmts.add(parent);

            //*******************//
            //   possible infix  //
            //*******************//
 /*            ExpressionVisitor expressionVisitor = new ExpressionVisitor();

           node.getRightHandSide().accept(expressionVisitor);
            List<ExpressionLiteral> exprList = expressionVisitor.getExprList();

            SetUnion setUnion = getSetUnion((EntryLabel) entry, exprList);
            variableFactory.setEntryLabel(parent, setUnion);*/

            String lhs = node.getLeftHandSide().toString();
            List<ExpressionLiteral>  exprToReassign = getExpressionsInvolving(lhs);

            for (ExpressionLiteral e : exprToReassign) {
//                System.out.println(symbVarCount);
                exit.reassignExpr(e, ("S" + symbVarCount++));
            }
//            SetDifference setDifference = getSetDifference(setUnion, exprToSubtract);
//
//            variableFactory.setEntryLabel(parent, setDifference);
//            result.add(newSubsetConstraint(exit, setDifference));

            result.add(newSubsetConstraint(exit, entry));


            constraints.addAll(result);
        }

        @Override
        public boolean visit(DoStatement node){
            //*****************//
            //   do statement  //
            //*****************//
            List<Constraint> result = new ArrayList<Constraint>();

            NodeLabel entry = variableFactory.createEntryLabel(node);
            NodeLabel exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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

            NodeLabel condEntry = variableFactory.createEntryLabel(cond);
            NodeLabel condExit = variableFactory.createExitLabel(cond);

            //*******************//
            //   possible infix  //
            //*******************//
/*            ExpressionVisitor infixVisitor = new ExpressionVisitor();

            cond.accept(infixVisitor);
            List<ExpressionLiteral> exprList = infixVisitor.getExprList();

            NodeLabel setUnion = getSetUnion((EntryLabel) condEntry, exprList);
            variableFactory.setEntryLabel(cond, setUnion);

            exitStmts.add(cond);*/

            //************//
            //    body    //
            //************//
            Statement body = node.getBody();
            List<ASTNode> bodyExitStmts = new ArrayList<>();

            if(body instanceof Block && ((Block) body).statements().size() == 0) {
                result.add(newSubsetConstraint(variableFactory.createEntryLabel(cond), exit));

            } else {
                BlockVisitor visitor = new BlockVisitor(exitStmts);
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
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(variableFactory.createEntryLabel(cond), prevExit));
                }
            }

            result.add(newSubsetConstraint(condExit, variableFactory.createEntryLabel(cond)));

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
            NodeLabel entry = variableFactory.createEntryLabel(node);
            NodeLabel exit = variableFactory.createExitLabel(node);

            List<Constraint> result = new ArrayList<Constraint>();

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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

            BlockVisitor visitor = new BlockVisitor(exitStmts);
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
        public boolean visit(ExpressionStatement node){
            return true;
        }


        @Override
        public boolean visit(ForStatement node){
            //*******************//
            //   for statement   //
            //*******************//
            NodeLabel entry = variableFactory.createEntryLabel(node);
            NodeLabel exit = variableFactory.createExitLabel(node);

            List<Constraint> result = new ArrayList<Constraint>();

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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

            NodeLabel initEntry = variableFactory.createEntryLabel(init);
            NodeLabel initExit = variableFactory.createExitLabel(init);

            for (ASTNode stmt : exitStmts) {
                NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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

            NodeLabel condEntry = variableFactory.createEntryLabel(cond);
            NodeLabel condExit = variableFactory.createExitLabel(cond);

            //*******************//
            //   possible infix  //
            //*******************//
/*            ExpressionVisitor infixVisitor = new ExpressionVisitor();

            cond.accept(infixVisitor);
            List<ExpressionLiteral> exprList = infixVisitor.getExprList();

            NodeLabel setUnion = getSetUnion((EntryLabel) condEntry, exprList);
            variableFactory.setEntryLabel(cond, setUnion);*/

            for (ASTNode stmt : exitStmts) {
                NodeLabel prevExit = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(variableFactory.createEntryLabel(cond), prevExit));
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

            BlockVisitor visitor = new BlockVisitor(exitStmts);
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

            NodeLabel updateEntry = variableFactory.createEntryLabel(update);
            NodeLabel updateExit = variableFactory.createExitLabel(update);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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
            NodeLabel entry = variableFactory.createEntryLabel(node);
            NodeLabel exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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

            NodeLabel condEntry = variableFactory.createEntryLabel(cond);
            NodeLabel condExit = variableFactory.createExitLabel(cond);

            //*******************//
            //   possible infix  //
            //*******************//
/*            ExpressionVisitor infixVisitor = new ExpressionVisitor();

            cond.accept(infixVisitor);
            List<ExpressionLiteral> exprList = infixVisitor.getExprList();

            NodeLabel setUnion = getSetUnion((EntryLabel) condEntry, exprList);
            variableFactory.setEntryLabel(cond, setUnion);*/

            for (ASTNode stmt : exitStmts) {
                NodeLabel prevExit = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(variableFactory.createEntryLabel(cond), prevExit));
            }

            result.add(newSubsetConstraint(condExit, variableFactory.createEntryLabel(cond)));

            exitStmts.clear();
            exitStmts.add(cond);

            // so the constraints get added in order.
            constraints.addAll(result);

            //**************//
            //    body      //
            //**************//
            Statement thenBlock = node.getThenStatement();
            Statement elseBlock = node.getElseStatement();

            BlockVisitor visitor = new BlockVisitor(exitStmts);
            thenBlock.accept(visitor);

            List<ASTNode> thenBlockExit = visitor.getExitStmts();
            List<ASTNode> elseBlockExit = new ArrayList<>();

            // then block was empty, nothing new added or deleted
            if (thenBlockExit.equals(exitStmts)) {
                thenBlockExit.clear();
            }

            if(elseBlock != null){
                BlockVisitor elseVisitor = new BlockVisitor(exitStmts);
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

            ASTNode parent = node.getParent();

            variableFactory.createEntryLabel(parent);
            variableFactory.createExitLabel(parent);
            return true;
        }

        @Override
        public void endVisit(MethodInvocation node) {
            List<Constraint> result = new ArrayList<Constraint>();

            ASTNode parent = node.getParent();

            NodeLabel entry = variableFactory.createEntryLabel(parent);
            NodeLabel exit = variableFactory.createExitLabel(parent);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));
                }
            }

            //*******************//
            //   possible infix  //
            //*******************//
 /*           ExpressionVisitor infixVisitor = new ExpressionVisitor();

            node.accept(infixVisitor);
            List<ExpressionLiteral> exprList = infixVisitor.getExprList();

            NodeLabel setUnion = getSetUnion((EntryLabel) entry, exprList);
            variableFactory.setEntryLabel(parent, setUnion);*/

            result.add(newSubsetConstraint(exit, variableFactory.createEntryLabel(parent)));

            exitStmts.clear();
            exitStmts.add(parent);

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

            NodeLabel entry = variableFactory.createEntryLabel(node);
            NodeLabel exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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

            NodeLabel entry = variableFactory.createEntryLabel(node);
            NodeLabel exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(entry, prevExit));

                }
            }

            VariableDeclarationFragment fragment = ((List<VariableDeclarationFragment>)
                    node.fragments()).get(0);

            //*******************//
            //   possible infix  //
            //*******************//
/*            ExpressionVisitor infixVisitor = new ExpressionVisitor();

            fragment.getInitializer().accept(infixVisitor);
            List<ExpressionLiteral> exprList = infixVisitor.getExprList();

            SetUnion setUnion = getSetUnion((EntryLabel) entry, exprList);

            String lhs = fragment.getName().getIdentifier();
            List<ExpressionLiteral>  exprToSubtract = getExpressionsInvolving(lhs);
            SetDifference setDifference = getSetDifference(setUnion, exprToSubtract);

            variableFactory.setEntryLabel(node, setDifference);
            result.add(newSubsetConstraint(exit, setDifference));*/


            String lhs = fragment.getName().getIdentifier();
            List<ExpressionLiteral>  exprToReassign = getExpressionsInvolving(lhs);

            for (ExpressionLiteral e : exprToReassign) {
                exit.reassignExpr(e, ("S" + symbVarCount++));
            }

            result.add(newSubsetConstraint(exit, entry));

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
            NodeLabel entry = variableFactory.createEntryLabel(node);
            NodeLabel exit = variableFactory.createExitLabel(node);

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
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

            NodeLabel condEntry = variableFactory.createEntryLabel(cond);
            NodeLabel condExit = variableFactory.createExitLabel(cond);

            //*******************//
            //   possible infix  //
            //*******************//
/*            ExpressionVisitor infixVisitor = new ExpressionVisitor();

            cond.accept(infixVisitor);
            List<ExpressionLiteral> exprList = infixVisitor.getExprList();

            NodeLabel setUnion = getSetUnion((EntryLabel) condEntry, exprList);
            variableFactory.setEntryLabel(cond, setUnion);*/

            if(!exitStmts.isEmpty()){
                for (ASTNode stmt : exitStmts) {
                    NodeLabel prevExit = variableFactory.createExitLabel(stmt);
                    result.add(newSubsetConstraint(variableFactory.createEntryLabel(cond), prevExit));
                }
            }

            result.add(newSubsetConstraint(condExit, variableFactory.createEntryLabel(cond)));

            exitStmts.clear();
            exitStmts.add(cond);

            // so the constraints get added in order.
            constraints.addAll(result);
            result = new ArrayList<Constraint>();

            //**************//
            //    body      //
            //**************//
            Statement body = node.getBody();

            BlockVisitor visitor = new BlockVisitor(exitStmts);
            body.accept(visitor);

            List<ASTNode> bodyExit = visitor.getExitStmts();

            // empty while body
            if (bodyExit.equals((exitStmts))) {
                bodyExit.clear();
            }

            for (ASTNode stmt : bodyExit) {
                NodeLabel exitStmt = variableFactory.createExitLabel(stmt);
                result.add(newSubsetConstraint(condEntry, exitStmt));
            }

            constraints.addAll(result);

            return false;
        }



        public Constraint newSubsetConstraint(NodeLabel l, NodeLabel r) {
            return new Constraint(l, new SubsetOperator(), r);
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