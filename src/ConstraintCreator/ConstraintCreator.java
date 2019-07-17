package ConstraintCreator;

import Constraint.Constraint;
import Constraint.Operator.SubsetOperator;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.EntryLabel;
import Constraint.Term.EntryUnionAE;
import Constraint.Term.ExpressionLiteral;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class ConstraintCreator {

    private ConstraintTermFactory variableFactory;

    public ConstraintCreator() {
        variableFactory = new ConstraintTermFactory();
    }

    public List<Constraint> createConstraints(Block node) {
        List<Constraint> result = new ArrayList<Constraint>();

        List<Statement> statements = node.statements();

        if (statements.isEmpty()) {
            return result;
        }

        for (int i = 0; i < statements.size() - 1; i++) {
            Statement s1 = statements.get(i);
            Statement s2 = statements.get(i + 1);

            ConstraintTerm s1_exit = variableFactory.getExitLabel(s1);
            ConstraintTerm s2_entry = variableFactory.getEntryLabel(s2);

            result.add(newSubsetConstraint(s2_entry, s1_exit));
        }

        // check if the last statement needs to be in a subset relationship

        // last statement subset of if_exit
//        if (node.getParent() instanceof IfStatement) {
//            ConstraintTerm last = variableFactory.getExitLabel(statements.get(statements.size() - 1));
//
//            ConstraintTerm ifExit = variableFactory.getExitLabel(node.getParent());
//
//            result.add(newSubsetConstraint(ifExit, last));
//        }

        // last statement subset of while_exit
        // last statement subset of while_entry
//        if (node.getParent() instanceof WhileStatement) {
//            ConstraintTerm last = variableFactory.getExitLabel(statements.get(statements.size() - 1));
//
//            ConstraintTerm whileExit = variableFactory.getExitLabel(node.getParent());
//            ConstraintTerm whileEntry = variableFactory.getEntryLabel(node.getParent());
//
//            result.add(newSubsetConstraint(whileExit, last));
//            result.add(newSubsetConstraint(whileEntry, last));
//        }
//
//        if (node.getParent() instanceof ForStatement) {
//            ConstraintTerm last = variableFactory.getExitLabel(statements.get(statements.size() - 1));
//
//            ConstraintTerm forExit = variableFactory.getExitLabel(node.getParent());
//            ConstraintTerm forEntry = variableFactory.getEntryLabel(node.getParent());
//
//            result.add(newSubsetConstraint(forExit, last));
//            result.add(newSubsetConstraint(forEntry, last));
//        }

        return result;
    }

    public List<Constraint> createConstraints(EnhancedForStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();
        return result;
    }

    public List<Constraint> createConstraints(ForStatement node) {
        List<Constraint> result = new ArrayList();

//        // Simplification: exactly one init expr, a condition, exactly one update expr
//        Statement body = node.getBody();
//        Expression cond = node.getExpression();
//
//        List<Expression> inits = node.initializers();
//        List<Expression> updates = node.updaters();
//
//        Expression init = (Expression) inits.get(0); // assume one init
//        Expression update = (Expression) updates.get(0); // assume one update
//
//        ConstraintTerm forEntry = variableFactory.createEntryLabel(node);
//        ConstraintTerm forExit = variableFactory.createExitLabel(node);
//        ConstraintTerm initEntry = variableFactory.createEntryLabel(init);
//        ConstraintTerm initExit = variableFactory.createExitLabel(init);
//        ConstraintTerm condEntry = variableFactory.createEntryLabel(cond);
//        ConstraintTerm condExit = variableFactory.createExitLabel(cond);
//        ConstraintTerm updateEntry = variableFactory.createEntryLabel(update);
//        ConstraintTerm updateExit = variableFactory.createExitLabel(update);
//        ConstraintTerm bodyEntry = variableFactory.createEntryLabel(body);
//        ConstraintTerm bodyExit = variableFactory.createExitLabel(body);
//
//        result.add(newSubsetConstraint(forEntry, initEntry));

        return result;
    }

    public List<Constraint> createConstraints(IfStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();

//        Statement stmt = node.getThenStatement();
//
//        ConstraintTerm entryFirstStmt = null;
//
//        if (stmt instanceof Block) {
//            List<Statement> blockStmts = ((Block) stmt).statements();
//            if (blockStmts.size() > 0) {
//                entryFirstStmt = variableFactory.createEntryLabel(blockStmts.get(0));
//            }
//        } else {
//            entryFirstStmt = variableFactory.createEntryLabel(stmt);
//        }
//
//        ConstraintTerm ifEntry = variableFactory.createEntryLabel(node);
//        ConstraintTerm ifExit = variableFactory.createExitLabel(node);
//
//        result.add(newSubsetConstraint(ifEntry, ifExit));
//
//        // exit of if statement needs to be subset of first statement of if body
//        if (entryFirstStmt != null) {
//            result.add(newSubsetConstraint(ifEntry, entryFirstStmt));
//        }

        return result;
    }

    public void createTerms(InfixExpression node) {
        ExpressionLiteral expr = variableFactory.createExpressionLiteral(node);

        ASTNode parentStmt = node.getParent();

        while(!(parentStmt instanceof Statement)){
            parentStmt = parentStmt.getParent();
        }

        EntryUnionAE parentStmtEntryUnion = variableFactory.createEntryUnionLabel(parentStmt);
        parentStmtEntryUnion.addExpression(expr);

    }

    public void createTerms(MethodInvocation node) {
        if (node.getParent() instanceof ExpressionStatement) {
            ConstraintTerm entry = variableFactory.createEntryLabel(node.getParent());
            ConstraintTerm exit = variableFactory.createExitLabel(node.getParent());
        }
    }

    public List<Constraint> createConstraints(MethodInvocation node) {
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
        return result;
    }

    public void createTerms(PostfixExpression node) {

    }

    public void createTerms(PrefixExpression node) {
    }


    public List<Constraint> createConstraints(SwitchStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();
        return result;
    }

    public List<Constraint> createConstraints(WhileStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();
//
//        Statement stmt = node.getBody();
//
//        ConstraintTerm entryFirstStmt = null;
//
//        if (stmt instanceof Block) {
//            List<Statement> blockStmts = ((Block) stmt).statements();
//            if (blockStmts.size() > 0) {
//                entryFirstStmt = variableFactory.createEntryLabel(blockStmts.get(0));
//            }
//        } else {
//            entryFirstStmt = variableFactory.createEntryLabel(stmt);
//        }
//
//        ConstraintTerm whileEntry = variableFactory.createEntryLabel(node);
//        ConstraintTerm whileExit = variableFactory.createExitLabel(node);
//
//        result.add(newSubsetConstraint(whileEntry, whileExit));
//
//        // exit of if statement needs to be subset of first statement of if body
//        if (entryFirstStmt != null) {
//            result.add(newSubsetConstraint(whileEntry, entryFirstStmt));
//        }

        return result;
    }


    public List<Constraint> createConstraints(VariableDeclarationExpression node) {
        List<Constraint> result = new ArrayList<Constraint>();
        return result;
    }

    public void createTerms(VariableDeclarationStatement node) {
        variableFactory.createEntryLabel(node);
        variableFactory.createExitLabel(node);
    }

    public List<Constraint> createConstraints(VariableDeclarationStatement node) {
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

        return result;
    }



    public Constraint newSubsetConstraint(ConstraintTerm l, ConstraintTerm r) {
        return new Constraint(l, new SubsetOperator(), r);
    }

}
