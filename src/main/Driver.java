package main;

import Constraint.Constraint;
import Constraint.ExpressionLiteral;
import Solving.ConstraintSolver;
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

//        ExpressionVisitor exprVisitor = new ExpressionVisitor();
//        cu.accept(exprVisitor);

        AEVisitor aeVisitor = new AEVisitor();
        cu.accept(aeVisitor);
        List<ExpressionLiteral> ae = aeVisitor.getAvailableExpressions();

        // TODO: Need to use the same cu for rewriting.

        System.out.println(" ------------- \n| Constraints |\n ------------- ");
        ArrayList<Constraint> constraints = aeVisitor.getConstraints();

        int i = 0;
        for (Constraint constraint : constraints) {
            System.out.println(++i + ") " + constraint);
        }

        System.out.println();

        System.out.println(" ------------  \n| Constraint |\n| Solutions  |\n ------------  ");
        ConstraintSolver solver = new ConstraintSolver(constraints);

        solver.buildConstraintGraph();

        solver.processWorkList();

/*        solver.buildEntryMap();

        HashMap<ASTNode, List<String>>  entryMap = solver.getEntryMap();
        Set set = (Set) entryMap.entrySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            System.out.println("Key : " + mapEntry.getKey() + "Value : " + mapEntry.getValue() + "\n");
        }

        RewriteExprVisitor rewriteVisitor = new RewriteExprVisitor(ae, entryMap);
        cu.accept(rewriteVisitor);*/
    }
}

