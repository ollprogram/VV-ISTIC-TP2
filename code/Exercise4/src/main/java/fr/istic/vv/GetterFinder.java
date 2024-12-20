package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.logging.Logger;

public class GetterFinder extends VoidVisitorWithDefaults<Void> {

    private ReportWriter writer;

    private String currentPackage;

    private String currentClass;

    public GetterFinder() {
        super();
        this.writer = new ReportWriter();
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        currentPackage = unit.getPackageDeclaration().orElse(new PackageDeclaration()).getNameAsString();
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic())
            return;
        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
    }

    @Override
    public void visit(FieldDeclaration field, Void arg) {
        if (!field.isPrivate())
            return;
        for (VariableDeclarator v : field.getVariables()) {
            Logger.getGlobal().info("Private : " + v.getNameAsString());
            writer.addField(v.getNameAsString(), currentClass, currentPackage, v.getType());
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
        if (!declaration.isPublic())
            return;
        String name = declaration.getNameAsString();
        Logger.getGlobal().info("Public method : " + name);
        writer.checkGetter(name, currentClass, currentPackage, declaration.getType());
    }

    public void writeReport(OutputStream outputStream) throws IOException {
        this.writer.write(new PrintStream(outputStream));
    }

}
