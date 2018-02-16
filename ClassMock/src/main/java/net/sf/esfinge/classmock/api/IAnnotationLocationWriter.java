package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.LocationEnum;

/**
 * Class responsible to define wrapper for the annotation location.
 */
public interface IAnnotationLocationWriter {

    /**
     * Define the location where the annotation is to be.
     *
     * @param location
     *            of annotation
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter location(LocationEnum location);
}