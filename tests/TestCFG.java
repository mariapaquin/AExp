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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCFG {

    @Test
    public void testIf(){
        File file = new File("./tests/If.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 8);

        assertEquals("exit[a=b + 1] subset entry[a=b + 1] \\ [] U (b + 1)", constraints.get(0).toString());
        assertEquals("exit[b=0] subset entry[b=0] \\ [(b + 1), (b + 2)]", constraints.get(1).toString());
        assertEquals("entry[b=0] subset exit[true]", constraints.get(2).toString());
        assertEquals("exit[true] subset entry[true]", constraints.get(3).toString());
        assertEquals("entry[true] subset exit[a=b + 1]", constraints.get(4).toString());
        assertEquals("exit[a=b + 2] subset entry[a=b + 2] \\ [] U (b + 2)", constraints.get(5).toString());
        assertEquals("entry[a=b + 2] subset exit[true]", constraints.get(6).toString());
        assertEquals("entry[a=b + 2] subset exit[b=0]", constraints.get(7).toString());
    }

    @Test
    public void testIfNoBrackets(){
        File file = new File("./tests/IfNoBrackets.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 6);

        assertEquals("exit[b=1] subset entry[b=1] \\ []", constraints.get(0).toString());
        assertEquals("entry[b=1] subset exit[true]", constraints.get(1).toString());
        assertEquals("exit[true] subset entry[true]", constraints.get(2).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ []", constraints.get(3).toString());
        assertEquals("entry[b=2] subset exit[true]", constraints.get(4).toString());
        assertEquals("entry[b=2] subset exit[b=1]", constraints.get(5).toString());

    }

    @Test
    public void testIfElse(){
        File file = new File("./tests/IfElse.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 10);

        assertEquals("exit[a=b + 1] subset entry[a=b + 1] \\ [] U (b + 1)", constraints.get(0).toString());
        assertEquals("exit[b=0] subset entry[b=0] \\ [(b + 1), (b + 2)]", constraints.get(1).toString());
        assertEquals("entry[b=0] subset exit[true]", constraints.get(2).toString());
        assertEquals("exit[b=1] subset entry[b=1] \\ [(b + 1), (b + 2)]", constraints.get(3).toString());
        assertEquals("entry[b=1] subset exit[true]", constraints.get(4).toString());
        assertEquals("exit[true] subset entry[true]", constraints.get(5).toString());
        assertEquals("entry[true] subset exit[a=b + 1]", constraints.get(6).toString());
        assertEquals("exit[a=b + 2] subset entry[a=b + 2] \\ [] U (b + 2)", constraints.get(7).toString());
        assertEquals("entry[a=b + 2] subset exit[b=0]", constraints.get(8).toString());
        assertEquals("entry[a=b + 2] subset exit[b=1]", constraints.get(9).toString());

    }

    @Test
    public void testIfElseNoBrackets(){
        File file = new File("./tests/IfElseNoBrackets.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 8);

        assertEquals("exit[b=1] subset entry[b=1] \\ []", constraints.get(0).toString());
        assertEquals("entry[b=1] subset exit[true]", constraints.get(1).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ []", constraints.get(2).toString());
        assertEquals("entry[b=2] subset exit[true]", constraints.get(3).toString());
        assertEquals("exit[true] subset entry[true]", constraints.get(4).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ []", constraints.get(5).toString());
        assertEquals("entry[b=3] subset exit[b=1]", constraints.get(6).toString());
        assertEquals("entry[b=3] subset exit[b=2]", constraints.get(7).toString());

    }

    @Test
    public void testIfNested(){
        File file = new File("./tests/IfNested.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 13);

        assertEquals("exit[a=b + 1] subset entry[a=b + 1] \\ [] U (b + 1)", constraints.get(0).toString());
        assertEquals("exit[b=0] subset entry[b=0] \\ [(b + 1), (b + 2)]", constraints.get(1).toString());
        assertEquals("entry[b=0] subset exit[true]", constraints.get(2).toString());
        assertEquals("exit[b=1] subset entry[b=1] \\ [(b + 1), (b + 2)]", constraints.get(3).toString());
        assertEquals("entry[b=1] subset exit[false]", constraints.get(4).toString());
        assertEquals("exit[false] subset entry[false]", constraints.get(5).toString());
        assertEquals("entry[false] subset exit[b=0]", constraints.get(6).toString());
        assertEquals("exit[true] subset entry[true]", constraints.get(7).toString());
        assertEquals("entry[true] subset exit[a=b + 1]", constraints.get(8).toString());
        assertEquals("exit[a=b + 2] subset entry[a=b + 2] \\ [] U (b + 2)", constraints.get(9).toString());
        assertEquals("entry[a=b + 2] subset exit[true]", constraints.get(10).toString());
        assertEquals("entry[a=b + 2] subset exit[false]", constraints.get(11).toString());
        assertEquals("entry[a=b + 2] subset exit[b=1]", constraints.get(12).toString());

    }

    @Test
    public void testIfElseNested(){
        File file = new File("./tests/IfElseNested.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 13);

        assertEquals("exit[b=1] subset entry[b=1] \\ []", constraints.get(0).toString());
        assertEquals("entry[b=1] subset exit[false]", constraints.get(1).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ []", constraints.get(2).toString());
        assertEquals("entry[b=2] subset exit[false]", constraints.get(3).toString());
        assertEquals("exit[false] subset entry[false]", constraints.get(4).toString());
        assertEquals("entry[false] subset exit[true]", constraints.get(5).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ []", constraints.get(6).toString());
        assertEquals("entry[b=3] subset exit[true]", constraints.get(7).toString());
        assertEquals("exit[true] subset entry[true]", constraints.get(8).toString());
        assertEquals("exit[b=4] subset entry[b=4] \\ []", constraints.get(9).toString());
        assertEquals("entry[b=4] subset exit[b=1]", constraints.get(10).toString());
        assertEquals("entry[b=4] subset exit[b=2]", constraints.get(11).toString());
        assertEquals("entry[b=4] subset exit[b=3]", constraints.get(12).toString());

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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 9);

        assertEquals("exit[b=1] subset entry[b=1] \\ [(b == 1)]", constraints.get(0).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ [(b == 1)]", constraints.get(1).toString());
        assertEquals("entry[b=2] subset exit[b == 1]", constraints.get(2).toString());
        assertEquals("exit[b == 1] subset entry[b == 1]", constraints.get(3).toString());
        assertEquals("entry[b == 1] subset exit[b=1]", constraints.get(4).toString());
        assertEquals("entry[b == 1] subset exit[b=2]", constraints.get(5).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ [(b == 1)]", constraints.get(6).toString());
        assertEquals("entry[b=3] subset exit[b == 1]", constraints.get(7).toString());
        assertEquals("entry[b=3] subset exit[b=2]", constraints.get(8).toString());

    }

    @Test
    public void testNestedWhile(){
        File file = new File("./tests/NestedWhile.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 14);

        assertEquals("exit[b=1] subset entry[b=1] \\ [(b == 1), (b != 2)]", constraints.get(0).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ [(b == 1), (b != 2)]", constraints.get(1).toString());
        assertEquals("entry[b=2] subset exit[b != 2]", constraints.get(2).toString());
        assertEquals("exit[b != 2] subset entry[b != 2]", constraints.get(3).toString());
        assertEquals("entry[b != 2] subset exit[b == 1]", constraints.get(4).toString());
        assertEquals("entry[b != 2] subset exit[b=2]", constraints.get(5).toString());
        assertEquals("exit[b == 1] subset entry[b == 1]", constraints.get(6).toString());
        assertEquals("entry[b == 1] subset exit[b=1]", constraints.get(7).toString());
        assertEquals("entry[b == 1] subset exit[b != 2]", constraints.get(8).toString());
        assertEquals("entry[b == 1] subset exit[b=2]", constraints.get(9).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ [(b == 1), (b != 2)]", constraints.get(10).toString());
        assertEquals("entry[b=3] subset exit[b == 1]", constraints.get(11).toString());
        assertEquals("entry[b=3] subset exit[b != 2]", constraints.get(12).toString());
        assertEquals("entry[b=3] subset exit[b=2]", constraints.get(13).toString());
    }

    @Test
    public void testIfNestedWhile(){
        File file = new File("./tests/IfNestedWhile.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        assertEquals(constraints.size(), 12);

        assertEquals("exit[b=1] subset entry[b=1] \\ [(b == 1), (b != 2)]", constraints.get(0).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ [(b == 1), (b != 2)]", constraints.get(1).toString());
        assertEquals("entry[b=2] subset exit[b != 2]", constraints.get(2).toString());
        assertEquals("exit[b != 2] subset entry[b != 2]", constraints.get(3).toString());
        assertEquals("entry[b != 2] subset exit[b == 1]", constraints.get(4).toString());
        assertEquals("entry[b != 2] subset exit[b=2]", constraints.get(5).toString());
        assertEquals("exit[b == 1] subset entry[b == 1]", constraints.get(6).toString());
        assertEquals("entry[b == 1] subset exit[b=1]", constraints.get(7).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ [(b == 1), (b != 2)]", constraints.get(8).toString());
        assertEquals("entry[b=3] subset exit[b == 1]", constraints.get(9).toString());
        assertEquals("entry[b=3] subset exit[b != 2]", constraints.get(10).toString());
        assertEquals("entry[b=3] subset exit[b=2]", constraints.get(11).toString());
    }

    @Test
    public void testFor(){
        File file = new File("./tests/For.java");
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

        ArrayList<Constraint> constraints = methodVisitor.getConstraints();

        for(int i = 0; i < constraints.size(); i++){
            System.out.println((i+1) + " " + constraints.get(i));
        }
    }

}
