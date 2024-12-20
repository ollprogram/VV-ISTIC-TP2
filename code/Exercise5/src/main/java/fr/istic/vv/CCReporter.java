package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.PrintStream;
import java.util.logging.Logger;

public class CCReporter extends VoidVisitorWithDefaults<Void> {

    private String currentPackage;
    private String currentClass;
    private String currentMethod;

    private int nodes = 0;
    private int edges = 0;
    private final static int COMPONENTS = 1;
    private final ReportBuilder reportBuilder;

    public CCReporter(ReportBuilder builder){
        super();
        this.reportBuilder = builder;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        currentPackage = unit.getPackageDeclaration().orElse(new PackageDeclaration()).getNameAsString();
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        currentClass = declaration.getNameAsString();
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        currentClass = declaration.getNameAsString();
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        currentMethod = declaration.getNameAsString();
        declaration.getBody().orElse(new BlockStmt()).accept(this, arg);
        //CC = E - N + 2P
        int cc = (edges - nodes) + (2 * COMPONENTS);
        Logger.getGlobal().info(currentMethod+" CC score : "+cc+", edges : "+edges+", nodes : "+nodes);
        reportBuilder.addCC(currentPackage, currentClass, currentMethod, cc);
        edges = 0;
        nodes = 0;
    }

    private boolean isControlStatement(Statement s){
        return s.isForStmt() || s.isWhileStmt() || s.isIfStmt() || s.isForEachStmt();
    }

    @Override
    public void visit(BlockStmt blockStmt, Void arg){
        nodes++;
        for(Statement s : blockStmt.getStatements()){
            if(isControlStatement(s)) nodes++;
            s.accept(this, arg);
        }
    }

    @Override
    public void visit(IfStmt ifStmt, Void arg){
        edges +=3;
        ifStmt.getElseStmt().ifPresent((e) -> {e.accept(this, arg);});
        if(ifStmt.hasElseBlock()) {
            edges++;
        }
        ifStmt.getThenStmt().accept(this, arg);
    }

    private void increaseAsLoop(){
        edges += 3;// (block, break, iteration)
    }

    @Override
    public void visit(WhileStmt loop, Void arg){
        increaseAsLoop();
        loop.getBody().accept(this, arg);
    }

    @Override
    public void visit(ForStmt loop, Void arg){
        increaseAsLoop();
        loop.getBody().accept(this, arg);
    }

    @Override
    public void visit(ForEachStmt whileStmt, Void arg){
        increaseAsLoop();
        whileStmt.getBody().accept(this, arg);
    }

    @Override
    public void visit(SwitchStmt switchStmt, Void arg){//each switch entry is an "if else" with default included in entries
        switchStmt.getEntries().accept(this, arg);
    }

    @Override
    public void visit(SwitchEntry switchEntry, Void arg){//each switch entry is an "if else" with default included in entries
        edges+=2;
        switchEntry.getStatements().accept(this, arg);
    }

}
