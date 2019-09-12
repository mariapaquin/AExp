package visitor;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.HashMap;

public class RewriteExprVisitor extends ASTVisitor {
    private HashMap<ASTNode, HashMap<ExpressionLiteral, Integer>> entryMap;
    private ASTRewrite rewriter;
    private AST ast;

    public RewriteExprVisitor(HashMap<ASTNode, HashMap<ExpressionLiteral, Integer>> entryMap) {
        this.entryMap = entryMap;
    }

    @Override
    public boolean visit(CompilationUnit node) {
        ast = node.getAST();
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

        // TODO : check lhs and rhs for variables.

        ASTNode parent = node.getParent();

        while (!(parent instanceof Statement)) {
            parent = parent.getParent();
        }

        HashMap<ExpressionLiteral, Integer> nodeMap = entryMap.get(parent);

        for (ExpressionLiteral exprLiteral : nodeMap.keySet()) {
            Expression expr = exprLiteral.getNode();

            if (expr.toString().equals(node.toString())) {

                int symbVarNum = nodeMap.get(exprLiteral);
                String name = "x" + symbVarNum;

                MethodInvocation randMethodInvocation = ast.newMethodInvocation();
                randMethodInvocation.setExpression(ast.newSimpleName("Debug"));
                randMethodInvocation.setName(ast.newSimpleName("makeSymbolicInteger"));
                StringLiteral str = ast.newStringLiteral();
                str.setLiteralValue(name);
                randMethodInvocation.arguments().add(str);

                VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
                fragment.setName(ast.newSimpleName(name));
                fragment.setInitializer(randMethodInvocation);

                VariableDeclarationStatement varDeclaration = ast.newVariableDeclarationStatement(fragment);
                varDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
                // replace infix node with new expression (simple name).
                System.out.println(varDeclaration);

                SimpleName exprSymbVar = ast.newSimpleName(name);
                rewriter.replace(node, exprSymbVar, null);

            }
        }

    }

    public ASTRewrite getRewriter() {
        return rewriter;
    }
}
