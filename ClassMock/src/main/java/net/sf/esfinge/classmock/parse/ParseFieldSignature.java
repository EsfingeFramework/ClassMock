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
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import net.sf.esfinge.classmock.MockClassLoader;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.FieldImp;

public final class ParseFieldSignature {

    private static ParseFieldSignature instance = new ParseFieldSignature();

    private ParseFieldSignature() {

        // Singleton
    }

    public static ParseFieldSignature getInstance() {

        return ParseFieldSignature.instance;
    }

    public FieldImp parse(final String signature) {

        final StringBuilder sb = new StringBuilder();
        sb.append("class A {\n");
        sb.append(signature.endsWith(";") ? signature : signature.concat(";"));
        sb.append("\n}");

        final FieldImp field = new FieldImp("NOT_DEFINED", null);

        final CompilationUnit compilationUnit = JavaParser.parse(sb.toString());
        compilationUnit.getClassByName("A").ifPresent(clazz -> {

            clazz.getFields().forEach(fd -> {

                field.name(fd.getVariables().get(0).toString());
                field.type(this.getType(fd));
                field.visibility(this.getVisibility(fd));
                field.modifiers(this.getModifiers(fd));
                fd.getAnnotations().forEach(an -> this.getAnnotationField(an)
                                .ifPresent(x -> field.annotation(x)));
            });
        });

        return field;
    }

    private Class<?> getType(final FieldDeclaration fd) {

        final String type = fd.getVariables().get(0).getType().asString();
        final List<String> classNames = new ArrayList<>();

        if (type.contains(".")) {
            classNames.add(type.trim());
        } else {
            this.getNativeJavaPackages().forEach(p -> classNames.add(p + type.trim()));
        }

        final List<Class<?>> classes = this.loadClasses(classNames)
                        .stream()
                        .collect(Collectors.toList());

        return classes.isEmpty() ? Object.class : classes.get(0);
    }

    private Optional<AnnotationImp> getAnnotationField(final AnnotationExpr an) {

        Optional<AnnotationImp> optional = Optional.empty();
        final List<String> classes = this.findImportForClasses(an.getNameAsString());

        for (final Class<?> clazz : this.loadClasses(classes)) {

            try {
                final AnnotationImp annotationImp = new AnnotationImp(clazz.asSubclass(Annotation.class));
                annotationImp.location(LocationEnum.FIELD);

                an.ifNormalAnnotationExpr(normal -> {

                    normal.getPairs().forEach(pair -> {

                        annotationImp.property(
                                        pair.getNameAsString(),
                                        pair.getValue().toString().replaceAll("\"", ""));
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

    private ModifierEnum[] getModifiers(final FieldDeclaration fd) {

        return fd.getModifiers()
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

    private VisibilityEnum getVisibility(final FieldDeclaration fd) {

        VisibilityEnum visibilityEnum;

        if (fd.isProtected()) {
            visibilityEnum = VisibilityEnum.PROTECTED;
        } else if (fd.isPublic()) {
            visibilityEnum = VisibilityEnum.PUBLIC;
        } else {
            visibilityEnum = VisibilityEnum.PRIVATE;
        }

        return visibilityEnum;
    }
}