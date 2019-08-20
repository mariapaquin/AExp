package main;

import Constraint.Constraint;
import Constraint.Term.ExpressionLiteral;
import Solving.ConstraintSolver;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import visitor.ExpressionVisitor;
import visitor.MethodVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Driver {

    public static void main(String[] args) throws IOException {

        File file = new File("./src/test/Test.java");
        String source = new String(Files.readAllBytes(file.toPath()));
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        AST ast = cu.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        // TODO: Need to do this separately for each method

        ExpressionVisitor exprVisitor = new ExpressionVisitor();
        cu.accept(exprVisitor);
        List<ExpressionLiteral> ae = exprVisitor.getAvailableExpressions();

        MethodVisitor methodVisitor = new MethodVisitor(ae);
        cu.accept(methodVisitor);

        System.out.println(" ------------- \n| Constraints |\n ------------- ");
        HashSet<Constraint> constraints = methodVisitor.getConstraints();

        int i = 0;
        for (Constraint constraint : constraints) {
            System.out.println(++i + ") " + constraint);
        }
        System.out.println();

        // find local variables in the method
        Set<String> variables = new HashSet<String>();

        cu.accept(new ASTVisitor() {
            @Override
            public boolean visit(SimpleName node) {
                if (node.getLocationInParent() == TypeDeclaration.NAME_PROPERTY ||
                        node.getLocationInParent() == MethodDeclaration.NAME_PROPERTY ||
                        node.getLocationInParent() == SingleVariableDeclaration.NAME_PROPERTY ||
                        node.getLocationInParent() == QualifiedName.NAME_PROPERTY ||
                        node.getLocationInParent() == QualifiedName.QUALIFIER_PROPERTY ||
                        node.getLocationInParent() == PackageDeclaration.NAME_PROPERTY ||
                        node.getLocationInParent() == SimpleType.NAME_PROPERTY ||
                        node.getLocationInParent() == ImportDeclaration.NAME_PROPERTY ||
                        node.getLocationInParent() == TypeParameter.NAME_PROPERTY) {
                    return true;
                }
                variables.add(node.getIdentifier());
                return true;
            }
        });

        System.out.println(" ------------  \n| Constraint |\n| Solutions  |\n ------------  ");
        ConstraintSolver solver = new ConstraintSolver(constraints, variables);

//        solver.buildConstraintGraph();
//
//        solver.initializeDefinitionSet();
//
//        solver.processWorkList();

    }

}
