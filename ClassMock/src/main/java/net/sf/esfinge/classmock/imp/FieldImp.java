package net.sf.esfinge.classmock.imp;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.sf.esfinge.classmock.api.IAnnotationPropertyWriter;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IFieldReader;
import net.sf.esfinge.classmock.api.IFieldWriter;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

/**
 * Class responsible for implement all definitions of a field.
 */
public class FieldImp implements IFieldReader, IFieldWriter, Comparable<FieldImp>, Cloneable {

    private String name;

    private Class<?> type;

    private Class<?> generics;

    private Object value;

    private boolean getter = true;

    private boolean setter = true;

    private VisibilityEnum visibility = VisibilityEnum.PRIVATE;

    private final Set<ModifierEnum> modifiers = new HashSet<>();

    private final Set<IAnnotationReader> annotations = new HashSet<>();

    /**
     * Constructor to receive the field.
     *
     * @param name
     *            of the field
     * @param type
     *            class type of the field
     */
    public FieldImp(final String name, final Class<?> type) {

        this.name = name;
        this.type = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        final FieldImp fi = new FieldImp(this.name(), this.type());

        fi.modifiers.addAll(this.modifiers());
        fi.generics(this.generics());
        fi.hasGetter(this.hasGetter());
        fi.hasSetter(this.hasSetter());
        fi.value(this.value());
        fi.visibility(this.visibility());

        // Deep clone
        this.annotations().forEach(a -> {

            try {
                final AnnotationImp b = (AnnotationImp) ((AnnotationImp) a).clone();
                fi.annotation(b);
            } catch (final Exception e) {
                // Went wrong
                e.printStackTrace();
            }
        });

        return fi;
    }

    @Override
    public IFieldWriter name(final String name) {

        this.name = name;

        return this;
    }

    @Override
    public IFieldWriter type(final Class<?> type) {

        this.type = type;

        return this;
    }

    @Override
    public IFieldWriter generics(final Class<?> generics) {

        this.generics = generics;

        return this;
    }

    @Override
    public IFieldWriter value(final Object value) {

        this.value = value;

        return this;
    }

    @Override
    public IFieldWriter visibility(final VisibilityEnum visibility) {

        this.visibility = visibility;

        return this;
    }

    @Override
    public IFieldWriter modifiers(final ModifierEnum... modifiers) {

        this.modifiers.addAll(Arrays.asList(modifiers));

        return this;
    }

    @Override
    public IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation) {

        return this.annotation(annotation, LocationEnum.FIELD);
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
    public String name() {

        return this.name;
    }

    @Override
    public Class<?> type() {

        return this.type;
    }

    @Override
    public Class<?> generics() {

        return this.generics;
    }

    @Override
    public Object value() {

        return this.value;
    }

    @Override
    public VisibilityEnum visibility() {

        return this.visibility;
    }

    @Override
    public Collection<ModifierEnum> modifiers() {

        return this.modifiers;
    }

    @Override
    public Collection<IAnnotationReader> annotations() {

        return this.annotations;
    }

    @Override
    public IFieldWriter hasGetter(final boolean getter) {

        this.getter = getter;

        return this;
    }

    @Override
    public IFieldWriter hasSetter(final boolean setter) {

        this.setter = setter;

        return this;
    }

    @Override
    public boolean hasGetter() {

        return this.getter;
    }

    @Override
    public boolean hasSetter() {

        return this.setter;
    }

    @Override
    public int compareTo(final FieldImp other) {

        return this.name().compareTo(other.name());
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        sb.append("(");
        sb.append((this.type() == null) ? "No Type" : this.type().getSimpleName());
        sb.append(") ");
        sb.append(this.name());

        return sb.toString();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder()
                        .append(this.name())
                        .append(this.type())
                        .append(this.generics())
                        .append(this.hasGetter())
                        .append(this.hasSetter())
                        .append(this.visibility())
                        .append(this.modifiers())
                        // .append(this.annotations())
                        .build();
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

        final FieldImp other = (FieldImp) obj;

        return new EqualsBuilder()
                        .append(this.name(), other.name())
                        .append(this.type(), other.type())
                        .append(this.generics(), other.generics())
                        .append(this.hasGetter(), other.hasGetter())
                        .append(this.hasSetter(), other.hasSetter())
                        .append(this.visibility(), other.visibility())
                        .append(this.modifiers(), other.modifiers())
                        .append(this.annotations().size(), other.annotations().size())
                        .build()
                        && this.annotations().stream().allMatch(a -> other.annotations().contains(a));
    }
}