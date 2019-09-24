package main;

import Expression.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import visitor.ExpressionVisitor;
import visitor.AEVisitor;
import visitor.RewriteExprVisitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Driver {

    public static void main(String[] args) throws IOException {

        File file = new File("./tests/While.java");
        String source = new String(Files.readAllBytes(file.toPath()));
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        AST ast = cu.getAST();

        ASTRewrite rewriter = ASTRewrite.create(ast);

        List<TypeDeclaration> types = cu.types();
        for (TypeDeclaration type : types) {
            for (MethodDeclaration methodDeclaration : type.getMethods()) {

                ExpressionVisitor exprVisitor = new ExpressionVisitor();
                methodDeclaration.accept(exprVisitor);
                List<ExpressionLiteral> exprList = exprVisitor.getNonlinearVarExpr();
                HashMap<String, Integer> exprToVarMap = exprVisitor.getExprMap();
                int varCount = exprVisitor.getVarCount();

                AEVisitor aeVisitor = new AEVisitor(exprList);
                methodDeclaration.accept(aeVisitor);
                HashMap<ASTNode, KillSet> killMap = aeVisitor.getKillMap();

                RewriteExprVisitor rewriteVisitor = new RewriteExprVisitor(exprToVarMap,
                        killMap, rewriter, ast);
                methodDeclaration.accept(rewriteVisitor);



            }
        }

        Document document = new Document(source);
        TextEdit edits = rewriter.rewriteAST(document, null);
        try {
            edits.apply(document);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        System.out.println(document.get());

/*        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(document.get());
        out.flush();
        out.close();*/
    }
}

