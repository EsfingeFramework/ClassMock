package net.sf.esfinge.classmock;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
import net.sf.esfinge.classmock.api.Self;
import net.sf.esfinge.classmock.api.enums.JavaEnum;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.FieldImp;
import net.sf.esfinge.classmock.imp.MethodImp;
import net.sf.esfinge.classmock.imp.SuperClassImp;
import net.sf.esfinge.classmock.parse.ParseASM;
import net.sf.esfinge.classmock.parse.ParseFieldSignature;
import net.sf.esfinge.classmock.parse.ParseMethodSignature;

/**
 * Class responsible for implement all definitions of: Concrete Class, Abstract Class, Enum, Interface, Annotation. It uses the builder pattern to
 * simplify the usage.
 */
public class ClassMock implements IClassReader, IClassWriter, Cloneable {

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
     * Define a name for your <b>Entity</b>. Understand Entity as a <b>generic name</b> that can represent: <i>Concrete Class, Abstract
     * Class, Enum, Interface or Annotation</i>.
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
    public IFieldWriter field(final String name) {

        return this.field(name, Self.class);
    }

    @Override
    public IFieldWriter field(final String name, final Class<?> type) {

        final FieldImp wrapper = new FieldImp(name, type);
        this.fields.add(wrapper);

        return wrapper;
    }

    @Override
    public IFieldWriter fieldByParse(final String fieldSignature) {

        final FieldImp wrapper = ParseFieldSignature.getInstance().parse(fieldSignature);
        this.fields.add(wrapper);

        return wrapper;
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
    public IMethodWriter method(final String name) {

        final MethodImp method = new MethodImp(name);
        this.methods.add(method);

        return method;
    }

    @Override
    public IMethodWriter methodByParse(final String methodSignature) {

        final MethodImp wrapper = ParseMethodSignature.getInstance().parse(methodSignature);
        this.methods.add(wrapper);

        return wrapper;
    }

    @Override
    public boolean isClass() {

        return !this.modifiers.contains(ModifierEnum.ABSTRACT)
                        && !this.modifiers.contains(ModifierEnum.ENUM)
                        && this.modifiers.contains(ModifierEnum.SUPER);
    }

    @Override
    public boolean isInterface() {

        return this.modifiers.contains(ModifierEnum.INTERFACE)
                        && !this.modifiers.contains(ModifierEnum.ANNOTATION);
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

    @Override
    protected Object clone() throws CloneNotSupportedException {

        return this.clone(this.name() + "Cloned");
    }

    @Override
    public IClassWriter clone(final String name) {

        final ClassMock clone = new ClassMock(name);

        clone.interfaces.addAll(this.interfaces());
        clone.version(this.version());
        clone.modifiers.addAll(this.modifiers());
        clone.superClassImp.superclass(this.superclass().superclass());
        clone.superClassImp.generics().addAll(this.superclass().generics());

        // Deep clone
        this.annotations().forEach(a -> {

            try {
                final AnnotationImp b = (AnnotationImp) ((AnnotationImp) a).clone();
                clone.annotation(b);
            } catch (final Exception e) {
                // Went wrong
            }
        });

        // Deep clone
        this.fields().forEach(f -> {

            try {
                final FieldImp b = (FieldImp) ((FieldImp) f).clone();
                clone.fields().add(b);
            } catch (final Exception e) {
                // Went wrong
            }
        });

        // Deep clone
        this.methods().forEach(m -> {

            try {
                final MethodImp b = (MethodImp) ((MethodImp) m).clone();
                clone.methods().add(b);
            } catch (final Exception e) {
                // Went wrong
            }
        });

        return clone;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.annotations == null) ? 0 : this.annotations.hashCode());

        return new HashCodeBuilder()
                        .append(this.name())
                        .append(this.version())
                        .append(this.visibility())
                        .append(this.modifiers())
                        .append(this.fields())
                        .append(this.methods())
                        .append(this.annotations())
                        .append(this.interfaces())
                        .append(this.superclass())
                        .build();
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }

        final ClassMock other = (ClassMock) obj;

        return new EqualsBuilder()
                        .append(this.name(), other.name())
                        .append(this.version(), other.version())
                        .append(this.visibility(), other.visibility())
                        .append(this.modifiers(), other.modifiers())
                        .append(this.fields().size(), other.fields().size())
                        .append(this.methods().size(), other.methods().size())
                        .append(this.annotations().size(), other.annotations().size())
                        .append(this.interfaces(), other.interfaces())
                        .append(this.superclass(), other.superclass())
                        .build()
                        && this.fields().stream().allMatch(f -> other.fields().contains(f))
                        && this.methods().stream().allMatch(m -> other.methods().contains(m))
                        && this.annotations().stream().allMatch(a -> other.annotations().contains(a));
    }

}