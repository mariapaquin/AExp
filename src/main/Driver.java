package main;

import Constraint.Constraint;
import Constraint.ExpressionLiteral;
import Solving.ConstraintSolver;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
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

        File file = new File("./tests/If.java");
        String source = new String(Files.readAllBytes(file.toPath()));
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        // TODO: Need to do this separately for each method

        AEVisitor aeVisitor = new AEVisitor();
        cu.accept(aeVisitor);
        List<ExpressionLiteral> ae = aeVisitor.getAvailableExpressions();
        int symbVarCount = aeVisitor.getSymbVarCount();


//        System.out.println(" ------------- \n| Constraints |\n ------------- ");
        ArrayList<Constraint> constraints = aeVisitor.getConstraints();

/*        int i = 0;
        for (Constraint constraint : constraints) {
            System.out.println(++i + ") " + constraint);
            System.out.println(constraint.getLhs().getExprMap() + " >= " + constraint.getRhs().getExprMap());
        }*/

//        System.out.println(" ------------  \n| Constraint |\n| Solutions  |\n ------------  ");
        ConstraintSolver solver = new ConstraintSolver(constraints, symbVarCount);

        solver.buildConstraintGraph();

        solver.processWorkList();

       solver.buildEntryMap();
        HashMap<ASTNode, HashMap<ExpressionLiteral, Integer>> entryMap = solver.getEntryMap();
        Set set = entryMap.entrySet();
        Iterator iterator = set.iterator();

/*        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            System.out.println("Key : " + mapEntry.getKey() + "Value : " + mapEntry.getValue() + "\n");
        }*/


        RewriteExprVisitor rewriteVisitor = new RewriteExprVisitor(entryMap);
        cu.accept(rewriteVisitor);

        ASTRewrite rewriter = rewriteVisitor.getRewriter();

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

