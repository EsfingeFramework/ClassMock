package net.sf.esfinge.classmock.parse;

import java.io.File;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ReferenceType;

import net.sf.esfinge.classmock.MockClassLoader;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.FieldImp;
import net.sf.esfinge.classmock.imp.MethodImp;

public final class ParseMethodSignature {

    private static ParseMethodSignature instance = new ParseMethodSignature();

    private ParseMethodSignature() {

        // Singleton
    }

    public static ParseMethodSignature getInstance() {

        return ParseMethodSignature.instance;
    }

    public MethodImp parse(final String signature) {

        final StringBuilder sb = new StringBuilder();
        sb.append("class A {\n");

        if (!signature.contains("(")) {
            sb.append(signature.contains("()") ? signature : signature.concat("()"));
        } else {
            sb.append(signature);
        }
        if (!signature.contains("{}")) {
            sb.append(" {}");
        }

        sb.append("\n}");

        final MethodImp method = new MethodImp("NOT_DEFINED");

        final CompilationUnit compilationUnit = JavaParser.parse(sb.toString());
        compilationUnit.getClassByName("A").ifPresent(clazz -> {

            clazz.getMethods().forEach(md -> {

                method.name(md.getNameAsString());
                method.visibility(this.getVisibility(md));
                method.modifiers(this.getModifiers(md));
                method.returnType(this.getReturnType(md));
                method.exceptions(this.getExceptions(md));
                md.getAnnotations().forEach(an -> this.getAnnotationMethod(an)
                                .ifPresent(x -> method.annotation(x)));
                md.getParameters().forEach(p -> this.getField(p)
                                .ifPresent(x -> method.parameters().add(x)));
            });
        });

        return method;
    }

    private Optional<FieldImp> getField(final Parameter p) {

        Optional<FieldImp> optional = Optional.empty();

        try {
            optional = Optional.ofNullable(ParseFieldSignature.getInstance().parse(p.toString()));
        } catch (final Exception e) {
            // Something went wrong
        }

        return optional;
    }

    private Optional<AnnotationImp> getAnnotationMethod(final AnnotationExpr an) {

        Optional<AnnotationImp> optional = Optional.empty();
        final List<String> classes = this.findImportForClasses(an.getNameAsString());

        for (final Class<?> clazz : this.loadClasses(classes)) {

            try {
                final AnnotationImp annotationImp = new AnnotationImp(clazz.asSubclass(Annotation.class));
                annotationImp.location(LocationEnum.METHOD);

                an.ifNormalAnnotationExpr(normal -> {

                    normal.getPairs().forEach(pair -> {

                        annotationImp.property(
                                        pair.getNameAsString(),
                                        pair.getValue().toString().replace("\"", ""));
                    });
                });

                optional = Optional.ofNullable(annotationImp);
                break;

            } catch (final Exception e) {
                // Probably wrong import.
            }
        }

        return optional;
    }

    private Class<?> getReturnType(final MethodDeclaration md) {

        Class<?> clazz = void.class;
        final String returnType = md.getType().asString();

        if (!"void".equals(returnType)) {

            final List<String> classNames = new ArrayList<>();

            if (returnType.contains(".")) {
                classNames.add(returnType.trim());
            } else {
                this.getNativeJavaPackages().forEach(p -> classNames.add(p + returnType.trim()));
            }

            final List<Class<?>> classes = this.loadClasses(classNames).stream().collect(Collectors.toList());
            clazz = classes.isEmpty() ? void.class : classes.get(0);
        }

        return clazz;
    }

    private Class<?>[] getExceptions(final MethodDeclaration md) {

        Set<Class<?>> classes = new HashSet<>();

        for (final ReferenceType referenceType : md.getThrownExceptions()) {

            final String name = referenceType.asString();
            final List<String> classNames = this.findImportForClasses(name);

            classes = this.loadClasses(classNames);
        }

        return classes.stream().toArray(Class<?>[]::new);
    }

    private List<String> findImportForClasses(final String name) {

        final List<String> classNames = new ArrayList<>();

        if (name.contains(".")) {

            classNames.add(name);

        } else {

            this.getNativeJavaPackages().forEach(entry -> classNames.add(entry + name));
        }
        return classNames;
    }

    private Set<Class<?>> loadClasses(final List<String> classNames) {

        final Set<Class<?>> classes = new HashSet<>();

        for (final String className : classNames) {

            try {
                classes.add(Class.forName(className));
            } catch (final Exception e) {
                try {
                    classes.add(MockClassLoader.getInstance().loadClass(className));
                } catch (final Exception e1) {
                }
            }
        }

        return classes;
    }

    private List<String> getNativeJavaPackages() {

        final List<String> packages = new ArrayList<>();
        final Path path = new File("src/main/resources/java.packages.txt").toPath();

        try (final Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> packages.add(line + "."));
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return packages;
    }

    private ModifierEnum[] getModifiers(final MethodDeclaration md) {

        return md.getModifiers()
                        .stream()
                        .map(mod -> {
                            try {
                                return ModifierEnum.valueOf(mod.name());
                            } catch (final Exception e) {
                                return null;
                            }
                        })
                        .toArray(ModifierEnum[]::new);
    }

    private VisibilityEnum getVisibility(final MethodDeclaration md) {

        VisibilityEnum visibilityEnum;

        if (md.isProtected()) {
            visibilityEnum = VisibilityEnum.PROTECTED;
        } else if (md.isPrivate()) {
            visibilityEnum = VisibilityEnum.PRIVATE;
        } else {
            visibilityEnum = VisibilityEnum.PUBLIC;
        }

        return visibilityEnum;
    }
}