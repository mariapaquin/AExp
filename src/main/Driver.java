package main;

import Expression.*;

import org.eclipse.jdt.core.dom.*;
import visitor.ExpressionVisitor;
import visitor.AEVisitor;
import visitor.RewriteExprVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Driver {

    public static void main(String[] args) throws IOException {

        File file = new File("./tests/AE/StatementSequence.java");
        String source = new String(Files.readAllBytes(file.toPath()));
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        // TODO: Need to do this separately for each method

        ExpressionVisitor exprVisitor = new ExpressionVisitor();
        cu.accept(exprVisitor);
        List<ExpressionLiteral> exprList = exprVisitor.getNonlinearVarExpr();
        HashMap<String, Integer> exprToVarMap = exprVisitor.getExprMap();

        AEVisitor aeVisitor = new AEVisitor(exprList);
        cu.accept(aeVisitor);
        HashMap<ASTNode, KillSet> killMap = aeVisitor.getKillMap();

        RewriteExprVisitor rewriteVisitor = new RewriteExprVisitor(exprToVarMap, killMap);
        cu.accept(rewriteVisitor);
    }
}

