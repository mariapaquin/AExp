package main;

import Constraint.Constraint;
import Constraint.ExpressionLiteral;
import Solving.ConstraintSolver;
import org.eclipse.jdt.core.dom.*;
import visitor.ExpressionVisitor;
import visitor.MethodVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Driver {

    public static void main(String[] args) throws IOException {

        File file = new File("./tests/StatementSequence.java");
        String source = new String(Files.readAllBytes(file.toPath()));
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        // TODO: Need to do this separately for each method

        ExpressionVisitor exprVisitor = new ExpressionVisitor();
        cu.accept(exprVisitor);
        List<ExpressionLiteral> ae = exprVisitor.getAvailableExpressions();

        MethodVisitor methodVisitor = new MethodVisitor(ae);
        cu.accept(methodVisitor);

        System.out.println(" ------------- \n| Constraints |\n ------------- ");
        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        int i = 0;
        for (Constraint constraint : constraints) {
            System.out.println(++i + ") " + constraint);
        }

        System.out.println();

        System.out.println(" ------------  \n| Constraint |\n| Solutions  |\n ------------  ");
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();

    }

}
