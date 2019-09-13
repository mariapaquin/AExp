package visitor;

import Constraint.ExpressionLiteral;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RewriteExprVisitor extends ASTVisitor {
    private HashMap<ASTNode, HashMap<ExpressionLiteral, Integer>> entryMap;
    private ASTRewrite rewriter;
    private AST ast;
    private MethodDeclaration methodDeclaration;
    private List<String> symbVarsUsed;

    public RewriteExprVisitor(HashMap<ASTNode, HashMap<ExpressionLiteral, Integer>> entryMap) {
        this.entryMap = entryMap;
        symbVarsUsed = new ArrayList<>();
    }

    @Override
    public boolean visit(CompilationUnit node) {
        ast = node.getAST();
        rewriter = ASTRewrite.create(ast);
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        methodDeclaration = node;
        return true;
    }

    @Override
    public void endVisit(MethodDeclaration node) {
        for (int i = symbVarsUsed.size() - 1; i >= 0; i--) {
            String name = symbVarsUsed.get(i);

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

            addVariableStatementDeclaration(varDeclaration);
        }
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

                if (!symbVarsUsed.contains(name)) {
                    symbVarsUsed.add(name);
                }

                SimpleName exprSymbVar = ast.newSimpleName(name);
                rewriter.replace(node, exprSymbVar, null);

            }
        }

    }

    private void addVariableStatementDeclaration(VariableDeclarationStatement varDeclaration) {

        Block block = methodDeclaration.getBody();

        if (block != null) { // not abstract
            ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
            listRewrite.insertFirst(varDeclaration, null);
        }
    }

    public ASTRewrite getRewriter() {
        return rewriter;
    }
}
