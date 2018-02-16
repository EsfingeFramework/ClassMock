package net.sf.esfinge.classmock.api;

import java.lang.annotation.Annotation;

import net.sf.esfinge.classmock.api.enums.LocationEnum;

/**
 * Class responsible to define wrapper for annotations.
 */
public interface IAnnotationWriter {

    /**
     * Add an annotation to this.
     *
     * @param annotation
     *            to associate
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation);

    /**
     * Add an annotation and defining the location of it.
     *
     * @param annotation
     *            to associate
     * @param location
     *            to be
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter annotation(final Class<? extends Annotation> annotation, LocationEnum location);

    /**
     * Add a annotation wrapper to this.
     *
     * @param annotation
     *            to associate
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter annotation(final IAnnotationReader annotation);
}