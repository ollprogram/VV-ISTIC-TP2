package fr.istic.vv;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class ProjectAnalyzer {

    private final TTCCalculator ttcCalculator;

    public ProjectAnalyzer(){
        this.ttcCalculator = new TTCCalculator();
    }

    public void analyzeProject(String projectPath, PrintStream out) {
        try {
            List<Path> javaFiles = findJavaFiles(Paths.get(projectPath));

            for (Path javaFile : javaFiles) {
                //Logger.getGlobal().info("File analyze : " + javaFile.toString());
                analyzeFile(javaFile.toFile(), out);
            }

        } catch (IOException e) {
            Logger.getGlobal().severe("Analyze error : " + e.getMessage());
        }
    }

    private List<Path> findJavaFiles(Path projectPath) throws IOException {
        return Files.walk(projectPath)
                .filter(path -> path.toString().endsWith(".java"))
                .toList();
    }

    private void analyzeFile(File file, PrintStream out) {
        try (FileInputStream fis = new FileInputStream(file)) {
            CompilationUnit cu = StaticJavaParser.parse(fis);
            this.ttcCalculator.visit(cu, null);
            this.ttcCalculator.write(file.getName(), out);

        } catch (Exception e) {
            Logger.getGlobal().severe("Erreur lors de l'analyse du fichier : " + file.getName() + " - " + e.getMessage());
        }
    }
}

