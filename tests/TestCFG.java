import Constraint.Constraint;
import Constraint.Term.ExpressionLiteral;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import visitor.ExpressionVisitor;
import visitor.MethodVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCFG {
    HashSet<Constraint> constraints;

    @BeforeEach
    public void setUp() throws Exception
    {
        File file = new File("./tests/IfNestedWhile.java");
        String source = new String(Files.readAllBytes(file.toPath()));
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        ExpressionVisitor exprVisitor = new ExpressionVisitor();
        cu.accept(exprVisitor);
        List<ExpressionLiteral> ae = exprVisitor.getAvailableExpressions();

        MethodVisitor methodVisitor = new MethodVisitor(ae);
        cu.accept(methodVisitor);

        constraints = methodVisitor.getConstraints();
    }


    @Test
    public void testWhile(){
        File file = new File("./tests/While.java");
        String source = null;
        try {
            source = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        ExpressionVisitor exprVisitor = new ExpressionVisitor();
        cu.accept(exprVisitor);
        List<ExpressionLiteral> ae = exprVisitor.getAvailableExpressions();

        MethodVisitor methodVisitor = new MethodVisitor(ae);
        cu.accept(methodVisitor);

        HashSet<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 9);
/*        int i = 0;
        for (Constraint constraint : constraints) {
            System.out.println(++i + ") " + constraint);
        }*/
    }
    
}
