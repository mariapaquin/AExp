package AE;

import Constraint.Constraint;
import Constraint.ExpressionLiteral;
import Solving.ConstraintSolver;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.Test;
import visitor.ExpressionVisitor;
import visitor.AEVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAE {

    @Test
    public void testStatementSequence(){
        File file = new File("./tests/AE/StatementSequence.java");
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();

    }

    @Test
    public void testIf(){
        File file = new File("./tests/AE/If.java");
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();


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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();

//        for(int i = 0; i < constraints.size(); i++){
//            System.out.println((i+1) + " " + constraints.get(i));
//        }

    }

    @Test
    public void testIfElse() {
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
    }

    @Test
    public void testIfElseNoBrackets() {
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();

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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();

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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
    }

    @Test
    public void testDoWhile(){
        File file = new File("./tests/DoWhile.java");
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
    }

    @Test
    public void testEmptyDoWhile(){
        File file = new File("./tests/EmptyDoWhile.java");
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

        AEVisitor AEVisitor = new AEVisitor(ae);
        cu.accept(AEVisitor);

        ArrayList<Constraint> constraints = AEVisitor.getConstraints();
        ConstraintSolver solver = new ConstraintSolver(constraints, ae);

        solver.buildConstraintGraph();

        solver.initializeAESet();

        solver.processWorkList();
    }

}
