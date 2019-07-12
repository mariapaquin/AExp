package tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class ASTTester {

    public static void main(String[] args) throws IOException {
        File file = new File("./src/tests/Test.java");
        String source = new String(Files.readAllBytes(file.toPath()));
        ASTParser parser = ASTParser.newParser(AST.JLS3);

        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);

        parser.setSource(source.toCharArray());

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        TypeFinderVisitor v = new TypeFinderVisitor();
        cu.accept(v);
    }
}

class TypeFinderVisitor extends ASTVisitor{

    public boolean visit(VariableDeclarationStatement node){
        System.out.println(node);
        for (Iterator iter = node.fragments().iterator(); iter.hasNext();) {
            System.out.println("------------------");

            VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
        }
        return true;
    }
}