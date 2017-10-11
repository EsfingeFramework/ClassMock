package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.LocationEnum;

public interface IAnnotationLocationWriter {

    /**
     * @param location
     *            of annotation
     * @return IAnnotationPropertyWriter
     */
    IAnnotationPropertyWriter location(LocationEnum location);
}