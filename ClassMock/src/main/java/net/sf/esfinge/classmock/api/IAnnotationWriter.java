package net.sf.esfinge.classmock.api;

import java.lang.annotation.Annotation;

import net.sf.esfinge.classmock.api.enums.LocationEnum;

public interface IAnnotationWriter {

    /**
     * @param annotation
     *            to associate
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation);

    /**
     *
     * @param annotation
     *            to associate
     * @param location
     *            to be
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation, LocationEnum location);

    /**
     * @param annotation
     *            to associate
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter annotation(final IAnnotationReader annotation);
}