package net.sf.esfinge.classmock.api;

import net.sf.esfinge.classmock.api.enums.LocationEnum;

public interface IAnnotationLocationReader {

    /**
     * @return location of the Annotation
     */
    LocationEnum location();
}