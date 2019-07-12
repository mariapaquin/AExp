package ConstraintCreator;

import Constraint.Constraint;
import Constraint.Operator.SubsetOperator;
import Constraint.Term.ConstraintTerm;
import Constraint.Term.DefinitionLiteral;
import Constraint.Term.SetDifference;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class ConstraintCreator {

    private ConstraintTermFactory variableFactory;

    public ConstraintCreator() {
        variableFactory = new ConstraintTermFactory();
    }

    public List<Constraint> create(Assignment node) {
        List<Constraint> result = new ArrayList<Constraint>();

        Expression lhs = node.getLeftHandSide();

        // if lhs isn't a simple name, it isn't a local variable
        if (lhs.getNodeType() != ASTNode.SIMPLE_NAME) {
            return result;
        }

        SimpleName name = (SimpleName) lhs;

        // need to check if variable refers to a field.
        // if it does, ignore it.

        // parent is expression statement (wrapper for assignments)
        ConstraintTerm assignEntry = variableFactory.createEntryLabel(node.getParent()); // RD_entry[v=e]
        DefinitionLiteral defWild = variableFactory.createDefinitionWildcard(name.getIdentifier());
        ConstraintTerm setDiff = getSetDiff(assignEntry, defWild);
        variableFactory.setEntryLabel(node.getParent(), setDiff);

        ConstraintTerm assignExit = variableFactory.createExitLabel(node.getParent()); // RD_exit[v=e]
        ConstraintTerm def = variableFactory.createDefinition(name.getIdentifier(), node.getParent()); // (v,v=e)

        result.add(newSubsetConstraint(def, assignExit));
        result.add(newSubsetConstraint(setDiff, assignExit));

        return result;
    }

    public List<Constraint> create(Block node) {
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

            result.add(newSubsetConstraint(s1_exit, s2_entry));
        }

        // check if the last statement needs to be in a subset relationship

        // last statement subset of if_exit
        if (node.getParent() instanceof IfStatement) {
            ConstraintTerm last = variableFactory.getExitLabel(statements.get(statements.size() - 1));
            ConstraintTerm ifExit = variableFactory.getExitLabel(node.getParent());

            result.add(newSubsetConstraint(last, ifExit));
        }

        // last statement subset of while_exit
        // last statement subset of while_entry
        if (node.getParent() instanceof WhileStatement) {
            ConstraintTerm last = variableFactory.getExitLabel(statements.get(statements.size() - 1));
            ConstraintTerm whileExit = variableFactory.getExitLabel(node.getParent());
            ConstraintTerm whileEntry = variableFactory.getEntryLabel(node.getParent());

            result.add(newSubsetConstraint(last, whileExit));
            result.add(newSubsetConstraint(last, whileEntry));
        }

        if (node.getParent() instanceof ForStatement) {
            ConstraintTerm last = variableFactory.getExitLabel(statements.get(statements.size() - 1));

            ConstraintTerm forExit = variableFactory.getExitLabel(node.getParent());
            ConstraintTerm forEntry = variableFactory.getEntryLabel(node.getParent());

            result.add(newSubsetConstraint(last, forExit));
            result.add(newSubsetConstraint(last, forEntry));
        }

        return result;
    }

    public List<Constraint> create(EnhancedForStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();
        return result;
    }

    public List<Constraint> create(ForStatement node) {
        // Simplification: exactly one init expr, a condition, exactly one update expr
        Statement body = node.getBody();
        Expression cond = node.getExpression();

        List<Expression> inits = node.initializers();
        List<Expression> updates = node.updaters();

        Expression init = (Expression) inits.get(0); // assume one init
        Expression update = (Expression) updates.get(0); // assume one update

        List<Constraint> result = new ArrayList();

        ConstraintTerm forEntry = variableFactory.createEntryLabel(node);
        ConstraintTerm forExit = variableFactory.createExitLabel(node);
        ConstraintTerm initEntry = variableFactory.createEntryLabel(init);
        ConstraintTerm initExit = variableFactory.createExitLabel(init);
        ConstraintTerm condEntry = variableFactory.createEntryLabel(cond);
        ConstraintTerm condExit = variableFactory.createExitLabel(cond);
        ConstraintTerm updateEntry = variableFactory.createEntryLabel(update);
        ConstraintTerm updateExit = variableFactory.createExitLabel(update);
        ConstraintTerm bodyEntry = variableFactory.createEntryLabel(body);
        ConstraintTerm bodyExit = variableFactory.createExitLabel(body);

        result.add(newSubsetConstraint(forEntry, initEntry));

        return result;
    }

    public List<Constraint> create(IfStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();

        Statement stmt = node.getThenStatement();

        ConstraintTerm entryFirstStmt = null;

        if (stmt instanceof Block) {
            List<Statement> blockStmts = ((Block) stmt).statements();
            if (blockStmts.size() > 0) {
                entryFirstStmt = variableFactory.createEntryLabel(blockStmts.get(0));
            }
        } else {
            entryFirstStmt = variableFactory.createEntryLabel(stmt);
        }

        ConstraintTerm ifEntry = variableFactory.createEntryLabel(node);
        ConstraintTerm ifExit = variableFactory.createExitLabel(node);

        result.add(newSubsetConstraint(ifEntry, ifExit));

        // exit of if statement needs to be subset of first statement of if body
        if (entryFirstStmt != null) {
            result.add(newSubsetConstraint(ifEntry, entryFirstStmt));
        }

        return result;
    }

    public List<Constraint> create(MethodInvocation node) {
        List<Constraint> result = new ArrayList<Constraint>();

        if (node.getParent() instanceof ExpressionStatement) {
            ConstraintTerm ifEntry = variableFactory.createEntryLabel(node.getParent());
            ConstraintTerm ifExit = variableFactory.createExitLabel(node.getParent());

            result.add(newSubsetConstraint(ifEntry, ifExit));
        }

        return result;
    }

    public List<Constraint> create(PostfixExpression node) {
        List<Constraint> result = new ArrayList<Constraint>();

        Expression operand = node.getOperand();
        // if lhs isn't a simple name, it isn't a local variable
        if (operand.getNodeType() != ASTNode.SIMPLE_NAME) {
            return result;
        }

        SimpleName name = (SimpleName) operand;

        // need to check if variable refers to a field.
        // if it does, ignore it.

        ConstraintTerm postfixEntry;
        ConstraintTerm setDiff;
        ConstraintTerm postfixExit;
        ConstraintTerm def;

        if (node.getParent() instanceof ExpressionStatement) {
            postfixEntry = variableFactory.createEntryLabel(node.getParent());
            DefinitionLiteral defWild = variableFactory.createDefinitionWildcard(name.getIdentifier());
            setDiff = getSetDiff(postfixEntry, defWild);
            variableFactory.setEntryLabel(node.getParent(), setDiff);

            postfixExit = variableFactory.createExitLabel(node.getParent());
            def = variableFactory.createDefinition(name.getIdentifier(), node.getParent());
        } else {
            postfixEntry = variableFactory.createEntryLabel(node);
            DefinitionLiteral defWild = variableFactory.createDefinitionWildcard(name.getIdentifier());
            setDiff = getSetDiff(postfixEntry, defWild);
            variableFactory.setEntryLabel(node, setDiff);

            postfixExit = variableFactory.createExitLabel(node);
            def = variableFactory.createDefinition(name.getIdentifier(), node);
        }

        result.add(newSubsetConstraint(def, postfixExit));
        result.add(newSubsetConstraint(setDiff, postfixExit));

        return result;
    }

    public List<Constraint> create(PrefixExpression node) {
        List<Constraint> result = new ArrayList<Constraint>();

        Expression operand = node.getOperand();
        // if lhs isn't a simple name, it isn't a local variable
        if (operand.getNodeType() != ASTNode.SIMPLE_NAME) {
            return result;
        }

        SimpleName name = (SimpleName) operand;

        // need to check if variable refers to a field.
        // if it does, ignore it.

        ConstraintTerm prefixEntry;
        ConstraintTerm setDiff;
        ConstraintTerm prefixExit;
        ConstraintTerm def;

        if (node.getParent() instanceof ExpressionStatement) {
            prefixEntry = variableFactory.createEntryLabel(node.getParent());
            DefinitionLiteral defWild = variableFactory.createDefinitionWildcard(name.getIdentifier());
            setDiff = getSetDiff(prefixEntry, defWild);
            variableFactory.setEntryLabel(node.getParent(), setDiff);

            prefixExit = variableFactory.createExitLabel(node.getParent());
            def = variableFactory.createDefinition(name.getIdentifier(), node.getParent());
        } else {
            prefixEntry = variableFactory.createEntryLabel(node);
            DefinitionLiteral defWild = variableFactory.createDefinitionWildcard(name.getIdentifier());
            setDiff = getSetDiff(prefixEntry, defWild);
            variableFactory.setEntryLabel(node, setDiff);

            prefixExit = variableFactory.createExitLabel(node);
            def = variableFactory.createDefinition(name.getIdentifier(), node);
        }

        result.add(newSubsetConstraint(def, prefixExit));
        result.add(newSubsetConstraint(setDiff, prefixExit));

        return result;
    }


    public List<Constraint> create(SwitchStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();
        return result;
    }

    public List<Constraint> create(WhileStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();

        Statement stmt = node.getBody();

        ConstraintTerm entryFirstStmt = null;

        if (stmt instanceof Block) {
            List<Statement> blockStmts = ((Block) stmt).statements();
            if (blockStmts.size() > 0) {
                entryFirstStmt = variableFactory.createEntryLabel(blockStmts.get(0));
            }
        } else {
            entryFirstStmt = variableFactory.createEntryLabel(stmt);
        }

        ConstraintTerm whileEntry = variableFactory.createEntryLabel(node);
        ConstraintTerm whileExit = variableFactory.createExitLabel(node);

        result.add(newSubsetConstraint(whileEntry, whileExit));

        // exit of if statement needs to be subset of first statement of if body
        if (entryFirstStmt != null) {
            result.add(newSubsetConstraint(whileEntry, entryFirstStmt));
        }

        return result;
    }


    public List<Constraint> create(VariableDeclarationExpression node) {
        List<Constraint> result = new ArrayList<Constraint>();
        return result;
    }

    public List<Constraint> create(VariableDeclarationStatement node) {
        List<Constraint> result = new ArrayList<Constraint>();

        List<VariableDeclarationFragment> fragments = node.fragments();

        for (VariableDeclarationFragment fragment : fragments) {

            Expression lhs = fragment.getName();
            Expression rhs = fragment.getInitializer();

            // if lhs isn't a simple name, it isn't a local variable
            if (lhs.getNodeType() != ASTNode.SIMPLE_NAME || rhs == null) {
                return result;
            }

            SimpleName name = (SimpleName) lhs;

            // need to check if variable refers to a field.
            // if it does, ignore it.

            ConstraintTerm assignEntry = variableFactory.createEntryLabel(node); // RD_entry[v=e]
            DefinitionLiteral defWild = variableFactory.createDefinitionWildcard(name.getIdentifier());
            ConstraintTerm setDiff = getSetDiff(assignEntry, defWild);
            variableFactory.setEntryLabel(node, setDiff);

            ConstraintTerm assignExit = variableFactory.createExitLabel(node); // RD_exit[v=e]
            ConstraintTerm def = variableFactory.createDefinition(name.getIdentifier(), node); // (v,v=e)

            result.add(newSubsetConstraint(def, assignExit));
            result.add(newSubsetConstraint(setDiff, assignExit));

        }
        return result;
    }


    public Constraint newSubsetConstraint(ConstraintTerm l, ConstraintTerm r) {
        return new Constraint(l, new SubsetOperator(), r);
    }

    public ConstraintTerm getSetDiff(ConstraintTerm t1, DefinitionLiteral t2) {
        return new SetDifference(t1, t2); // temporary
    }
}
