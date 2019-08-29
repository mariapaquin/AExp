package visitor;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.HashMap;
import java.util.List;

public class RewriteExprVisitor extends ASTVisitor {
    private List<ExpressionLiteral> availableExpressions;
    private HashMap<String, String> exprToSymbNameMap;
    private HashMap<ASTNode, List<String>> entryMap;
    private ASTRewrite rewriter;


    public RewriteExprVisitor(List<ExpressionLiteral> availableExpressions,
                              HashMap<ASTNode, List<String>> entryMap) {
        this.availableExpressions = availableExpressions;
        this.entryMap = entryMap;
        exprToSymbNameMap = new HashMap<>();
        initializeMap();
    }

    private void initializeMap() {
        for (ExpressionLiteral e : availableExpressions) {
            String expr = e.getNode().toString();
            exprToSymbNameMap.put(expr, "");
        }
    }

    @Override
    public boolean visit(CompilationUnit node) {
        AST ast = node.getAST();
        rewriter = ASTRewrite.create(ast);
        return true;
    }

    @Override
    public void endVisit(InfixExpression node) {
        InfixExpression.Operator op = node.getOperator();

        if((op != InfixExpression.Operator.TIMES) &&
                (op != InfixExpression.Operator.DIVIDE) &&
                (op != InfixExpression.Operator.REMAINDER)){
            return;
        }

        // check lhs and rhs for variables. if one does not contain
        // variables, return

        ASTNode parent = node.getParent();

        while (!(parent instanceof Statement)) {
            parent = parent.getParent();
        }

        List<String> expressions = entryMap.get(parent);

        if (expressions.contains(node.toString())) {
            // the expression is available.
            // get the variable name from the exprToSymbNameMap
            // replace node with a variable
        } else{
            // the expressions is not available.
            // create a new variable declaration statement
            // right before the parent statement
            // put the variable declaration name in the exprToSymbNameMap
            // replace the node with the variable
        }

    }
}
