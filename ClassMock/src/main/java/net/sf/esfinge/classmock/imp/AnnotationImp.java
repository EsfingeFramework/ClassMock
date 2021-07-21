package net.sf.esfinge.classmock.imp;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.sf.esfinge.classmock.api.IAnnotationLocationWriter;
import net.sf.esfinge.classmock.api.IAnnotationPropertyWriter;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IAnnotationWriter;
import net.sf.esfinge.classmock.api.enums.LocationEnum;

/**
 * Class responsible for implement all definitions of an annotation.
 */
public class AnnotationImp implements IAnnotationReader, IAnnotationPropertyWriter, IAnnotationLocationWriter, Cloneable {

    private IAnnotationWriter writer;

    private final Class<? extends Annotation> annotation;

    private LocationEnum location = LocationEnum.FIELD;

    private final Map<String, Object> properties = new HashMap<>();

    /***
     * Constructor to receive the annotation.
     *
     * @param annotation
     *            to bind
     */
    public AnnotationImp(final Class<? extends Annotation> annotation) {

        this.annotation = annotation;
    }

    /**
     * Constructor to receive the reader.
     *
     * @param reader
     *            to bind
     */
    public AnnotationImp(final IAnnotationReader reader) {

        this.annotation = reader.annotation();
        this.location = reader.location();
        this.properties.putAll(reader.properties());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        final AnnotationImp wp = new AnnotationImp(this);
        wp.setAnd(this.writer);

        return wp;
    }

    @Override
    public IAnnotationPropertyWriter property(final Object value) {

        return this.property("value", value);
    }

    @Override
    public IAnnotationPropertyWriter property(final String property, final Object value) {

        this.properties.put(property, value);

        return this;
    }

    @Override
    public Class<? extends Annotation> annotation() {

        return this.annotation;
    }

    @Override
    public Map<String, Object> properties() {

        return this.properties;
    }

    @Override
    public IAnnotationWriter and() {

        return this.writer;
    }

    @Override
    public void setAnd(final IAnnotationWriter writer) {

        this.writer = writer;
    }

    @Override
    public IAnnotationPropertyWriter location(final LocationEnum location) {

        this.location = location;

        return this;
    }

    @Override
    public LocationEnum location() {

        return this.location;
    }

    @Override
    public String toString() {

        return this.annotation().getSimpleName();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder()
                        .append(this.location())
                        .append(this.annotation())
                        // .append(this.properties())
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

        final AnnotationImp other = (AnnotationImp) obj;

        return new EqualsBuilder()
                        .append(this.location(), other.location())
                        .append(this.annotation(), other.annotation())
                        .build()
                        && this.properties().entrySet().stream()
                                        .allMatch(e -> other.properties().containsKey(e.getKey())
                                                        && other.properties().containsValue(e.getValue()));
    }
}