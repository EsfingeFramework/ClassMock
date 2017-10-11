package net.sf.esfinge.classmock.api;

import java.util.Collection;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;

public interface IMethodReader {

    /**
     * @return name of the method
     */
    String name();

    /**
     * @return type of the returned value
     */
    Class<?> returnType();

    /**
     * @return visibility of the method
     */
    VisibilityEnum visibility();

    /**
     * @return modifiers of the method
     */
    Collection<ModifierEnum> modifiers();

    /**
     * @return collection of parameters
     */
    Collection<IFieldReader> parameters();

    /**
     * @return collection of annotations
     */
    Collection<IAnnotationReader> annotations();

    /**
     * @return collection of exceptions
     */
    Collection<Class<?>> exceptions();
}