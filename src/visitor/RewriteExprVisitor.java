package visitor;

import Expression.KillSet;
import Expression.ExpressionLiteral;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.HashMap;
import java.util.List;

public class RewriteExprVisitor extends ASTVisitor {
    private HashMap<String, Integer> exprToVarmap;
    private HashMap<ASTNode, KillSet> killMap;
    private AST ast;
    private ASTRewrite rewriter;


    public RewriteExprVisitor(HashMap<String, Integer> exprToVarmap,
                              HashMap<ASTNode, KillSet> killMap,
                              ASTRewrite rewriter, AST ast) {
        this.exprToVarmap = exprToVarmap;
        this.killMap = killMap;
        this.rewriter = rewriter;
        this.ast = ast;
    }

    @Override
    public void endVisit(InfixExpression node) {
        Integer symbVarNum = exprToVarmap.get(node.toString());
        if (symbVarNum == null) {
            return;
        }

        String name = "x" + symbVarNum;

        SimpleName exprSymbVar = ast.newSimpleName(name);
        rewriter.replace(node, exprSymbVar, null);
    }



    @Override
    public void endVisit(MethodDeclaration node) {
        for (String expr : exprToVarmap.keySet()) {
            int symbVarNum = exprToVarmap.get(expr);
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

            addVariableStatementDeclaration(varDeclaration, node);
        }
    }

    @Override
    public boolean visit(Assignment node) {
        if (!(node.getParent() instanceof ExpressionStatement)) {
            return true;
        }

        ExpressionStatement parent = (ExpressionStatement) node.getParent();
        ASTNode block = parent.getParent();

        while (!(block instanceof Block)) {
            block = block.getParent();
        }

        KillSet ks = killMap.get(node);
        if (ks == null) {
            return true;
        }

        List<ExpressionLiteral> exprs = ks.getExprs();

        for (ExpressionLiteral expr : exprs) {
            int symbVarNum = exprToVarmap.get(expr.toString());
            String name = "x" + symbVarNum;

            MethodInvocation randMethodInvocation = ast.newMethodInvocation();
            randMethodInvocation.setExpression(ast.newSimpleName("Debug"));
            randMethodInvocation.setName(ast.newSimpleName("makeSymbolicInteger"));
            StringLiteral str = ast.newStringLiteral();
            str.setLiteralValue(name);
            randMethodInvocation.arguments().add(str);

            Assignment assignment = ast.newAssignment();
            assignment.setLeftHandSide(ast.newSimpleName(name));
            assignment.setRightHandSide(randMethodInvocation);
            ExpressionStatement stmt = ast.newExpressionStatement(assignment);

            addAssignmentStatement(parent, stmt, (Block) block);
        }

        return true;
    }

    @Override
    public boolean visit(PostfixExpression node) {

        if (!(node.getParent() instanceof ExpressionStatement)) {
            return true;
        }

        ExpressionStatement parent = (ExpressionStatement) node.getParent();
        ASTNode block = parent.getParent();

        while (!(block instanceof Block)) {
            block = block.getParent();
        }

        KillSet ks = killMap.get(node);
        if (ks == null) {
            return true;
        }

        List<ExpressionLiteral> exprs = ks.getExprs();

        for (ExpressionLiteral expr : exprs) {
            int symbVarNum = exprToVarmap.get(expr.toString());
            String name = "x" + symbVarNum;

            MethodInvocation randMethodInvocation = ast.newMethodInvocation();
            randMethodInvocation.setExpression(ast.newSimpleName("Debug"));
            randMethodInvocation.setName(ast.newSimpleName("makeSymbolicInteger"));
            StringLiteral str = ast.newStringLiteral();
            str.setLiteralValue(name);
            randMethodInvocation.arguments().add(str);

            Assignment assignment = ast.newAssignment();
            assignment.setLeftHandSide(ast.newSimpleName(name));
            assignment.setRightHandSide(randMethodInvocation);
            ExpressionStatement stmt = ast.newExpressionStatement(assignment);

            addAssignmentStatement(parent, stmt, (Block) block);
        }

        return true;
    }

    @Override
    public boolean visit(PrefixExpression node) {

        if (!(node.getParent() instanceof ExpressionStatement)) {
            return true;
        }

        ExpressionStatement parent = (ExpressionStatement) node.getParent();
        ASTNode block = parent.getParent();

        while (!(block instanceof Block)) {
            block = block.getParent();
        }

        KillSet ks = killMap.get(node);
        if (ks == null) {
            return true;
        }

        List<ExpressionLiteral> exprs = ks.getExprs();

        for (ExpressionLiteral expr : exprs) {
            int symbVarNum = exprToVarmap.get(expr.toString());
            String name = "x" + symbVarNum;

            MethodInvocation randMethodInvocation = ast.newMethodInvocation();
            randMethodInvocation.setExpression(ast.newSimpleName("Debug"));
            randMethodInvocation.setName(ast.newSimpleName("makeSymbolicInteger"));
            StringLiteral str = ast.newStringLiteral();
            str.setLiteralValue(name);
            randMethodInvocation.arguments().add(str);

            Assignment assignment = ast.newAssignment();
            assignment.setLeftHandSide(ast.newSimpleName(name));
            assignment.setRightHandSide(randMethodInvocation);
            ExpressionStatement stmt = ast.newExpressionStatement(assignment);

            addAssignmentStatement(parent, stmt, (Block) block);
        }

        return true;
    }


    private void addAssignmentStatement(Statement parent, ExpressionStatement stmt,
                                        Block block) {

        if (block != null) { // not abstract
            ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
            listRewrite.insertAfter(stmt, parent,null);
        }
    }


    private void addVariableStatementDeclaration(VariableDeclarationStatement varDeclaration,
                                                 MethodDeclaration methodDeclaration) {

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
