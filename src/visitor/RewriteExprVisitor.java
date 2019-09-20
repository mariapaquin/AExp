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
    private int varCount;
    private ASTRewrite rewriter;
    private AST ast;


    public RewriteExprVisitor(int varCount, HashMap<String, Integer> exprToVarmap,
                              HashMap<ASTNode, KillSet> killMap) {
        this.varCount = varCount;
        this.exprToVarmap = exprToVarmap;
        this.killMap = killMap;
    }

    @Override
    public boolean visit(CompilationUnit node) {
        ast = node.getAST();
        rewriter = ASTRewrite.create(ast);
        return true;
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
        ASTNode parent = node.getParent();

        while (!(parent instanceof ExpressionStatement)) {
            parent = parent.getParent();
        }

        ASTNode methodDec = parent.getParent();

        while (!(methodDec instanceof MethodDeclaration)) {
            methodDec = methodDec.getParent();
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

            addAssignmentStatement((ExpressionStatement) parent, stmt, (MethodDeclaration) methodDec);
        }

        return true;
    }

    private void addAssignmentStatement(ExpressionStatement parent, ExpressionStatement stmt,
                                        MethodDeclaration methodDeclaration) {
        Block block = methodDeclaration.getBody();

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
