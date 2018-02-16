package net.sf.esfinge.classmock.imp;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import net.sf.esfinge.classmock.api.IAnnotationLocationReader;
import net.sf.esfinge.classmock.api.IAnnotationLocationWriter;
import net.sf.esfinge.classmock.api.IAnnotationPropertyWriter;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IAnnotationWriter;
import net.sf.esfinge.classmock.api.enums.LocationEnum;

/**
 * Class responsible for implement all definitions of an annotation.
 */
public class AnnotationImp implements IAnnotationReader, IAnnotationPropertyWriter, IAnnotationLocationReader, IAnnotationLocationWriter {

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
        this.properties.putAll(reader.properties());
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
}