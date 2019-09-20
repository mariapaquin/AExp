package visitor;

import Expression.KillSet;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.HashMap;

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
