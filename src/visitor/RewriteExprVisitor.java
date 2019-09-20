package visitor;

import Expression.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.HashMap;

public class RewriteExprVisitor extends ASTVisitor {
    private HashMap<String, Integer> exprToVarmap;
    private HashMap<ASTNode, KillSet> killMap;
    private ASTRewrite rewriter;


    public RewriteExprVisitor(HashMap<String, Integer> exprToVarmap,
                              HashMap<ASTNode, KillSet> killMap) {
        this.exprToVarmap = exprToVarmap;
        this.killMap = killMap;
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

        KillSet killedExprs = killMap.get(parent);


    }
}
