package net.sf.esfinge.classmock;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sf.esfinge.classmock.api.IAnnotationPropertyWriter;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IClassReader;
import net.sf.esfinge.classmock.api.IClassWriter;
import net.sf.esfinge.classmock.api.IFieldReader;
import net.sf.esfinge.classmock.api.IFieldWriter;
import net.sf.esfinge.classmock.api.IMethodReader;
import net.sf.esfinge.classmock.api.IMethodWriter;
import net.sf.esfinge.classmock.api.ISuperClassReader;
import net.sf.esfinge.classmock.api.ISuperClassWriter;
import net.sf.esfinge.classmock.api.enums.JavaEnum;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.FieldImp;
import net.sf.esfinge.classmock.imp.MethodImp;
import net.sf.esfinge.classmock.imp.SuperClassImp;

public class ClassMock implements IClassReader, IClassWriter {

    private String name;

    private final SuperClassImp superClassImp = new SuperClassImp(Object.class);

    private JavaEnum javaEnum = JavaEnum.V1_8;

    private VisibilityEnum visibility = VisibilityEnum.PUBLIC;

    private final Set<ModifierEnum> modifiers = new HashSet<>();

    private final Set<Class<?>> interfaces = new HashSet<>();

    private final List<IAnnotationReader> annotations = new ArrayList<>();

    private final Set<IFieldReader> fields = new LinkedHashSet<>();

    private final Set<IMethodReader> methods = new HashSet<>();

    private ClassMock(final String name) {

        this.name = name;
        this.asClass();
    }

    /**
     *
     * @param name
     *            of the entity
     * @return IClassWriter
     */
    public static IClassWriter of(final String name) {

        return new ClassMock(name);
    }

    @Override
    public IClassWriter asClass() {

        this.modifiers.clear();
        this.modifiers.add(ModifierEnum.SUPER);

        this.superclass(Object.class);
        this.interfaces().remove(Annotation.class);

        return this;
    }

    @Override
    public IClassWriter asInterface() {

        this.modifiers.clear();
        this.modifiers.add(ModifierEnum.ABSTRACT);
        this.modifiers.add(ModifierEnum.INTERFACE);

        this.superclass(Object.class);
        this.interfaces().remove(Annotation.class);

        return this;
    }

    @Override
    public IClassWriter asAbstract() {

        this.modifiers.clear();
        this.modifiers.add(ModifierEnum.ABSTRACT);
        this.modifiers.add(ModifierEnum.SUPER);

        this.superclass(Object.class);
        this.interfaces().remove(Annotation.class);

        return this;
    }

    @Override
    public IClassWriter asEnum() {

        this.modifiers.clear();
        this.modifiers.add(ModifierEnum.FINAL);
        this.modifiers.add(ModifierEnum.SUPER);
        this.modifiers.add(ModifierEnum.ENUM);

        this.superclass(Enum.class);
        this.interfaces().remove(Annotation.class);

        return this;
    }

    @Override
    public IClassWriter asAnnotation() {

        this.modifiers.clear();
        this.modifiers.add(ModifierEnum.ANNOTATION);
        this.modifiers.add(ModifierEnum.ABSTRACT);
        this.modifiers.add(ModifierEnum.INTERFACE);

        this.superclass(Object.class);
        this.interfaces(Annotation.class);

        return this;
    }

    @Override
    public IClassWriter name(final String name) {

        this.name = name;

        return this;
    }

    @Override
    public IClassWriter visibility(final VisibilityEnum visibility) {

        this.visibility = visibility;

        return this;
    }

    @Override
    public ISuperClassWriter superclass(final Class<?> superclass) {

        this.superClassImp.superclass(superclass);

        return this.superClassImp;
    }

    @Override
    public IClassWriter interfaces(final Class<?>... classes) {

        this.interfaces.addAll(Arrays.asList(classes));

        return this;
    }

    @Override
    public IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation) {

        return this.annotation(annotation, LocationEnum.CLASS);
    }

    @Override
    public IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation, final LocationEnum location) {

        final AnnotationImp wrapper = new AnnotationImp(annotation);
        wrapper.location(location);

        return this.annotation(wrapper);
    }

    @Override
    public IAnnotationPropertyWriter annotation(final IAnnotationReader annotation) {

        AnnotationImp wrapper;

        if (annotation instanceof AnnotationImp) {
            wrapper = (AnnotationImp) annotation;
        } else {
            wrapper = new AnnotationImp(annotation);
        }

        wrapper.setAnd(this);
        this.annotations.add(wrapper);

        return wrapper;
    }

    @Override
    public IFieldWriter field(final String name, final Class<?> type) {

        final FieldImp wrapper = new FieldImp(name, type);
        this.fields.add(wrapper);

        return wrapper;
    }

    @Override
    public IMethodWriter method(final String name) {

        final MethodImp method = new MethodImp(name);
        this.methods.add(method);

        return method;
    }

    @Override
    public IMethodWriter method(final IMethodReader method) {

        MethodImp wrapper;

        if (method instanceof MethodImp) {
            wrapper = (MethodImp) method;
        } else {
            wrapper = new MethodImp(method);
        }

        this.methods.add(wrapper);

        return wrapper;
    }

    @Override
    public boolean isClass() {

        return !this.modifiers.contains(ModifierEnum.ABSTRACT)
                        && this.modifiers.contains(ModifierEnum.SUPER);
    }

    @Override
    public boolean isInterface() {

        return this.modifiers.contains(ModifierEnum.INTERFACE);
    }

    @Override
    public boolean isAbstract() {

        return this.modifiers.contains(ModifierEnum.ABSTRACT)
                        && this.modifiers.contains(ModifierEnum.SUPER);
    }

    @Override
    public boolean isEnum() {

        return this.modifiers.contains(ModifierEnum.ENUM);
    }

    @Override
    public boolean isAnnotation() {

        return this.modifiers.contains(ModifierEnum.ANNOTATION);
    }

    @Override
    public String name() {

        return this.name;
    }

    @Override
    public VisibilityEnum visibility() {

        return this.visibility;
    }

    @Override
    public ISuperClassReader superclass() {

        return this.superClassImp;
    }

    @Override
    public Collection<Class<?>> interfaces() {

        return this.interfaces;
    }

    @Override
    public Collection<IFieldReader> fields() {

        return this.fields;
    }

    @Override
    public Collection<IMethodReader> methods() {

        return this.methods;
    }

    @Override
    public Collection<IAnnotationReader> annotations() {

        return this.annotations;
    }

    @Override
    public IClassWriter modifiers(final ModifierEnum... modifiers) {

        this.modifiers.addAll(Arrays.asList(modifiers));

        return this;
    }

    @Override
    public Collection<ModifierEnum> modifiers() {

        return this.modifiers;
    }

    @Override
    public IClassWriter version(final JavaEnum javaEnum) {

        this.javaEnum = javaEnum;

        return this;
    }

    @Override
    public JavaEnum version() {

        return this.javaEnum;
    }

    @Override
    public Class<?> build() {

        Class<?> clazz;

        try {

            clazz = MockClassLoader.getInstance().loadClass(this.name);

        } catch (final ClassNotFoundException e) {

            final ParseASM asm = new ParseASM(this);
            clazz = MockClassLoader.getInstance().defineClass(this.name, asm.parse());
        }

        return clazz;
    }

    @Override
    public String toString() {

        return this.name;
    }
}