package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.LocationEnum;

/**
 * Class responsible to retrieve the annotation location.
 */
public interface IAnnotationLocationReader {

    /**
     * Inform the location where the annotation is to be.
     *
     * @return location of the Annotation
     */
    LocationEnum location();
}