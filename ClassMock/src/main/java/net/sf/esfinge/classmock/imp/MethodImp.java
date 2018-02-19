package net.sf.esfinge.classmock.imp;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sf.esfinge.classmock.api.IAnnotationPropertyWriter;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IFieldReader;
import net.sf.esfinge.classmock.api.IFieldWriter;
import net.sf.esfinge.classmock.api.IMethodReader;
import net.sf.esfinge.classmock.api.IMethodWriter;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible for implement all definitions of a method.
 */
public class MethodImp implements IMethodReader, IMethodWriter, Comparable<MethodImp>, Cloneable {

    private String name;

    private Object value;

    private Class<?> returnType = void.class;

    private VisibilityEnum visibility = VisibilityEnum.PUBLIC;

    private final Set<ModifierEnum> modifiers = new HashSet<>();

    private final Set<Class<?>> exceptions = new HashSet<>();

    private final Set<IFieldReader> parameters = new TreeSet<>();

    private final Set<IAnnotationReader> annotations = new HashSet<>();

    /**
     * Constructor to receive the name of the method.
     *
     * @param name
     *            of the method
     */
    public MethodImp(final String name) {

        this.name = name;
    }

    /**
     * Constructor to receive the method.
     *
     * @param method
     *            to be added
     */
    public MethodImp(final IMethodReader method) {

        this.name = method.name();
        this.returnType = method.returnType();
        this.visibility = method.visibility();
        this.modifiers.addAll(method.modifiers());
        this.exceptions.addAll(method.exceptions());
        this.parameters.addAll(method.parameters());
        this.annotations.addAll(method.annotations());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        final MethodImp mi = new MethodImp(this.name());

        mi.returnType(this.returnType());
        mi.visibility(this.visibility());
        mi.modifiers().addAll(this.modifiers());
        mi.exceptions().addAll(this.exceptions());

        // Deep clone
        this.parameters().forEach(f -> {

            try {
                final FieldImp b = (FieldImp) ((FieldImp) f).clone();
                mi.parameters().add(b);
            } catch (final Exception e) {
                // Went wrong
            }
        });

        // Deep clone
        this.annotations().forEach(a -> {

            try {
                final AnnotationImp b = (AnnotationImp) ((AnnotationImp) a).clone();
                mi.annotation(b);
            } catch (final Exception e) {
                // Went wrong
            }
        });

        return mi;
    }

    @Override
    public IMethodWriter name(final String name) {

        this.name = name;

        return this;
    }

    @Override
    public IMethodWriter returnTypeAsVoid() {

        return this.returnType(void.class);
    }

    @Override
    public IMethodWriter returnType(final Class<?> clazz) {

        this.returnType = clazz;

        return this;
    }

    @Override
    public IFieldWriter parameter(final String name, final Class<?> type) {

        final FieldImp wrapper = new FieldImp(name, type);
        this.parameters.add(wrapper);

        return wrapper;
    }

    @Override
    public String name() {

        return this.name;
    }

    @Override
    public Class<?> returnType() {

        return this.returnType;
    }

    @Override
    public Collection<IFieldReader> parameters() {

        return this.parameters;
    }

    @Override
    public IMethodWriter modifiers(final ModifierEnum... modifiers) {

        this.modifiers.addAll(Arrays.asList(modifiers));

        return this;
    }

    @Override
    public Collection<ModifierEnum> modifiers() {

        return this.modifiers;
    }

    @Override
    public IMethodWriter visibility(final VisibilityEnum visibility) {

        this.visibility = visibility;

        return this;
    }

    @Override
    public VisibilityEnum visibility() {

        return this.visibility;
    }

    @Override
    public IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation) {

        return this.annotation(annotation, LocationEnum.METHOD);
    }

    @Override
    public IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation, final LocationEnum location) {

        final AnnotationImp wrapper = new AnnotationImp(annotation);
        wrapper.location(location);

        return this.annotation(wrapper);
    }

    @Override
    public IAnnotationPropertyWriter annotation(final IAnnotationReader annotation) {

        final AnnotationImp wrapper = new AnnotationImp(annotation);
        wrapper.setAnd(this);

        this.annotations.add(wrapper);

        return wrapper;
    }

    @Override
    public Collection<IAnnotationReader> annotations() {

        return this.annotations;
    }

    @Override
    public IMethodWriter exceptions(final Class<?>... exceptions) {

        this.exceptions.addAll(Arrays.asList(exceptions));

        return this;
    }

    @Override
    public Collection<Class<?>> exceptions() {

        return this.exceptions;
    }

    @Override
    public IMethodWriter value(final Object value) {

        this.value = value;

        return this;
    }

    @Override
    public Object value() {

        return this.value;
    }

    @Override
    public int compareTo(final MethodImp other) {

        return this.name().compareTo(other.name());
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
        result = (prime * result) + ((this.parameters == null) ? 0 : this.parameters.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MethodImp other = (MethodImp) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!this.parameters.equals(other.parameters)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        return this.name;
    }
}