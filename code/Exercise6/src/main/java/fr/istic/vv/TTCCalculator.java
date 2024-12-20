package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class TTCCalculator extends VoidVisitorWithDefaults<Void> {

    private final Set<String> fields;
    private final Map<String, Set<String>> methodFieldAccessMap;
    private String currentMethodName;
    private String currentPackage;

    public TTCCalculator(){
        this.fields = new HashSet<>();
        this.methodFieldAccessMap = new HashMap<>();
        this.currentMethodName = null;
    }

    @Override
    public void visit(FieldDeclaration field, Void arg) {
        field.getVariables().forEach(variable -> fields.add(variable.getNameAsString()));
    }

    @Override
    public void visit(NameExpr nameExpr, Void arg) {
        String fieldName = nameExpr.getNameAsString();
        if (fields.contains(fieldName)) {
            methodFieldAccessMap.get(currentMethodName).add(fieldName);
        }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;
        String name = declaration.getNameAsString();
        methodFieldAccessMap.put(name, new HashSet<>());
        currentMethodName = name;

        declaration.getBody().ifPresent(body -> {
            body.getStatements().forEach(statement -> statement.accept(this, arg));
        });
    }


    @Override
    public void visit(CompilationUnit unit, Void arg) {
        this.currentPackage = unit.getPackageDeclaration().orElse(new PackageDeclaration()).getNameAsString();
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        this.currentMethodName = declaration.getNameAsString();
        visitTypeDeclaration(declaration, arg);
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if (!declaration.isPublic()) return;

        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        for (FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration) {
                member.accept(this, arg);
            }
        }
    }

    public double calculateTCC() {
        List<String> methodNames = new ArrayList<>(methodFieldAccessMap.keySet());
        int totalPairs = methodNames.size() * (methodNames.size() - 1) / 2;
        if (totalPairs == 0) {
            return 1.0;
        }

        int connectedPairs = 0;

        for (int i = 0; i < methodNames.size(); i++) {
            for (int j = i + 1; j < methodNames.size(); j++) {
                Set<String> fields1 = methodFieldAccessMap.get(methodNames.get(i));
                Set<String> fields2 = methodFieldAccessMap.get(methodNames.get(j));
                if (!Collections.disjoint(fields1, fields2)) {
                    connectedPairs++;
                }
            }
        }
        System.out.println(connectedPairs + " / " + totalPairs);
        return (double) connectedPairs / totalPairs; // nombre de paire connecté / nombre de pair possible
    }

    public void write(String name, PrintStream out) {
        out.println(name + " -> TTC : " + calculateTCC());
    }
}
