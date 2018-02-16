package net.sf.esfinge.classmock.api;

import java.lang.annotation.Annotation;

/**
 * Class responsible to retrieve annotations from this wrapper.
 */
public interface IAnnotationReader extends IAnnotationPropertyReader {

    /**
     * Inform the annotation that is associated to this wrapper.
     *
     * @return class of the type Annotation
     */
    Class<? extends Annotation> annotation();
}