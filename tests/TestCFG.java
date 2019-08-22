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
        assertEquals("entry[b=0] subset exit[if(true)]", constraints.get(2).toString());
        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(3).toString());
        assertEquals("entry[if(true)] subset exit[a=b + 1]", constraints.get(4).toString());
        assertEquals("exit[a=b + 2] subset entry[a=b + 2] \\ [] U (b + 2)", constraints.get(5).toString());
        assertEquals("entry[a=b + 2] subset exit[if(true)]", constraints.get(6).toString());
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
        assertEquals("entry[b=1] subset exit[if(true)]", constraints.get(1).toString());
        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(2).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ []", constraints.get(3).toString());
        assertEquals("entry[b=2] subset exit[if(true)]", constraints.get(4).toString());
        assertEquals("entry[b=2] subset exit[b=1]", constraints.get(5).toString());
    }

    @Test
    public void testIfEmptyBlock(){
        File file = new File("./tests/IfEmptyBlock.java");
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

        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(0).toString());
        assertEquals("exit[System.out.println(a)] subset entry[System.out.println(a)]", constraints.get(1).toString());
        assertEquals("entry[System.out.println(a)] subset exit[if(true)]", constraints.get(2).toString());

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }

    }

    @Test
    public void testIfNestedEmptyBlocks(){
        File file = new File("./tests/IfNestedEmptyBlocks.java");
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

        assertEquals("exit[if(false)] subset entry[if(false)]", constraints.get(0).toString());
        assertEquals("entry[if(false)] subset exit[if(true)]", constraints.get(1).toString());
        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(2).toString());
        assertEquals("exit[System.out.println(a)] subset entry[System.out.println(a)]", constraints.get(3).toString());
        assertEquals("entry[System.out.println(a)] subset exit[if(true)]", constraints.get(4).toString());
        assertEquals("entry[System.out.println(a)] subset exit[if(false)]", constraints.get(5).toString());

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }

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
        assertEquals("entry[b=0] subset exit[if(true)]", constraints.get(2).toString());
        assertEquals("exit[b=1] subset entry[b=1] \\ [(b + 1), (b + 2)]", constraints.get(3).toString());
        assertEquals("entry[b=1] subset exit[if(true)]", constraints.get(4).toString());
        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(5).toString());
        assertEquals("entry[if(true)] subset exit[a=b + 1]", constraints.get(6).toString());
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
        assertEquals("entry[b=1] subset exit[if(true)]", constraints.get(1).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ []", constraints.get(2).toString());
        assertEquals("entry[b=2] subset exit[if(true)]", constraints.get(3).toString());
        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(4).toString());
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
        assertEquals("entry[b=0] subset exit[if(true)]", constraints.get(2).toString());
        assertEquals("exit[b=1] subset entry[b=1] \\ [(b + 1), (b + 2)]", constraints.get(3).toString());
        assertEquals("entry[b=1] subset exit[if(false)]", constraints.get(4).toString());
        assertEquals("exit[if(false)] subset entry[if(false)]", constraints.get(5).toString());
        assertEquals("entry[if(false)] subset exit[b=0]", constraints.get(6).toString());
        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(7).toString());
        assertEquals("entry[if(true)] subset exit[a=b + 1]", constraints.get(8).toString());
        assertEquals("exit[a=b + 2] subset entry[a=b + 2] \\ [] U (b + 2)", constraints.get(9).toString());
        assertEquals("entry[a=b + 2] subset exit[if(true)]", constraints.get(10).toString());
        assertEquals("entry[a=b + 2] subset exit[if(false)]", constraints.get(11).toString());
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
        assertEquals("entry[b=1] subset exit[if(false)]", constraints.get(1).toString());
        assertEquals("exit[b=2] subset entry[b=2] \\ []", constraints.get(2).toString());
        assertEquals("entry[b=2] subset exit[if(false)]", constraints.get(3).toString());
        assertEquals("exit[if(false)] subset entry[if(false)]", constraints.get(4).toString());
        assertEquals("entry[if(false)] subset exit[if(true)]", constraints.get(5).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ []", constraints.get(6).toString());
        assertEquals("entry[b=3] subset exit[if(true)]", constraints.get(7).toString());
        assertEquals("exit[if(true)] subset entry[if(true)]", constraints.get(8).toString());
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
        assertEquals("entry[b=2] subset exit[while(b == 1)]", constraints.get(2).toString());
        assertEquals("exit[while(b == 1)] subset entry[while(b == 1)]", constraints.get(3).toString());
        assertEquals("entry[while(b == 1)] subset exit[b=1]", constraints.get(4).toString());
        assertEquals("entry[while(b == 1)] subset exit[b=2]", constraints.get(5).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ [(b == 1)]", constraints.get(6).toString());
        assertEquals("entry[b=3] subset exit[while(b == 1)]", constraints.get(7).toString());
        assertEquals("entry[b=3] subset exit[b=2]", constraints.get(8).toString());
    }

    @Test
    public void testEmptyWhile(){
        File file = new File("./tests/EmptyWhile.java");
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

        assertEquals(constraints.size(), 3);

        assertEquals("exit[while(b == 1)] subset entry[while(b == 1)]", constraints.get(0).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ [(b == 1)]", constraints.get(1).toString());
        assertEquals("entry[b=3] subset exit[while(b == 1)]", constraints.get(2).toString());

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }
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
        assertEquals("entry[b=2] subset exit[while(b != 2)]", constraints.get(2).toString());
        assertEquals("exit[while(b != 2)] subset entry[while(b != 2)]", constraints.get(3).toString());
        assertEquals("entry[while(b != 2)] subset exit[while(b == 1)]", constraints.get(4).toString());
        assertEquals("entry[while(b != 2)] subset exit[b=2]", constraints.get(5).toString());
        assertEquals("exit[while(b == 1)] subset entry[while(b == 1)]", constraints.get(6).toString());
        assertEquals("entry[while(b == 1)] subset exit[b=1]", constraints.get(7).toString());
        assertEquals("entry[while(b == 1)] subset exit[while(b != 2)]", constraints.get(8).toString());
        assertEquals("entry[while(b == 1)] subset exit[b=2]", constraints.get(9).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ [(b == 1), (b != 2)]", constraints.get(10).toString());
        assertEquals("entry[b=3] subset exit[while(b == 1)]", constraints.get(11).toString());
        assertEquals("entry[b=3] subset exit[while(b != 2)]", constraints.get(12).toString());
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
        assertEquals("entry[b=2] subset exit[while(b != 2)]", constraints.get(2).toString());
        assertEquals("exit[while(b != 2)] subset entry[while(b != 2)]", constraints.get(3).toString());
        assertEquals("entry[while(b != 2)] subset exit[if(b == 1)]", constraints.get(4).toString());
        assertEquals("entry[while(b != 2)] subset exit[b=2]", constraints.get(5).toString());
        assertEquals("exit[if(b == 1)] subset entry[if(b == 1)]", constraints.get(6).toString());
        assertEquals("entry[if(b == 1)] subset exit[b=1]", constraints.get(7).toString());
        assertEquals("exit[b=3] subset entry[b=3] \\ [(b == 1), (b != 2)]", constraints.get(8).toString());
        assertEquals("entry[b=3] subset exit[if(b == 1)]", constraints.get(9).toString());
        assertEquals("entry[b=3] subset exit[while(b != 2)]", constraints.get(10).toString());
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

        assertEquals("exit[a=0] subset entry[a=0] \\ [(i < a)]", constraints.get(0).toString());
        assertEquals("exit[a=1] subset entry[a=1] \\ [(i < a)]", constraints.get(1).toString());
        assertEquals("entry[a=1] subset exit[i < a]", constraints.get(2).toString());
        assertEquals("exit[for( i < a)] subset entry[for( i < a)]", constraints.get(3).toString());
        assertEquals("entry[for( i < a)] subset exit[a=0]", constraints.get(4).toString());
        assertEquals("exit[int i=0] subset entry[int i=0]", constraints.get(5).toString());
        assertEquals("entry[int i=0] subset exit[for( i < a)]", constraints.get(6).toString());
        assertEquals("exit[i < a] subset entry[i < a]", constraints.get(7).toString());
        assertEquals("entry[i < a] subset exit[int i=0]", constraints.get(8).toString());
        assertEquals("exit[i++] subset entry[i++]", constraints.get(9).toString());
        assertEquals("entry[i++] subset exit[a=1]", constraints.get(10).toString());
        assertEquals("entry[i < a] subset exit[i++]", constraints.get(11).toString());
        assertEquals("exit[a=2] subset entry[a=2] \\ [(i < a)]", constraints.get(12).toString());
        assertEquals("entry[a=2] subset exit[i < a]", constraints.get(13).toString());

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }
    }

    @Test
    public void testEmptyFor(){
        File file = new File("./tests/EmptyFor.java");
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

        assertEquals("exit[for( i < a)] subset entry[for( i < a)]", constraints.get(0).toString());
        assertEquals("exit[int i=0] subset entry[int i=0]", constraints.get(1).toString());
        assertEquals("entry[int i=0] subset exit[for( i < a)]", constraints.get(2).toString());
        assertEquals("exit[i < a] subset entry[i < a]", constraints.get(3).toString());
        assertEquals("entry[i < a] subset exit[int i=0]", constraints.get(4).toString());
        assertEquals("exit[i++] subset entry[i++]", constraints.get(5).toString());
        assertEquals("entry[i++] subset exit[i < a]", constraints.get(6).toString());
        assertEquals("entry[i < a] subset exit[i++]", constraints.get(7).toString());
        assertEquals("exit[a=2] subset entry[a=2] \\ [(i < a)]", constraints.get(8).toString());
        assertEquals("entry[a=2] subset exit[i < a]", constraints.get(9).toString());

        assertEquals(constraints.size(), 10);

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }
    }

    @Test
    public void testForIfElse(){
        File file = new File("./tests/ForIfElse.java");
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

        assertEquals("exit[a=1] subset entry[a=1] \\ [(i < a)]", constraints.get(0).toString());
        assertEquals("entry[a=1] subset exit[if(i == 0)]", constraints.get(1).toString());
        assertEquals("exit[a=2] subset entry[a=2] \\ [(i < a)]", constraints.get(2).toString());
        assertEquals("entry[a=2] subset exit[if(i == 0)]", constraints.get(3).toString());
        assertEquals("exit[if(i == 0)] subset entry[if(i == 0)]", constraints.get(4).toString());
        assertEquals("entry[if(i == 0)] subset exit[i < a]", constraints.get(5).toString());
        assertEquals("exit[for( i < a)] subset entry[for( i < a)]", constraints.get(6).toString());
        assertEquals("exit[int i=0] subset entry[int i=0]", constraints.get(7).toString());
        assertEquals("entry[int i=0] subset exit[for( i < a)]", constraints.get(8).toString());
        assertEquals("exit[i < a] subset entry[i < a]", constraints.get(9).toString());
        assertEquals("entry[i < a] subset exit[int i=0]", constraints.get(10).toString());
        assertEquals("exit[i++] subset entry[i++]", constraints.get(11).toString());
        assertEquals("entry[i++] subset exit[a=1]", constraints.get(12).toString());
        assertEquals("entry[i++] subset exit[a=2]", constraints.get(13).toString());
        assertEquals("entry[i < a] subset exit[i++]", constraints.get(14).toString());
        assertEquals("exit[a=3] subset entry[a=3] \\ [(i < a)]", constraints.get(15).toString());
        assertEquals("entry[a=3] subset exit[i < a]", constraints.get(16).toString());
    }

    @Test
    public void testEnhancedFor(){
        File file = new File("./tests/EnhancedFor.java");
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

        assertEquals("exit[System.out.println(a)] subset entry[System.out.println(a)]", constraints.get(0).toString());
        assertEquals("exit[System.out.println(b)] subset entry[System.out.println(b)]", constraints.get(1).toString());
        assertEquals("entry[System.out.println(b)] subset exit[for(String str)]", constraints.get(2).toString());
        assertEquals("exit[for(String str)] subset entry[for(String str)]", constraints.get(3).toString());
        assertEquals("entry[for(String str)] subset exit[System.out.println(a)]", constraints.get(4).toString());
        assertEquals("exit[System.out.println(c)] subset entry[System.out.println(c)]", constraints.get(5).toString());
        assertEquals("entry[System.out.println(c)] subset exit[for(String str)]", constraints.get(6).toString());
        assertEquals("entry[System.out.println(c)] subset exit[System.out.println(b)]", constraints.get(7).toString());

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }
    }

    @Test
    public void testEmptyEnhancedFor(){
        File file = new File("./tests/EmptyEnhancedFor.java");
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

        assertEquals(constraints.size(), 3);

        assertEquals("exit[for(String str)] subset entry[for(String str)]", constraints.get(0).toString());
        assertEquals("exit[System.out.println(c)] subset entry[System.out.println(c)]", constraints.get(1).toString());
        assertEquals("entry[System.out.println(c)] subset exit[for(String str)]", constraints.get(2).toString());

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }
    }

}
