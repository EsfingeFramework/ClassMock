package net.sf.esfinge.classmock.api;

import java.lang.annotation.Annotation;

public interface IAnnotationReader extends IAnnotationPropertyReader {

    /**
     * @return class of the type Annotation
     */
    Class<? extends Annotation> annotation();
}